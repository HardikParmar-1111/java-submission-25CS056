import java.util.Scanner;

class DivideByZeroException extends Exception {
    public DivideByZeroException(String message) {
        super(message);
    }
}

public class calculator {

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int attempts = 0;
        boolean success = false;

        double n1 = 0;
        double n2 = 0;
        String op = "";
        double result = 0;

        while(!success){
            attempts++;
        
        try{
        n1 = Double.parseDouble(sc.nextLine().trim());
        n2 = Double.parseDouble(sc.nextLine().trim());
        op = sc.nextLine().trim();
        if(n2==0 && op.equals("/")){
            throw new DivideByZeroException("Cannot divide by zero.");
        }
        switch(op){
            case "+":
                result = n1 + n2;
                break;
            case "-":
                result = n1 - n2;
                break;
            case "*":
                result = n1 * n2;
                break;
            case "/":
                result = n1 / n2;
                break;
            default:
                System.out.println("Invalid operator");
                continue;
        }
        success = true;
         System.out.println("Result: " + result);
    }
    catch(NumberFormatException e){
            System.out.println("Invalid number format");
        }
        catch(DivideByZeroException e){
            System.out.println(e.getMessage());
        }
        finally{
           
            System.out.println("Attempt: " + attempts);
        }
    }
        
    }
}
