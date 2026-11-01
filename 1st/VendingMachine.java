import java.util.Scanner;

enum Coin {
    ONE, TWO, FIVE, TEN
}

public class VendingMachine {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int price = 15;
        int total = 0;

        while (total < price) {
            System.out.println("Enter coin name:");
            String input = sc.nextLine().trim().toUpperCase();
            Coin coin;

            try {
                coin = Coin.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid coin.");
                continue;
            }

            int value = switch (coin) {
                case ONE -> 1;
                case TWO -> 2;
                case FIVE -> 5;
                case TEN -> 10;
            };

            total += value;
            System.out.println("Total: " + total);
        }

        System.out.println("Paid. Change: " + (total - price));
        sc.close();
    }
}
