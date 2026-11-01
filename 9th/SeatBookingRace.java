import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

class Seats {
    private int seatsLeft = 5;

    boolean bookWithoutSync(CyclicBarrier barrier)
            throws InterruptedException, BrokenBarrierException {
        if (seatsLeft > 0) {
            barrier.await();
            seatsLeft--;
            return true;
        }
        return false;
    }

    synchronized boolean book() {
        if (seatsLeft > 0) {
            seatsLeft--;
            return true;
        }
        return false;
    }

    int getSeatsLeft() {
        return seatsLeft;
    }
}

class BookingWorker implements Runnable {
    private Seats seats;
    private AtomicInteger booked;
    private CyclicBarrier barrier;
    private boolean useSync;

    BookingWorker(Seats seats, AtomicInteger booked, CyclicBarrier barrier, boolean useSync) {
        this.seats = seats;
        this.booked = booked;
        this.barrier = barrier;
        this.useSync = useSync;
    }

    public void run() {
        try {
            boolean success;
            if (useSync) {
                success = seats.book();
            } else {
                success = seats.bookWithoutSync(barrier);
            }

            if (success) {
                booked.incrementAndGet();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Booking was interrupted.");
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}

public class SeatBookingRace {
    public static void main(String[] args) throws InterruptedException {
        int threadCount = 10;
        Seats seats = new Seats();
        AtomicInteger booked = new AtomicInteger();
        CyclicBarrier barrier = new CyclicBarrier(threadCount);
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(
                    new BookingWorker(seats, booked, barrier, false));
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("Without synchronization, booked: " + booked.get());
        System.out.println("Seats left: " + seats.getSeatsLeft());

        seats = new Seats();
        booked = new AtomicInteger();
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(
                    new BookingWorker(seats, booked, null, true));
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("With synchronization, booked: " + booked.get());
        System.out.println("Seats left: " + seats.getSeatsLeft());
    }
}
