package bootcamp.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BankServiceTest {
    private BankService bankService;
    private Client client1;
    private Client client2;
    private Client client3;

    @BeforeEach
    void setUp() {
        client1 = new Client("John", "Smith", "123");
        client2 = new Client("Dora", "Smith", "456");
        client3 = new Client("Albert", "Smith", "789");
        bankService = new BankService();
    }

    @Test
    void testOpenAccountAndGetAccountNumber_whenAccountNumberNotNull() {
        String accountNumber = bankService.openAccountAndGetAccountNumber(List.of(client1));
        assertNotNull(accountNumber);
        assertNotNull(bankService.getAccount(accountNumber));
    }

    @Test
    void testOpenAccountAndGetAccountNumber_whenAccountNumberIsGenerated() {
        String accountNumber1 = bankService.openAccountAndGetAccountNumber(List.of(client1));
        String accountNumber2 = bankService.openAccountAndGetAccountNumber(List.of(client2));

        assertEquals(accountNumber1, bankService.getAccount(accountNumber1).getAccountNumber());
        assertEquals(accountNumber2, bankService.getAccount(accountNumber2).getAccountNumber());
    }

    @Test
    void testTransferMoney() {
        String accountNumber1 = bankService.openAccountAndGetAccountNumber(List.of(client1));
        String accountNumber2 = bankService.openAccountAndGetAccountNumber(List.of(client2));

        Account account1 = bankService.getAccount(accountNumber1);
        Account account2 = bankService.getAccount(accountNumber2);
        account1.deposit(BigDecimal.valueOf(1000));
        account2.deposit(BigDecimal.valueOf(200));

        bankService.transferMoney(accountNumber1, accountNumber2, BigDecimal.valueOf(700));
        assertEquals(BigDecimal.valueOf(300), account1.getBalance());
        assertEquals(BigDecimal.valueOf(900), account2.getBalance());
    }

    @Test
    public void testTransferInvalidAccount() {
        bankService.openAccountAndGetAccountNumber(List.of(client1));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bankService.transferMoney("invalidAccount", "anotherInvalidAccount", BigDecimal.valueOf(100));
        });
        assertEquals("Eine oder beide Kontonummern sind ungültig.", exception.getMessage());
    }

    @Test
    public void testSplitAccountEvenly() {
        List<Client> clients = new ArrayList<>();
        clients.add(client1);
        clients.add(client2);
        String accountNumber = bankService.openAccountAndGetAccountNumber(clients);

        Account account = bankService.getAccount(accountNumber);
        account.deposit(BigDecimal.valueOf(3)); // 3 EUR einzahlen

        List<String> newAccountNumbers = bankService.split(accountNumber);

        assertEquals(2, newAccountNumbers.size());
        assertEquals(BigDecimal.valueOf(1.50).setScale(2, RoundingMode.HALF_UP), bankService.getAccount(newAccountNumbers.get(0)).getBalance());
        assertEquals(BigDecimal.valueOf(1.50).setScale(2, RoundingMode.HALF_UP), bankService.getAccount(newAccountNumbers.get(1)).getBalance());
    }

    @Test
    public void testSplitAccountWithThreeClients() {
        List<Client> clients = new ArrayList<>();
        clients.add(client1);
        clients.add(client2);
        clients.add(client3);
        String accountNumber = bankService.openAccountAndGetAccountNumber(clients);

        Account account = bankService.getAccount(accountNumber);
        account.deposit(BigDecimal.valueOf(5)); // 5 EUR einzahlen

        List<String> newAccountNumbers = bankService.split(accountNumber);

        assertEquals(3, newAccountNumbers.size());

        assertEquals(BigDecimal.valueOf(1.67), bankService.getAccount(newAccountNumbers.get(0)).getBalance());
        assertEquals(BigDecimal.valueOf(1.66), bankService.getAccount(newAccountNumbers.get(1)).getBalance());
        assertEquals(BigDecimal.valueOf(1.66), bankService.getAccount(newAccountNumbers.get(2)).getBalance());
    }

    @Test
    public void testSplitAccountNotEnoughClients() {
        List<Client> clients = new ArrayList<>();
        clients.add(client1);
        String accountNumber = bankService.openAccountAndGetAccountNumber(clients);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bankService.split(accountNumber);
        });
        assertEquals("Konto nicht gefunden oder nicht genügend Kunden zum Aufteilen.", exception.getMessage());
    }

    @Test
    public void testApplyInterest() {
        List<Client> clients = new ArrayList<>();
        clients.add(client1);
        String accountNumber = bankService.openAccountAndGetAccountNumber(clients);
        Account account = bankService.getAccount(accountNumber);
        account.deposit(BigDecimal.valueOf(1000)); // 1000 Euro einzahlen

        // Zinsen anwenden
        bankService.applyInterest(BigDecimal.valueOf(5)); // 5% Zinsen

        // Erwarteter Kontostand nach Zinsgutschrift
        BigDecimal expectedBalance = BigDecimal.valueOf(1000).add(BigDecimal.valueOf(50)); // 1000 + 50 (5% von 1000)
        assertEquals(expectedBalance.setScale(2, RoundingMode.HALF_UP), account.getBalance());
    }
}