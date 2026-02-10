import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class PIncrement {
    public static int parallelIncrement(int c, int numThreads) {
        Anderson anderson = new Anderson();
        ExecutorService exec = Executors.newFixedThreadPool(numThreads);
        List<Future<?>> futures = new ArrayList<>();

        long t0;
        long t1;
        long diff;

        final int m = 1200000;
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
                    anderson.requestCS();
                    cFinal.getAndIncrement();
                    anderson.releaseCS();
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
        diff = t1 - t0;
        System.out.println("type=Anderson’s spin lock algorithm n=" + numThreads + " time=" + diff + " ms");

        exec.shutdown();
        return 0;
    }
    public static class Anderson {
        public Anderson() {

        }

        public void requestCS() {

        }

        public void releaseCS() {

        }

    }
}
