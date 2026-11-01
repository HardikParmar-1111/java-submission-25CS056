package service;

public class BankSession implements AutoCloseable {
    public void start() {
        System.out.println("Bank session started");
    }

    @Override
    public void close() {
        System.out.println("Bank session closed");
    }
}
