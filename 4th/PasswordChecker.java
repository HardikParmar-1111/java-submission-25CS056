import java.util.Scanner;

public class PasswordChecker {

    public static boolean hasLength(String password) {
        return password.length() >= 8;
    }
    
    public static boolean hasUpperCase(String password) {
       return password.matches(".*[A-Z].*");
    }

    public static boolean hasDigit(String password) {
        return password.matches(".*[0-9].*");
    }

    public static boolean hasSpecialChar(String password) {
        return password.matches(".*[@#$*()].*");
    }

    public static String getStrength(String password) {
        int count = 0;
        if (hasLength(password)) {
            count++;
        }
        if (hasUpperCase(password)) {
            count++;
        }
        if (hasDigit(password)) {
            count++;
        }
        if (hasSpecialChar(password)) {
            count++;
        }
        String st = switch(count){
            case 0,1 -> "weak";
            case 2,3 -> "medium";
            case 4 -> "strong";
            default -> "invalid";
        };
        return st;
    }
}

class Main {
    public static void main (String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Password");
        String password = sc.nextLine();
        System.out.println("Password Strength: " + PasswordChecker.getStrength(password));
    }
}
