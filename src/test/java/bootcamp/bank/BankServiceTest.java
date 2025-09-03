package bootcamp.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class BankServiceTest {
    private BankService bankService;
    private Client client1;
    private Client client2;

    @BeforeEach
    void setUp() {
        client1 = new Client("John", "Smith", "123");
        client2 = new Client("Dora", "Smith", "456");
        bankService = new BankService();
    }

    @Test
    void testOpenAccountAndGetAccountNumber_whenAccountNumberNotNull() {
        String accountNumber = bankService.openAccountAndGetAccountNumber(client1);
        assertNotNull(accountNumber);
        assertNotNull(bankService.getAccount(accountNumber));
    }

    @Test
    void testOpenAccountAndGetAccountNumber_whenAccountNumberIsGenerated() {
        String accountNumber1 = bankService.openAccountAndGetAccountNumber(client1);
        String accountNumber2 = bankService.openAccountAndGetAccountNumber(client2);

        assertEquals(accountNumber1, bankService.getAccount(accountNumber1).getAccountNumber());
        assertEquals(accountNumber2, bankService.getAccount(accountNumber2).getAccountNumber());
    }

    @Test
    void testTransferMoney() {
        String accountNumber1 = bankService.openAccountAndGetAccountNumber(client1);
        String accountNumber2 = bankService.openAccountAndGetAccountNumber(client2);

        Account account1 = bankService.getAccount(accountNumber1);
        Account account2 = bankService.getAccount(accountNumber2);
        account1.deposit(BigDecimal.valueOf(1000));
        account2.deposit(BigDecimal.valueOf(200));

        bankService.transferMoney(accountNumber1, accountNumber2, BigDecimal.valueOf(700));
        assertEquals(BigDecimal.valueOf(2300), account1.getBalance());
        assertEquals(BigDecimal.valueOf(2900), account2.getBalance());
    }

    @Test
    public void testTransferInvalidAccount() {
        bankService.openAccountAndGetAccountNumber(client1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bankService.transferMoney("invalidAccount", "anotherInvalidAccount", BigDecimal.valueOf(100));
        });
        assertEquals("Eine oder beide Kontonummern sind ungültig.", exception.getMessage());
    }
}