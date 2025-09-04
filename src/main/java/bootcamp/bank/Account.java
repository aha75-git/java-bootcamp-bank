package bootcamp.bank;

import java.math.BigDecimal;
import java.util.List;

public class Account {
    private String accountNumber;
    private BigDecimal balance;
    private List<Client> clients;

    public Account(String accountNumber, BigDecimal balance, List<Client> clients) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.clients = clients;
    }

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            this.balance = this.balance.add(amount);
            System.out.println("Eingezahlt: " + amount + ", neuer Kontostand: " + this.balance);
        } else {
            throw new IllegalArgumentException("Der Einzahlungsbetrag muss positiv sein.");
        }
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(balance) <= 0) {
            this.balance = this.balance.subtract(amount);
            System.out.println("Auszahlung: " + amount + ", neuer Kontostand: " + this.balance);
        } else {
            throw new IllegalArgumentException("Ungültiger Auszahlungsbetrag.");
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
}
