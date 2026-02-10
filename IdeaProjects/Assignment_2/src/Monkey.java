import java.util.concurrent.locks.ReentrantLock;

public class Monkey {
    // declare the variables here
    // A monkey calls the method when it arrives at the river bank and
    // wants to climb the rope in the specified direction (0 or 1);
    // Kong’s direction is -1.
    // The method blocks a monkey until it is allowed to climb the rope.
    int direction;
    int capacity;
    int current;
    boolean kongWaiting;
    boolean kongPresent;
    LinkedList rope;
    ReentrantLock ropeLock;

    Monkey() {
        capacity = 3;
        direction = 0;
        kongWaiting = false;
        kongPresent = false;
        rope = new LinkedList(capacity);
        ropeLock = new ReentrantLock();
    }

    public void ClimbRope(int direction) throws InterruptedException {
        if (direction == -1) { kongWaiting = true;}
        while (rope.isFull() || (direction != rope.head.direction && rope.size >= 1) || kongWaiting || kongPresent) {
            this.wait();
        }
        // Entering CS ...
        ropeLock.lock();
        if (direction == -1) { // If the monkey is Kong, signal
            kongPresent = true;
            kongWaiting = false;
        }
        LinkedList.Node monkey = new LinkedList.Node(direction);
        this.rope.push(monkey); // this method is atomic
        this.rope.head.direction = direction;
        ropeLock.unlock();
        // Exiting CS ...
    }
    public void LeaveRope() {
        // Entering CS ...
        ropeLock.lock();
        this.rope.pop();
        this.notifyAll();
        ropeLock.unlock();
        // Exiting CS ...
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
