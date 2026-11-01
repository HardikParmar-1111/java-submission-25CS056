package util;

import model.Command;
import model.TransactionType;

public class CommandParser {
    public static Command parse(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Command cannot be empty");
        }

        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3) {
            throw new IllegalArgumentException(
                    "Enter a transaction type, account number and amount");
        }

        try {
            TransactionType type = TransactionType.valueOf(parts[0].toUpperCase());
            long amount = Long.parseLong(parts[2]);
            return new Command(type, parts[1], amount);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid transaction command", e);
        }
    }
}
