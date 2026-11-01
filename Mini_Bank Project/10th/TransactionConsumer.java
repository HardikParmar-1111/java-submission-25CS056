package service;

public class TransactionConsumer implements Runnable {
    private TransactionBuffer buffer;
    private TransactionProcessor processor;

    public TransactionConsumer(TransactionBuffer buffer, TransactionProcessor processor) {
        this.buffer = buffer;
        this.processor = processor;
    }

    @Override
    public void run() {
        try {
            Runnable transaction;
            while ((transaction = buffer.take()) != null) {
                processor.submit(transaction);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Transaction consumer was interrupted");
        }
    }
}
