package model;

public class CurrentAccount extends Account {
    private static final long serialVersionUID = 1L;
    private long overdraftLimit;

    public CurrentAccount(String ownerName, long balance, long overdraftLimit) {
        super(ownerName, balance);
        if (overdraftLimit < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative");
        }
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public double interestRate() {
        return 0.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        if (amount <= 0) {
            return false;
        }
        try {
            return Math.subtractExact(getBalance(), amount) >= -overdraftLimit;
        } catch (ArithmeticException e) {
            return false;
        }
    }

    @Override
    protected long availableToWithdraw() {
        long balance = getBalance();
        if (balance > Long.MAX_VALUE - overdraftLimit) {
            return Long.MAX_VALUE;
        }
        return Math.max(0, balance + overdraftLimit);
    }

    public long getOverdraftLimit() {
        return overdraftLimit;
    }
}
