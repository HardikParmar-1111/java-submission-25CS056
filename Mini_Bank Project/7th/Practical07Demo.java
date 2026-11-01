import model.SavingsAccount;
import model.annotation.AnnotationValidator;

public class Practical07Demo {
    public static void main(String[] args) throws IllegalAccessException {
        SavingsAccount account = new SavingsAccount("Riya", -100, 1000);
        String[] errors = AnnotationValidator.validate(account);
        for (String error : errors) {
            System.out.println(error);
        }
    }
}
