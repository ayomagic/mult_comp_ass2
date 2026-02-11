import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Monkey {
    int direction;
    int capacity;
    boolean kongWaiting;
    boolean kongPresent;
    LinkedList rope;
    ReentrantLock ropeLock;
    Condition canClimb;

    Monkey() {
        capacity = 3;
        direction = 0;
        kongWaiting = false;
        kongPresent = false;
        rope = new LinkedList(capacity);
        ropeLock = new ReentrantLock();
        canClimb = ropeLock.newCondition();
    }

    public void ClimbRope(int direction) throws InterruptedException {
        ropeLock.lock();
        try {
            if (direction == -1) {
                kongWaiting = true;
            }
            while (rope.isFull() || (direction != rope.head.direction && rope.size >= 1) || kongWaiting || kongPresent) {
                canClimb.await();
            }
            if (direction == -1) {
                kongPresent = true;
                kongWaiting = false;
            }
            LinkedList.Node monkey = new LinkedList.Node(direction);
            rope.push(monkey);
            rope.head.direction = direction;
        } finally {
            ropeLock.unlock();
        }
    }
    
    public void LeaveRope() {
        ropeLock.lock();
        try {
            rope.pop();
            canClimb.signalAll();
        } finally {
            ropeLock.unlock();
        }
    }
    /**
     * Returns the number of monkeys on the rope currently for test purpose.
     *
     * Positive Test Cases:
     * case 1: normal monkey (0 and 1)on the rope, this value should <= 3, >= 0
     * case 2: when Kong is on the rope, this value should be 1
     */
    public int getNumMonkeysOnRope() {
        return this.rope.getSize();
    }
}
