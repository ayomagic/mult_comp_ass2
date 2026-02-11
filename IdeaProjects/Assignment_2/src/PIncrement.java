import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PIncrement {
    public static int parallelIncrement(int c, int numThreads) {
        Anderson anderson = new Anderson(numThreads);
        ExecutorService exec = Executors.newFixedThreadPool(numThreads);
        List<Future<?>> futures = new ArrayList<>();

        long t0;
        long t1;
        double diff;

        final int m = 120000;
        int r = m % numThreads;
        int q = m / numThreads;

        t0 = System.nanoTime();

        AtomicInteger cFinal = new AtomicInteger(c);
        for (int i = 0; i < numThreads; i++) {
            int incThread = q;
            if (r > 0) {
                r --;
                incThread ++;
            }
            int finalIncThread = incThread;
            Future<?> future = exec.submit(() -> {
                for (int j = 0; j < finalIncThread; j++) {
                    anderson.lock();
                    cFinal.getAndIncrement();
                    anderson.unlock();
                }
            });
            futures.add(future);
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        t1 = System.nanoTime();
        diff = (t1 - t0) / 1000000.0;
        System.out.println("type=Anderson's spin lock algorithm n=" + numThreads + " time=" + diff + " ms");
        System.out.println("Final value: " + cFinal.get());

        exec.shutdown();
        return cFinal.get();
    }
    public static class Anderson {
        AtomicInteger tailSlot = new AtomicInteger(0);
        ArrayList<AtomicBoolean> available;
        ThreadLocal<Integer> mySlot = ThreadLocal.withInitial(() -> 0);
        int n;
        public Anderson(int n) {
            this.n = n;

            available = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                available.add(new AtomicBoolean(false));
            }
            available.set(0, new AtomicBoolean(true));
        }
        public void lock() {
            mySlot.set(tailSlot.getAndIncrement() % n);
            while (!available.get(mySlot.get()).get()) {}
        }
        public void unlock() {
            available.get(mySlot.get()).getAndSet(false);
            available.get((mySlot.get()+1) % n).getAndSet(true);
        }
    }
}
