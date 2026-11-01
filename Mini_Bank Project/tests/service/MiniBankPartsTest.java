package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import model.Account;
import model.CurrentAccount;
import model.Customer;
import model.SavingsAccount;
import model.annotation.AnnotationValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import repository.Repository;
import util.CommandParser;
import util.Validator;

class MiniBankPartsTest {
    @TempDir
    Path tempFolder;

    @Test
    void validatorAndCommandParserWork() {
        assertTrue(Validator.isValidMobile("9876543210"));
        assertFalse(Validator.isValidMobile("12345"));
        assertTrue(Validator.isValidEmail("riya@example.com"));
        assertEquals("AC0001",
                CommandParser.parse("DEPOSIT AC0001 500").accountNumber());
    }

    @Test
    void repositoryFindsAccountByAnnotatedId() {
        Repository<Account> repository = new Repository<>();
        Account account = new CurrentAccount("Riya", 5000, 1000);
        repository.save(account);

        assertEquals(account, repository.findById(account.getAccountNumber()));
        assertEquals(1, repository.findAll().size());
        assertTrue(repository.delete(account.getAccountNumber()));
    }

    @Test
    void annotationsReportNegativeBalance() throws Exception {
        Account account = new SavingsAccount("Riya", -100, 1000);
        String[] errors = AnnotationValidator.validate(account);

        assertEquals(1, errors.length);
        assertTrue(errors[0].contains("must be > 0"));
    }

    @Test
    void transferKeepsBalancesCorrect() throws Exception {
        BankService bank = new BankService(tempFolder);
        try {
            Account from = bank.openAccount(
                    "Riya", "riya@example.com", "9876543210", "current", 5000);
            Account to = bank.openAccount(
                    "Jay", "jay@example.com", "9876543211", "current", 1000);
            bank.transfer(from.getAccountNumber(), to.getAccountNumber(), 500);

            assertEquals(4500, from.getBalance());
            assertEquals(1500, to.getBalance());
        } finally {
            bank.stop();
        }
    }

    @Test
    void customerCloneKeepsItsValues() {
        Customer customer = new Customer("Riya", "riya@example.com", "9876543210",
                new Customer.Address("12 River Road", "Anand", "388001"));
        Customer copy = customer.clone();

        assertTrue(customer != copy);
        assertEquals(customer.getCustomerId(), copy.getCustomerId());
        assertEquals(customer.getAddress().getCity(), copy.getAddress().getCity());
    }

    @Test
    void reportAddsDepositAndWithdrawalLogs() throws Exception {
        BankService bank = new BankService(tempFolder);
        try {
            Account account = bank.openAccount(
                    "Riya", "riya@example.com", "9876543210", "current", 5000);
            bank.deposit(account.getAccountNumber(), 1000);
            bank.withdraw(account.getAccountNumber(), 500);

            String report = bank.generateReport();
            assertTrue(report.contains("Deposits: 1000"));
            assertTrue(report.contains("Withdrawals: 500"));
            assertTrue(report.contains("Net change: 500"));
        } finally {
            bank.stop();
        }
    }
}
