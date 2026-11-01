public class DeadlockFixed {
    public static void main(String[] args) throws InterruptedException {
        Object first = new Object();
        Object second = new Object();

        Thread one = new Thread(() -> useLocks(first, second));
        Thread two = new Thread(() -> useLocks(first, second));
        one.start();
        two.start();
        one.join();
        two.join();
        System.out.println("Both threads finished");
    }

    private static void useLocks(Object first, Object second) {
        synchronized (first) {
            synchronized (second) {
                System.out.println(Thread.currentThread().getName() + " finished");
            }
        }
    }
}
