import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import exception.BankException;
import exception.InsufficientFundsException;
import model.Account;
import model.BankInfo;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.MenuOption;
import model.SavingsAccount;
import service.BankService;
import service.BankSession;

public class MiniBank {
    public static void main(String[] args) {
        BankService bank = null;

        try (Scanner sc = new Scanner(System.in);
             BankSession session = new BankSession()) {
            session.start();
            bank = new BankService(Path.of("data"));
            System.out.println(new BankInfo("MiniBank", "Main Branch"));
            menu(sc, bank);
            bank.saveAccounts();
            System.out.println("Goodbye");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Could not load or save bank data: " + e.getMessage());
        } finally {
            if (bank != null) {
                try {
                    bank.stop();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Bank stopped before all tasks finished");
                }
            }
        }
    }

    private static void menu(Scanner sc, BankService bank) {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("1. Open account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Account statement");
            System.out.println("6. List accounts");
            System.out.println("7. Transaction report");
            System.out.println("8. Batch deposits");
            System.out.println("9. Exit");
            System.out.print("Choose: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a number from 1 to 9");
                continue;
            }

            MenuOption option = switch (choice) {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.STATEMENT;
                case 6 -> MenuOption.LIST_ACCOUNTS;
                case 7 -> MenuOption.REPORT;
                case 8 -> MenuOption.BATCH_DEPOSIT;
                case 9 -> MenuOption.EXIT;
                default -> null;
            };

            if (option == null) {
                System.out.println("Invalid choice");
                continue;
            }

            try {
                switch (option) {
                    case OPEN_ACCOUNT -> openAccount(sc, bank);
                    case DEPOSIT -> deposit(sc, bank);
                    case WITHDRAW -> withdraw(sc, bank);
                    case TRANSFER -> transfer(sc, bank);
                    case STATEMENT -> {
                        System.out.print("Account number: ");
                        System.out.println(bank.statement(sc.nextLine().trim()));
                    }
                    case LIST_ACCOUNTS -> {
                        for (Account account : bank.listAccounts()) {
                            System.out.println(account);
                        }
                    }
                    case REPORT -> System.out.println(bank.generateReport());
                    case BATCH_DEPOSIT -> batchDeposit(sc, bank);
                    case EXIT -> running = false;
                }
            } catch (BankException | IOException | IllegalAccessException e) {
                if (e instanceof InsufficientFundsException) {
                    InsufficientFundsException funds = (InsufficientFundsException) e;
                    System.out.println(e.getMessage() + ". Short by " + funds.getShortfall());
                } else {
                    System.out.println(e.getMessage());
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid amount");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("The operation was interrupted");
            } catch (IllegalStateException e) {
                System.out.println("Transaction failed: " + e.getMessage());
            }
        }
    }

    private static void openAccount(Scanner sc, BankService bank)
            throws BankException, IllegalAccessException {
        System.out.print("Customer name: ");
        String name = sc.nextLine().trim();
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        System.out.print("Mobile: ");
        String mobile = sc.nextLine().trim();
        System.out.print("Account type (savings/current/fixed): ");
        String type = sc.nextLine().trim();
        System.out.print("Opening balance: ");
        long balance = Long.parseLong(sc.nextLine().trim());

        Account account = bank.openAccount(name, email, mobile, type, balance);
        System.out.println("Account opened: " + account);
        if (account instanceof SavingsAccount savings) {
            System.out.println("Minimum balance: " + savings.getMinBalance());
        } else if (account instanceof CurrentAccount current) {
            System.out.println("Overdraft limit: " + current.getOverdraftLimit());
        } else if (account instanceof FixedDepositAccount) {
            System.out.println("This account is locked for withdrawal");
        }
    }

    private static void deposit(Scanner sc, BankService bank)
            throws BankException, IOException {
        System.out.print("Account number: ");
        String accountNumber = sc.nextLine().trim();
        System.out.print("Amount: ");
        long amount = Long.parseLong(sc.nextLine().trim());
        bank.deposit(accountNumber, amount);
        System.out.println("Deposit completed");
    }

    private static void withdraw(Scanner sc, BankService bank)
            throws BankException, IOException {
        System.out.print("Account number: ");
        String accountNumber = sc.nextLine().trim();
        System.out.print("Amount: ");
        long amount = Long.parseLong(sc.nextLine().trim());
        bank.withdraw(accountNumber, amount);
        System.out.println("Withdrawal completed");
    }

    private static void transfer(Scanner sc, BankService bank)
            throws BankException, IOException {
        System.out.print("From account: ");
        String from = sc.nextLine().trim();
        System.out.print("To account: ");
        String to = sc.nextLine().trim();
        System.out.print("Amount: ");
        long amount = Long.parseLong(sc.nextLine().trim());
        bank.transfer(from, to, amount);
        System.out.println("Transfer completed");
    }

    private static void batchDeposit(Scanner sc, BankService bank)
            throws BankException, IOException, InterruptedException {
        System.out.print("Account number: ");
        String accountNumber = sc.nextLine().trim();
        System.out.print("Number of deposits: ");
        int times = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Amount for each deposit: ");
        long amount = Long.parseLong(sc.nextLine().trim());
        bank.runDepositBatch(accountNumber, times, amount);
        System.out.println("Batch completed");
    }
}
