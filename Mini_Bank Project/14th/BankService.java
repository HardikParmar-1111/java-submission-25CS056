package service;

import static util.Validator.isValidEmail;
import static util.Validator.isValidMobile;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InvalidAmountException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import model.Account;
import model.CurrentAccount;
import model.Customer;
import model.FixedDepositAccount;
import model.SavingsAccount;
import model.annotation.AnnotationValidator;
import repository.Repository;
import util.ReportGenerator;
import util.StatePersister;
import util.StatementFormatter;
import util.TransactionLog;

public class BankService {
    private Repository<Account> accountRepository = new Repository<>();
    private Repository<Customer> customerRepository = new Repository<>();
    private AccountStore accountStore = new AccountStore();
    private ConcurrentHashMap<String, Account> sharedAccounts =
            new ConcurrentHashMap<>();
    private TransactionProcessor transactionProcessor =
            new TransactionProcessor(sharedAccounts);
    private Path dataFolder;
    private Path stateFile;
    private Path logFolder;
    private Path logFile;
    private Path reportFile;

    public BankService(Path dataFolder) throws IOException, ClassNotFoundException {
        this.dataFolder = dataFolder;
        this.stateFile = dataFolder.resolve("accounts.dat");
        this.logFolder = dataFolder.resolve("logs");
        this.logFile = logFolder.resolve("transactions.log");
        this.reportFile = dataFolder.resolve("report.txt");
        Files.createDirectories(dataFolder);
        Files.createDirectories(logFolder);

        if (Files.exists(stateFile)) {
            Account[] savedAccounts = StatePersister.load(stateFile);
            for (Account account : savedAccounts) {
                Account.updateAccountCounter(account.getAccountNumber());
                addAccount(account);
            }
        }
    }

    public Account openAccount(String name, String email, String mobile,
            String type, long openingBalance) throws BankException,
            IllegalAccessException {
        if (name == null || name.isBlank()) {
            throw new InvalidAmountException("Name cannot be empty");
        }
        if (!isValidEmail(email)) {
            throw new InvalidAmountException("Email is not valid");
        }
        if (!isValidMobile(mobile)) {
            throw new InvalidAmountException("Mobile number is not valid");
        }
        if (openingBalance < 0) {
            throw new InvalidAmountException("Opening balance cannot be negative");
        }

        Customer customer = new Customer(name, email, mobile);
        String[] errors = AnnotationValidator.validate(customer);
        if (errors.length > 0) {
            throw new InvalidAmountException(String.join(", ", errors));
        }

        Account account;
        switch (type.toLowerCase()) {
            case "savings" -> account = new SavingsAccount(name, openingBalance, 1000);
            case "current" -> account = new CurrentAccount(name, openingBalance, 1000);
            case "fixed" -> account = new FixedDepositAccount(name, openingBalance);
            default -> throw new InvalidAmountException("Unknown account type");
        }

        customerRepository.save(customer);
        addAccount(account);
        return account;
    }

    public void deposit(String accountNumber, long amount)
            throws BankException, IOException {
        Account account = findAccount(accountNumber);
        account.deposit(amount);
        String line = "DEPOSIT " + accountNumber + " " + amount;
        recordTransaction(line);
    }

    public void withdraw(String accountNumber, long amount)
            throws BankException, IOException {
        Account account = findAccount(accountNumber);
        account.withdraw(amount);
        String line = "WITHDRAW " + accountNumber + " " + amount;
        recordTransaction(line);
    }

    public void transfer(String fromNumber, String toNumber, long amount)
            throws BankException, IOException {
        Account from = findAccount(fromNumber);
        Account to = findAccount(toNumber);
        from.transfer(to, amount);
        recordTransaction("WITHDRAW " + fromNumber + " " + amount);
        recordTransaction("DEPOSIT " + toNumber + " " + amount);
    }

    public String statement(String accountNumber) throws BankException {
        return StatementFormatter.buildStatement(findAccount(accountNumber));
    }

    public ArrayList<Account> listAccounts() {
        return accountStore.sortedByBalance();
    }

    public ArrayList<String> getTransactionHistory() {
        return accountStore.getHistory();
    }

    public String generateReport() throws IOException {
        return ReportGenerator.generate(logFolder, reportFile);
    }

    public void saveAccounts() throws IOException {
        StatePersister.save(accountStore.getAll().toArray(new Account[0]), stateFile);
    }

    public void processBatch(List<Runnable> tasks) throws InterruptedException {
        transactionProcessor.processBatch(tasks);
    }

    public void runDepositBatch(String accountNumber, int times, long amount)
            throws BankException, IOException, InterruptedException {
        if (times <= 0 || amount <= 0) {
            throw new InvalidAmountException(
                    "Batch count and deposit amount must be greater than zero");
        }

        findAccount(accountNumber);
        ArrayList<Runnable> tasks = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            tasks.add(() -> {
                try {
                    Account account = transactionProcessor.getAccounts().get(accountNumber);
                    if (account == null) {
                        throw new IllegalStateException(
                                "Account not found: " + accountNumber);
                    }
                    account.deposit(amount);
                    String line = "DEPOSIT " + accountNumber + " " + amount;
                    recordTransaction(line);
                } catch (BankException | IOException e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            });
        }
        transactionProcessor.processBatch(tasks);
    }

    public void processBufferedBatch(List<Runnable> tasks) throws InterruptedException {
        TransactionBuffer buffer = new TransactionBuffer(3);
        Thread producer = new Thread(new TransactionProducer(buffer, tasks), "TransactionProducer");
        Thread consumer = new Thread(
                new TransactionConsumer(buffer, transactionProcessor), "TransactionConsumer");
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        transactionProcessor.awaitCompletion();
    }

    public TransactionProcessor getTransactionProcessor() {
        return transactionProcessor;
    }

    public void stop() throws InterruptedException {
        transactionProcessor.stop();
    }

    private void addAccount(Account account) {
        accountRepository.save(account);
        accountStore.add(account);
        sharedAccounts.put(account.getAccountNumber(), account);
    }

    private Account findAccount(String accountNumber) throws AccountNotFoundException {
        Account account = accountRepository.findById(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException(accountNumber);
        }
        return account;
    }

    private void recordTransaction(String line) throws IOException {
        TransactionLog.append(logFile, line);
        accountStore.addTransaction(line);
    }
}
