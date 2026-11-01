package model;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.annotation.Id;
import model.annotation.MaxLength;
import model.annotation.Positive;

public abstract class Account implements Transactable, InterestBearing,
        Comparable<Account>, Serializable {
    private static final long serialVersionUID = 1L;
    private static final AtomicLong accountCounter = new AtomicLong();

    @Id
    private final String accountNumber;
    @MaxLength(50)
    private String ownerName;
    @Positive
    private long balance;
    private boolean active;
    private boolean transferInProgress;

    protected Account(String ownerName, long balance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = balance;
        this.active = true;
    }

    protected Account(String ownerName) {
        this(ownerName, 0);
    }

    private static String generateAccountNumber() {
        return String.format("AC%04d", accountCounter.incrementAndGet());
    }

    public static void updateAccountCounter(String accountNumber) {
        if (accountNumber != null && accountNumber.startsWith("AC")) {
            try {
                long value = Long.parseLong(accountNumber.substring(2));
                accountCounter.accumulateAndGet(value, Math::max);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Invalid account number: " + accountNumber, e);
            }
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public synchronized long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    protected synchronized void setBalance(long balance) {
        this.balance = balance;
    }

    public abstract boolean canWithdraw(long amount);

    protected long availableToWithdraw() {
        return Math.max(0, getBalance());
    }

    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
        try {
            balance = Math.addExact(balance, amount);
        } catch (ArithmeticException e) {
            throw new InvalidAmountException("Balance is too large");
        }
    }

    @Override
    public synchronized boolean withdraw(long amount)
            throws InvalidAmountException, InsufficientFundsException {
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
        if (!canWithdraw(amount)) {
            long available = availableToWithdraw();
            long shortfall = Math.max(0, amount - available);
            throw new InsufficientFundsException(
                    "Not enough balance in account " + accountNumber, shortfall);
        }
        balance -= amount;
        return true;
    }

    public void transfer(Account to, long amount) throws BankException {
        if (to == null) {
            throw new AccountNotFoundException("destination");
        }
        Account first = compareTo(to) <= 0 ? this : to;
        Account second = first == this ? to : this;

        synchronized (first) {
            synchronized (second) {
                transferInProgress = true;
                try {
                    if (amount <= 0) {
                        throw new InvalidAmountException(
                                "Amount must be greater than zero");
                    }
                    try {
                        Math.addExact(to.balance, amount);
                    } catch (ArithmeticException e) {
                        throw new InvalidAmountException("Destination balance is too large");
                    }
                    withdraw(amount);
                    to.deposit(amount);
                } catch (BankException e) {
                    throw e;
                } finally {
                    transferInProgress = false;
                }
            }
        }
    }

    @Override
    public int compareTo(Account other) {
        return accountNumber.compareTo(other.accountNumber);
    }

    @Override
    public synchronized String toString() {
        return accountNumber + " | " + ownerName + " | " + balance;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Account)) {
            return false;
        }
        Account other = (Account) obj;
        return accountNumber.equals(other.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}
