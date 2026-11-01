import exception.BankException;
import model.Account;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;

public class Practical02Demo {
    public static void main(String[] args) {
        Account[] accounts = {
            new SavingsAccount("Riya", 5000, 1000),
            new CurrentAccount("Jay", 3000, 1000),
            new FixedDepositAccount("Kiran", 10000)
        };

        try {
            accounts[0].deposit(2000);
            accounts[1].withdraw(500);
            accounts[2].deposit(1000);
        } catch (BankException e) {
            System.out.println(e.getMessage());
        }

        for (Account account : accounts) {
            System.out.println(account.getAccountNumber() + " balance: "
                    + account.getBalance());
        }
    }
}
