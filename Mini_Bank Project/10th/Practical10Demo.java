import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import model.Account;
import model.CurrentAccount;
import service.TransactionBuffer;
import service.TransactionConsumer;
import service.TransactionProcessor;
import service.TransactionProducer;

public class Practical10Demo {
    public static void main(String[] args) throws InterruptedException {
        Account first = new CurrentAccount("Riya", 1000, 1000);
        Account second = new CurrentAccount("Jay", 1000, 1000);
        ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();
        accounts.put(first.getAccountNumber(), first);
        accounts.put(second.getAccountNumber(), second);

        TransactionProcessor processor = new TransactionProcessor(accounts);
        TransactionBuffer buffer = new TransactionBuffer(3);
        List<Runnable> transactions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            transactions.add(() -> {
                try {
                    first.deposit(100);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            });
        }

        Thread producer = new Thread(
                new TransactionProducer(buffer, transactions));
        Thread consumer = new Thread(
                new TransactionConsumer(buffer, processor));
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        processor.awaitCompletion();

        Thread transferOne = new Thread(() -> transfer(first, second));
        Thread transferTwo = new Thread(() -> transfer(second, first));
        transferOne.start();
        transferTwo.start();
        transferOne.join();
        transferTwo.join();
        processor.stop();

        System.out.println(first);
        System.out.println(second);
    }

    private static void transfer(Account from, Account to) {
        try {
            from.transfer(to, 100);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
