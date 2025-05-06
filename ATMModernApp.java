package ui;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ATMModernApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Map<String, String> users = new HashMap<>();
    private String currentUser;
    private double balance = 1000000;

    public ATMModernApp() {
        setTitle("ATM FlatLaf Modern");
        setSize(420, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadUsers();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.add(loginPanel(), "LOGIN");
        mainPanel.add(menuPanel(), "MENU");
        mainPanel.add(adminPanel(), "ADMIN");

        add(mainPanel);
        cardLayout.show(mainPanel, "LOGIN");
    }

    private void loadUsers() {
        users.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(",");
                if (split.length == 2) users.put(split[0].trim(), split[1].trim());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal membaca users.txt", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveUser(String username, String pin) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
            writer.write(username + "," + pin);
            writer.newLine();
            users.put(username, pin);
            JOptionPane.showMessageDialog(this, "User baru ditambahkan.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan user.");
        }
    }

    private JPanel loginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("ATM MODERN", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JTextField usernameField = new JTextField();
        JPasswordField pinField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pin = pinField.getText();

            if (user.equals("admin") && pin.equals("admin123")) {
                cardLayout.show(mainPanel, "ADMIN");
            } else if (users.containsKey(user) && users.get(user).equals(pin)) {
                currentUser = user;
                balance = 1000000; // default saldo simulasi
                cardLayout.show(mainPanel, "MENU");
            } else {
                JOptionPane.showMessageDialog(this, "Login gagal!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel form = new JPanel(new GridLayout(4, 1, 10, 10));
        form.add(new JLabel("Username:"));
        form.add(usernameField);
        form.add(new JLabel("PIN:"));
        form.add(pinField);

        panel.add(title, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(loginButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel adminPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel label = new JLabel("Tambah User Baru", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));

        JTextField newUserField = new JTextField();
        JTextField newPinField = new JTextField();

        JButton addButton = new JButton("Tambah");
        addButton.addActionListener(e -> {
            String newUser = newUserField.getText().trim();
            String newPin = newPinField.getText().trim();

            if (!newUser.isEmpty() && !newPin.isEmpty()) {
                if (users.containsKey(newUser)) {
                    JOptionPane.showMessageDialog(this, "User sudah ada.");
                } else {
                    saveUser(newUser, newPin);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Input tidak boleh kosong.");
            }
        });

        JButton backButton = new JButton("Kembali ke Login");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));

        JPanel form = new JPanel(new GridLayout(4, 1, 10, 10));
        form.add(new JLabel("Username Baru:"));
        form.add(newUserField);
        form.add(new JLabel("PIN Baru:"));
        form.add(newPinField);

        JPanel btns = new JPanel(new GridLayout(1, 2, 10, 10));
        btns.add(addButton);
        btns.add(backButton);

        panel.add(label, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(btns, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel menuPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel header = new JLabel("Menu Utama", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));

        JButton cekSaldo = new JButton("💰 Cek Saldo");
        JButton setor = new JButton("📥 Setor Tunai");
        JButton tarik = new JButton("📤 Tarik Tunai");
        JButton cetak = new JButton("🧾 Cetak Struk");
        JButton keluar = new JButton("🚪 Keluar");

        cekSaldo.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Saldo Anda: Rp" + String.format("%,.0f", balance))
        );

        setor.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Masukkan jumlah setor:");
            try {
                double amount = Double.parseDouble(input);
                if (amount > 0) {
                    balance += amount;
                    JOptionPane.showMessageDialog(this, "Setor berhasil. Saldo: Rp" + String.format("%,.0f", balance));
                }
            } catch (Exception ignored) {
                JOptionPane.showMessageDialog(this, "Input tidak valid.");
            }
        });

        tarik.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Masukkan jumlah tarik:");
            try {
                double amount = Double.parseDouble(input);
                if (amount > 0 && amount <= balance) {
                    balance -= amount;
                    JOptionPane.showMessageDialog(this, "Tarik berhasil. Saldo: Rp" + String.format("%,.0f", balance));
                } else {
                    JOptionPane.showMessageDialog(this, "Saldo tidak cukup.");
                }
            } catch (Exception ignored) {
                JOptionPane.showMessageDialog(this, "Input tidak valid.");
            }
        });

        cetak.addActionListener(e -> {
            StringBuilder struk = new StringBuilder();
            struk.append("=== STRUK TRANSAKSI ATM ===\n");
            struk.append("User: ").append(currentUser).append("\n");
            struk.append("Saldo saat ini: Rp").append(String.format("%,.0f", balance)).append("\n");
            struk.append("Terima kasih telah menggunakan ATM Modern.");

            JTextArea textArea = new JTextArea(struk.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(350, 150));

            JOptionPane.showMessageDialog(this, scrollPane, "Struk Digital", JOptionPane.INFORMATION_MESSAGE);
        });


        keluar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Anda telah logout.");
            cardLayout.show(mainPanel, "LOGIN");
        });

        panel.add(header);
        panel.add(cekSaldo);
        panel.add(setor);
        panel.add(tarik);
        panel.add(cetak);
        panel.add(keluar);

        return panel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Gagal memuat tema.");
        }

        SwingUtilities.invokeLater(() -> {
            ATMModernApp app = new ATMModernApp();
            app.setVisible(true);
        });
    }
}
