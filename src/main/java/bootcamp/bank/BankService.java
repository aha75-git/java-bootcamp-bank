package bootcamp.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class BankService {
    private Map<String, Account> accounts = new HashMap<>();

    public String openAccountAndGetAccountNumber(List<Client> clients) {
        String accountNumber = this.generateAccountNumber();
        Account account = new Account(accountNumber, BigDecimal.ZERO, clients);
        this.accounts.put(accountNumber, account);
        return accountNumber;
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

    public List<String> split(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account == null || account.getClients().size() < 2) {
            throw new IllegalArgumentException("Konto nicht gefunden oder nicht genügend Kunden zum Aufteilen.");
        }

        BigDecimal totalBalance = account.getBalance();
        int numberOfClients = account.getClients().size();
        BigDecimal splitAmount = totalBalance.divide(BigDecimal.valueOf(numberOfClients), 2, RoundingMode.DOWN);
        BigDecimal remainder = totalBalance.subtract(splitAmount.multiply(BigDecimal.valueOf(numberOfClients)));

        List<String> newAccountNumbers = new ArrayList<>();
        for (Client client : account.getClients()) {
            String newAccountNumber = this.openAccountAndGetAccountNumber(List.of(client));
            Account newAccount = accounts.get(newAccountNumber);
            newAccount.deposit(splitAmount);
            if (remainder.compareTo(BigDecimal.ZERO) > 0) {
                newAccount.deposit(new BigDecimal("0.01")); // Verteile den Restbetrag
                remainder = remainder.subtract(BigDecimal.ONE);
            }
            newAccountNumbers.add(newAccountNumber);
        }

        // Lösche das alte Konto
        accounts.remove(accountNumber);
        return newAccountNumbers;
    }

    public void applyInterest(BigDecimal interestRate) {
        for (Account account : accounts.values()) {
            BigDecimal interest = account.getBalance().multiply(interestRate).divide(BigDecimal.valueOf(100),  2, RoundingMode.DOWN);
            account.deposit(interest);
        }
    }
}
