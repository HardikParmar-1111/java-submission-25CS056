import java.util.Scanner;


class firstp {
    enum Coin { ONE, TWO, THREE, FOUR, FIVE }
    public static void main(String[] args) {
        Coin c = Coin.ONE;
        Scanner sc = new Scanner(System.in);
        
        final int PRICE = 10;
        int total = 0;

        while (total < PRICE){
            System.out.println("Enter coins: ");
            String input = sc.nextLine();
            try {
                c = Coin.valueOf(input.toUpperCase());
        }
        catch(Exception e){
            System.out.println("Invalid coin.");
            continue;
        }
        int value = 0;
        switch(c){
            case ONE:
                value = 1;
                break;
            case TWO:
                value = 2;
                break;
            case THREE:
                value = 3;
                break;
            case FOUR:
                value = 4;
                break;
            case FIVE:
                value = 5;
                break;
    }
    total += value;
    System.out.println("Total: " + total);
    }
    
}
}
