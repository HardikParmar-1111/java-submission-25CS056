class Counter {
    int count;

    void increment() {
        count++;
    }

    synchronized void incrementSynchronized() {
        count++;
    }
}

class CounterWorker implements Runnable {
    private Counter counter;
    private int times;
    private boolean useSync;

    CounterWorker(Counter counter, int times, boolean useSync) {
        this.counter = counter;
        this.times = times;
        this.useSync = useSync;
    }

    public void run() {
        for (int i = 0; i < times; i++) {
            if (useSync) {
                counter.incrementSynchronized();
            } else {
                counter.increment();
            }
        }
    }
}

public class CounterRace {
    public static void main(String[] args) throws InterruptedException {
        int threadCount = 10;
        int times = 100000;
        Counter counter = new Counter();
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new CounterWorker(counter, times, false));
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("Without synchronization: " + counter.count);

        counter = new Counter();
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new CounterWorker(counter, times, true));
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("With synchronization: " + counter.count);
    }
}
