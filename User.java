package model;

public class User {
    private String name;
    private String pin;
    private Account account;

    public User(String name, String pin, Account account) {
        this.name = name;
        this.pin = pin;
        this.account = account;
    }

    public boolean validatePin(String inputPin) {
        return this.pin.equals(inputPin);
    }

    public Account getAccount() {
        return account;
    }

    public String getName() {
        return name;
    }
}
