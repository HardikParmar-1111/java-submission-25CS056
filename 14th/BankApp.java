import java.util.Scanner;

public class BankApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AccountService service = new AccountService();
        boolean running = true;

        while (running) {
            System.out.println("1. Open account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. List accounts");
            System.out.println("5. Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> {
                        System.out.print("Account number: ");
                        String number = sc.nextLine().trim();
                        System.out.print("Owner name: ");
                        String owner = sc.nextLine().trim();
                        service.openAccount(number, owner, 0);
                        System.out.println("Account opened");
                    }
                    case "2" -> {
                        System.out.print("Account number: ");
                        Account account = service.findAccount(sc.nextLine().trim());
                        System.out.print("Amount: ");
                        account.deposit(Double.parseDouble(sc.nextLine().trim()));
                        System.out.println("Balance: " + account.getBalance());
                    }
                    case "3" -> {
                        System.out.print("Account number: ");
                        Account account = service.findAccount(sc.nextLine().trim());
                        System.out.print("Amount: ");
                        double amount = Double.parseDouble(sc.nextLine().trim());
                        if (account.withdraw(amount)) {
                            System.out.println("Balance: " + account.getBalance());
                        } else {
                            System.out.println("Withdrawal failed");
                        }
                    }
                    case "4" -> {
                        for (Account account : service.getAccounts()) {
                            System.out.println(account.getNumber() + " | "
                                    + account.getOwner() + " | " + account.getBalance());
                        }
                    }
                    case "5" -> running = false;
                    default -> System.out.println("Invalid choice");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        sc.close();
    }
}
