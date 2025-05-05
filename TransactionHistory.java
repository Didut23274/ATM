package service;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistory {
    private final List<String> history;

    public TransactionHistory() {
        this.history = new ArrayList<>();
    }

    public void add(String transaksi) {
        history.add(transaksi);
    }

    public void showHistory() {
        System.out.println("\n-- Riwayat Transaksi --");
        if (history.isEmpty()) {
            System.out.println("Belum ada transaksi.");
        } else {
            for (String h : history) {
                System.out.println(h);
            }
        }
    }
}
