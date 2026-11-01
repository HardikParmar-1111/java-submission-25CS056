package service;

public class TransactionBuffer {
    private Runnable[] items;
    private int count;
    private int nextIn;
    private int nextOut;
    private boolean closed;

    public TransactionBuffer(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size must be greater than zero");
        }
        items = new Runnable[size];
    }

    public synchronized void put(Runnable item) throws InterruptedException {
        while (count == items.length && !closed) {
            wait();
        }
        if (closed) {
            throw new IllegalStateException("Buffer is closed");
        }

        items[nextIn] = item;
        nextIn = (nextIn + 1) % items.length;
        count++;
        notifyAll();
    }

    public synchronized Runnable take() throws InterruptedException {
        while (count == 0 && !closed) {
            wait();
        }
        if (count == 0) {
            return null;
        }

        Runnable item = items[nextOut];
        items[nextOut] = null;
        nextOut = (nextOut + 1) % items.length;
        count--;
        notifyAll();
        return item;
    }

    public synchronized void close() {
        closed = true;
        notifyAll();
    }
}
