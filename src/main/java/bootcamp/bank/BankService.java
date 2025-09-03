package bootcamp.bank;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BankService {
    private Map<String, Account> accounts = new HashMap<>();

    public String openAccountAndGetAccountNumber(Client client) {
        String accountNumber = this.generateAccountNumber();
        Account account = new Account(accountNumber, BigDecimal.valueOf(2000), client);
        this.accounts.put(accountNumber, account);
        return account.getAccountNumber();
    }

    public void transferMoney(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        Account fromAccount = this.accounts.get(fromAccountNumber);
        Account toAccount = this.accounts.get(toAccountNumber);

        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("Eine oder beide Kontonummern sind ungültig.");
        }

        fromAccount.withdraw(amount); // abheben
        toAccount.deposit(amount); // einzahlen
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString();
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }
}
