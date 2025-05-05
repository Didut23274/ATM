package ui;

import java.util.Scanner;
import model.User;
import service.AccountService;
import service.TransactionHistory;

public class ATMUI {
    private final Scanner scanner = new Scanner(System.in);
    private final User user;
    private final AccountService accountService;
    private final TransactionHistory history;

    public ATMUI(User user) {
        this.user = user;
        this.accountService = new AccountService(user.getAccount());
        this.history = new TransactionHistory();
    }

    public void start() {
        System.out.print("Masukkan PIN: ");
        String inputPin = scanner.nextLine();
        if (!user.validatePin(inputPin)) {
            System.out.println("PIN salah.");
            return;
        }

        int pilihan;
        do {
            showMenu();
            pilihan = scanner.nextInt();
            scanner.nextLine();
            handleMenu(pilihan);
        } while (pilihan != 0);
    }

    private void showMenu() {
        System.out.println("\n--- ATM Menu ---");
        System.out.println("1. Cek Saldo");
        System.out.println("2. Tarik Tunai");
        System.out.println("3. Setor Tunai");
        System.out.println("4. Lihat Riwayat Transaksi");
        System.out.println("0. Keluar");
        System.out.print("Pilih menu: ");
    }

    private void handleMenu(int pilihan) {
        switch (pilihan) {
            case 1 -> {
                double saldo = accountService.getSaldo();
                System.out.println("Saldo Anda: Rp " + saldo);
                history.add("Cek Saldo: Rp " + saldo);
            }
            case 2 -> {
                System.out.print("Masukkan jumlah tarik tunai: ");
                double nominal = scanner.nextDouble();
                if (accountService.tarikTunai(nominal)) {
                    System.out.println("Tarik tunai berhasil.");
                    history.add("Tarik Tunai: Rp " + nominal);
                } else {
                    System.out.println("Saldo tidak cukup.");
                }
            }
            case 3 -> {
                System.out.print("Masukkan jumlah setor tunai: ");
                double nominal = scanner.nextDouble();
                accountService.setorTunai(nominal);
                System.out.println("Setor tunai berhasil.");
                history.add("Setor Tunai: Rp " + nominal);
            }
            case 4 -> history.showHistory();
            case 0 -> System.out.println("Terima kasih telah menggunakan ATM.");
            default -> System.out.println("Pilihan tidak valid.");
        }
    }
}
