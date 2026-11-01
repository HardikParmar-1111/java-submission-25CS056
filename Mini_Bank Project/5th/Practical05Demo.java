import model.Account;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;

public class Practical05Demo {
    public static void main(String[] args) {
        Account[] accounts = {
            new SavingsAccount("Riya", 5000, 1000),
            new CurrentAccount("Jay", 3000, 1000),
            new FixedDepositAccount("Kiran", 10000)
        };

        for (Account account : accounts) {
            System.out.println(account.getOwnerName() + " interest: "
                    + account.interestRate());
            if (account instanceof SavingsAccount savings) {
                System.out.println("Minimum balance: " + savings.getMinBalance());
            }
        }
    }
}
