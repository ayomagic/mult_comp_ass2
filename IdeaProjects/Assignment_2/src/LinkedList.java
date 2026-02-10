import java.util.concurrent.locks.ReentrantLock;

public class LinkedList {
    public static class Node {
        Node next;
        Node prev;
        int direction;
        ReentrantLock lock;

        Node() {
            this.next = null;
            this.prev = null;
            this.direction = 0;
            lock = new ReentrantLock();
        }
        Node(int direction) {
            this.next = null;
            this.prev = null;
            this.direction = direction;
            lock = new ReentrantLock();
        }

    }
    int capacity;
    int size;
    Node head;
    Node tail;
    public LinkedList() {
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
        capacity = 0;
        size = 0;
    }
    public LinkedList(int cap) {
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
        capacity = cap;
        size = 0;
    }
    public synchronized void push(Node node) {
        if (isFull()) {
            System.out.println("Error trying to push to full queue");
            return;
        }
        head.lock.lock();
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
        size += 1;
    }
    public synchronized void pop() {
        if (isEmpty()) {
            System.out.println("Error trying to pop from empty queue");
            return;
        }
        tail.prev = tail.prev.prev;
        tail.prev.next = tail;
    }
    public synchronized boolean isFull() {
        return this.size >= this.capacity;
    }
    public synchronized boolean isEmpty() {
        return this.size == 0;
    }
    public synchronized int getSize() {return this.size;}
}
