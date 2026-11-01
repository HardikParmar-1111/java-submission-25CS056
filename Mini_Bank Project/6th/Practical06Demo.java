import model.Account;
import model.SavingsAccount;
import model.WithdrawRule;

public class Practical06Demo {
    public static void main(String[] args) {
        Account account = new SavingsAccount("Riya", 5000, 1000);

        WithdrawRule ruleOne = new WithdrawRule() {
            public boolean allow(Account account, long amount) {
                return amount > 0 && account.canWithdraw(amount);
            }
        };

        WithdrawRule ruleTwo = (accountToCheck, amount) ->
                amount > 0 && accountToCheck.canWithdraw(amount);

        System.out.println("Anonymous rule: " + ruleOne.allow(account, 1000));
        System.out.println("Lambda rule: " + ruleTwo.allow(account, 1000));
    }
}
