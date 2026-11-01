import model.Command;
import util.CommandParser;
import util.Validator;

public class Practical04Demo {
    public static void main(String[] args) {
        System.out.println("Valid mobile: "
                + Validator.isValidMobile("9876543210"));
        System.out.println("Invalid mobile: "
                + Validator.isValidMobile("12345"));
        System.out.println("Valid email: "
                + Validator.isValidEmail("riya@example.com"));
        System.out.println("Invalid email: "
                + Validator.isValidEmail("riya@"));
        System.out.println("Valid PAN: "
                + Validator.isValidPan("ABCDE1234F"));
        System.out.println("Invalid PAN: "
                + Validator.isValidPan("1234"));
        System.out.println("Valid IFSC: "
                + Validator.isValidIfsc("ABCD0123456"));
        System.out.println("Invalid IFSC: "
                + Validator.isValidIfsc("BAD"));

        Command command = CommandParser.parse("DEPOSIT AC0001 500");
        System.out.println(command.type());
        System.out.println(command.accountNumber());
        System.out.println(command.amount());
    }
}
