import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModernATM extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private double balance = 1000000;
    private final String correctPIN = "1234";

    public ModernATM() {
        setTitle("ATM Modern");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createPinPanel(), "PIN");
        mainPanel.add(createMainMenu(), "MENU");

        add(mainPanel);
        cardLayout.show(mainPanel, "PIN");
    }

    private JPanel createPinPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Tambahkan logo ATM
        JLabel logo = new JLabel();
        logo.setHorizontalAlignment(JLabel.CENTER);
        try {
            ImageIcon icon = new ImageIcon("atm.png"); // Ganti path jika berbeda
            Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            logo.setText("ATM Logo");
        }

        // Bagian input PIN
        JPanel inputPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        inputPanel.setBackground(Color.WHITE);
        JLabel label = new JLabel("Masukkan PIN Anda:", SwingConstants.CENTER);
        JPasswordField pinField = new JPasswordField();
        JButton loginButton = new JButton("Masuk");

        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.WHITE);

        loginButton.addActionListener(e -> {
            if (pinField.getText().equals(correctPIN)) {
                cardLayout.show(mainPanel, "MENU");
            } else {
                JOptionPane.showMessageDialog(this, "PIN Salah!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        inputPanel.add(label);
        inputPanel.add(pinField);
        inputPanel.add(loginButton);

        panel.add(logo, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMainMenu() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setBackground(Color.WHITE);

        JLabel header = new JLabel("Menu Utama", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));

        JButton cekSaldo = new JButton("💰 Cek Saldo");
        JButton setor = new JButton("📥 Setor Tunai");
        JButton tarik = new JButton("📤 Tarik Tunai");
        JButton keluar = new JButton("🚪 Keluar");

        for (JButton btn : new JButton[]{cekSaldo, setor, tarik, keluar}) {
            btn.setBackground(new Color(0, 123, 255));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
        }

        cekSaldo.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Saldo Anda: Rp" + String.format("%,.0f", balance))
        );

        setor.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Jumlah setor:");
            try {
                double amount = Double.parseDouble(input);
                if (amount > 0) {
                    balance += amount;
                    JOptionPane.showMessageDialog(this, "Setor berhasil! Saldo: Rp" + String.format("%,.0f", balance));
                } else {
                    JOptionPane.showMessageDialog(this, "Jumlah tidak valid.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Input tidak valid.");
            }
        });

        tarik.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Jumlah tarik:");
            try {
                double amount = Double.parseDouble(input);
                if (amount > 0 && amount <= balance) {
                    balance -= amount;
                    JOptionPane.showMessageDialog(this, "Tarik berhasil! Saldo: Rp" + String.format("%,.0f", balance));
                } else {
                    JOptionPane.showMessageDialog(this, "Saldo tidak cukup.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Input tidak valid.");
            }
        });

        keluar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Anda telah keluar.");
            cardLayout.show(mainPanel, "PIN");
        });

        panel.add(header);
        panel.add(cekSaldo);
        panel.add(setor);
        panel.add(tarik);
        panel.add(keluar);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ModernATM atm = new ModernATM();
            atm.setVisible(true);
        });
    }
}
