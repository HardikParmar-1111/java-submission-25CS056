import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import model.Account;
import model.Customer;
import model.SavingsAccount;

public class Practical03Demo {
    public static void main(String[] args) throws Exception {
        Account account = new SavingsAccount("Riya", 4000, 1000);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(account);
        }

        Account copy;
        try (ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(bytes.toByteArray()))) {
            copy = (Account) in.readObject();
        }

        Customer.Address address =
                new Customer.Address("12 River Road", "Anand", "388001");
        Customer customer = new Customer("Riya", "riya@example.com",
                "9876543210", address);
        Customer customerCopy = customer.clone();

        System.out.println(account);
        System.out.println("Equal accounts: " + account.equals(copy));
        System.out.println("Address city: " + customer.getAddress().getCity());
        System.out.println("Customer cloned: " + (customer != customerCopy));
        System.out.println("Account object: " + (account instanceof Account));
    }
}
