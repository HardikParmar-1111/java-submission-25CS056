package util;

import model.Account;

public class StatementFormatter {
    public static String buildStatement(Account account) {
        StringBuilder statement = new StringBuilder();
        statement.append("Account statement\n");
        statement.append("Account number: ").append(account.getAccountNumber()).append('\n');
        statement.append("Owner: ").append(account.getOwnerName()).append('\n');
        statement.append("Balance: ").append(account.getBalance()).append('\n');
        statement.append("Interest rate: ").append(account.interestRate()).append("%\n");
        statement.append("Yearly interest: ").append(account.yearlyInterest()).append('\n');
        statement.append("Active: ").append(account.isActive());
        return statement.toString();
    }
}
