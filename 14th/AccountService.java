import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AccountService {
    private Map<String, Account> accounts = new HashMap<>();

    public Account openAccount(String number, String owner, double balance) {
        if (accounts.containsKey(number)) {
            throw new IllegalArgumentException("Account number already exists");
        }
        Account account = new Account(number, owner, balance);
        accounts.put(number, account);
        return account;
    }

    public Account findAccount(String number) {
        Account account = accounts.get(number);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        return account;
    }

    public Collection<Account> getAccounts() {
        return accounts.values();
    }
}
