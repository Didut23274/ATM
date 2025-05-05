package service;

import model.Account;

public class AccountService {
    private final Account account;

    public AccountService(Account account) {
        this.account = account;
    }

    public boolean tarikTunai(double jumlah) {
        return account.withdraw(jumlah);
    }

    public void setorTunai(double jumlah) {
        account.deposit(jumlah);
    }

    public double getSaldo() {
        return account.getBalance();
    }
}
