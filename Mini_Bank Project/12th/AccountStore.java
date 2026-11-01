package service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import model.Account;

public class AccountStore {
    private Map<String, Account> accounts = new HashMap<>();
    private ArrayList<String> history = new ArrayList<>();

    public synchronized void add(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public synchronized Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public synchronized ArrayList<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    public synchronized ArrayList<Account> sortedByBalance() {
        ArrayList<Account> sorted = getAll();
        sorted.sort(Comparator.comparingLong(Account::getBalance).reversed());
        return sorted;
    }

    public synchronized void addTransaction(String line) {
        history.add(line);
    }

    public synchronized ArrayList<String> getHistory() {
        return new ArrayList<>(history);
    }
}
