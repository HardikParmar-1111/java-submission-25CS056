package model;

public class SavingsAccount extends Account implements Premium {
    private static final long serialVersionUID = 1L;
    private long minBalance;

    public SavingsAccount(String ownerName, long balance, long minBalance) {
        super(ownerName, balance);
        if (minBalance < 0) {
            throw new IllegalArgumentException("Minimum balance cannot be negative");
        }
        this.minBalance = minBalance;
    }

    @Override
    public double interestRate() {
        return 4.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        if (amount <= 0) {
            return false;
        }
        try {
            return Math.subtractExact(getBalance(), amount) >= minBalance;
        } catch (ArithmeticException e) {
            return false;
        }
    }

    @Override
    protected long availableToWithdraw() {
        long balance = getBalance();
        return balance > minBalance ? balance - minBalance : 0;
    }

    public long getMinBalance() {
        return minBalance;
    }
}
