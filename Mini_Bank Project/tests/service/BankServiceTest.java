package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import exception.InsufficientFundsException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import model.Account;
import model.SavingsAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class BankServiceTest {
    @TempDir
    Path tempFolder;

    @Test
    void depositAddsToBalance() throws Exception {
        BankService bank = new BankService(tempFolder);
        try {
            Account account = bank.openAccount(
                    "Riya", "riya@example.com", "9876543210", "savings", 5000);
            bank.deposit(account.getAccountNumber(), 2000);
            assertEquals(7000, account.getBalance());
        } finally {
            bank.stop();
        }
    }

    @Test
    void withdrawalOverAvailableBalanceThrows() throws Exception {
        BankService bank = new BankService(tempFolder);
        try {
            Account account = bank.openAccount(
                    "Riya", "riya@example.com", "9876543210", "savings", 5000);
            assertThrows(InsufficientFundsException.class,
                    () -> bank.withdraw(account.getAccountNumber(), 5000));
            assertEquals(5000, account.getBalance());
        } finally {
            bank.stop();
        }
    }

    @Test
    void concurrentDepositsKeepTheCorrectBalance() throws Exception {
        BankService bank = new BankService(tempFolder);
        try {
            Account account = bank.openAccount(
                    "Riya", "riya@example.com", "9876543210", "savings", 1000);
            int threadCount = 10;
            int times = 1000;
            CountDownLatch done = new CountDownLatch(threadCount);
            List<Runnable> tasks = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                tasks.add(() -> {
                    try {
                        new AccountWorker(account, times, 1).run();
                    } finally {
                        done.countDown();
                    }
                });
            }

            bank.processBatch(tasks);
            done.await();
            assertEquals(1000 + (long) threadCount * times, account.getBalance());
        } finally {
            bank.stop();
        }
    }

    @Test
    void savedAccountsCanBeLoadedAgain() throws Exception {
        BankService bank = new BankService(tempFolder);
        String accountNumber;
        try {
            Account account = bank.openAccount(
                    "Riya", "riya@example.com", "9876543210", "savings", 5000);
            accountNumber = account.getAccountNumber();
            bank.saveAccounts();
        } finally {
            bank.stop();
        }

        BankService loadedBank = new BankService(tempFolder);
        try {
            assertEquals(accountNumber,
                    loadedBank.listAccounts().get(0).getAccountNumber());
        } finally {
            loadedBank.stop();
        }
    }
}
