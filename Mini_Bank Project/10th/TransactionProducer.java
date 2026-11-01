package service;

import java.util.List;

public class TransactionProducer implements Runnable {
    private TransactionBuffer buffer;
    private List<Runnable> transactions;

    public TransactionProducer(TransactionBuffer buffer, List<Runnable> transactions) {
        this.buffer = buffer;
        this.transactions = transactions;
    }

    @Override
    public void run() {
        try {
            for (Runnable transaction : transactions) {
                buffer.put(transaction);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Transaction producer was interrupted");
        } finally {
            buffer.close();
        }
    }
}
