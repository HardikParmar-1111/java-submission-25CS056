import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.CountDownLatch;

public class DeadlockDemo {
    public static void main(String[] args) throws InterruptedException {
        Object first = new Object();
        Object second = new Object();
        CountDownLatch ready = new CountDownLatch(2);

        Thread one = new Thread(() -> {
            synchronized (first) {
                ready.countDown();
                try {
                    ready.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                synchronized (second) {
                    System.out.println("Thread one finished");
                }
            }
        });

        Thread two = new Thread(() -> {
            synchronized (second) {
                ready.countDown();
                try {
                    ready.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                synchronized (first) {
                    System.out.println("Thread two finished");
                }
            }
        });

        one.setDaemon(true);
        two.setDaemon(true);
        one.start();
        two.start();
        one.join(100);
        two.join(100);

        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long[] deadlocked = bean.findDeadlockedThreads();
        System.out.println(deadlocked == null ? "No deadlock found" : "Deadlock detected");
    }
}
