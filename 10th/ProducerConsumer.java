import java.util.LinkedList;
import java.util.Queue;

class SharedBuffer {
    private Queue<Integer> items = new LinkedList<>();
    private int size;

    SharedBuffer(int size) {
        this.size = size;
    }

    public synchronized void put(int value) throws InterruptedException {
        while (items.size() == size) {
            wait();
        }

        items.add(value);
        System.out.println("Produced: " + value);
        notify();
    }

    public synchronized int get() throws InterruptedException {
        while (items.isEmpty()) {
            wait();
        }

        int value = items.remove();
        System.out.println("Consumed: " + value);
        notify();
        return value;
    }
}

class Producer implements Runnable {
    private SharedBuffer buffer;
    private int count;

    Producer(SharedBuffer buffer, int count) {
        this.buffer = buffer;
        this.count = count;
    }

    public void run() {
        try {
            for (int i = 1; i <= count; i++) {
                buffer.put(i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Producer was interrupted");
        }
    }
}

class Consumer implements Runnable {
    private SharedBuffer buffer;
    private int count;

    Consumer(SharedBuffer buffer, int count) {
        this.buffer = buffer;
        this.count = count;
    }

    public void run() {
        try {
            for (int i = 0; i < count; i++) {
                buffer.get();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Consumer was interrupted");
        }
    }
}

public class ProducerConsumer {
    public static void main(String[] args) throws InterruptedException {
        int itemCount = 10;
        SharedBuffer buffer = new SharedBuffer(3);
        Thread producer = new Thread(new Producer(buffer, itemCount));
        Thread consumer = new Thread(new Consumer(buffer, itemCount));

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }
}
