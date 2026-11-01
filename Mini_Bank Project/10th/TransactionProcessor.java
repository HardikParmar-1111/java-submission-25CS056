package service;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentLinkedQueue;
import model.Account;

public class TransactionProcessor {
    private ExecutorService pool = Executors.newFixedThreadPool(4);
    private ConcurrentHashMap<String, Account> accounts;
    private AtomicInteger pendingTasks = new AtomicInteger();
    private Object taskLock = new Object();

    public TransactionProcessor(ConcurrentHashMap<String, Account> accounts) {
        this.accounts = accounts;
    }

    public void submit(Runnable task) {
        pendingTasks.incrementAndGet();
        try {
            pool.execute(() -> {
                try {
                    task.run();
                } finally {
                    if (pendingTasks.decrementAndGet() == 0) {
                        synchronized (taskLock) {
                            taskLock.notifyAll();
                        }
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            pendingTasks.decrementAndGet();
            throw e;
        }
    }

    public void processBatch(List<Runnable> tasks) throws InterruptedException {
        CountDownLatch done = new CountDownLatch(tasks.size());
        ConcurrentLinkedQueue<RuntimeException> errors = new ConcurrentLinkedQueue<>();
        for (Runnable task : tasks) {
            submit(() -> {
                try {
                    task.run();
                } catch (RuntimeException e) {
                    errors.add(e);
                } finally {
                    done.countDown();
                }
            });
        }

        if (!done.await(1, TimeUnit.MINUTES)) {
            throw new IllegalStateException("Transaction batch took too long");
        }
        if (!errors.isEmpty()) {
            throw new IllegalStateException("A transaction in the batch failed",
                    errors.peek());
        }
    }

    public void awaitCompletion() throws InterruptedException {
        long endTime = System.nanoTime() + TimeUnit.MINUTES.toNanos(1);
        synchronized (taskLock) {
            while (pendingTasks.get() > 0) {
                long remaining = endTime - System.nanoTime();
                if (remaining <= 0) {
                    throw new IllegalStateException("Transaction batch took too long");
                }
                TimeUnit.NANOSECONDS.timedWait(taskLock, remaining);
            }
        }
    }

    public void stop() throws InterruptedException {
        pool.shutdown();
        if (!pool.awaitTermination(1, TimeUnit.MINUTES)) {
            pool.shutdownNow();
            if (!pool.awaitTermination(1, TimeUnit.MINUTES)) {
                throw new IllegalStateException("Transaction pool did not stop");
            }
        }
    }

    public ConcurrentHashMap<String, Account> getAccounts() {
        return accounts;
    }
}
