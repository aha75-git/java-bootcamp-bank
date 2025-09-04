package bootcamp.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    private Account account;

    @BeforeEach
    void setUp() {
        Client client = new Client("Andreas", "Mustermann", "12233");
        account = new Account("A12345", BigDecimal.valueOf(2000), List.of(client));
    }

    @Test
    void testDeposit() {
        account.deposit(BigDecimal.valueOf(500));
        assertEquals(BigDecimal.valueOf(2500), account.getBalance());
    }

    @Test
    void testWithdraw() {
        account.withdraw(BigDecimal.valueOf(750));
        assertEquals(BigDecimal.valueOf(1250), account.getBalance());
    }

    @Test
    public void testWithdrawInvalidAmount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            account.withdraw(BigDecimal.valueOf(2500)); // Übersteigt den Kontostand
        });
        assertEquals("Ungültiger Auszahlungsbetrag.", exception.getMessage());
    }

    @Test
    public void testDepositNegativeAmount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            account.deposit(BigDecimal.valueOf(-100)); // Negativer Betrag
        });
        assertEquals("Der Einzahlungsbetrag muss positiv sein.", exception.getMessage());
    }
}