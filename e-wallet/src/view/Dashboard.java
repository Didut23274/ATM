
package view;

import config.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Dashboard extends JFrame {
    private int userId;
    private JLabel saldoLabel;
    private JButton topupButton, tarikButton, cekSaldoButton, riwayatButton, logoutButton;

    public Dashboard(int userId) {
        this.userId = userId;

        setTitle("E-Wallet Dashboard");
        setSize(480, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 250, 255)); // Biru muda sangat terang

        // ==== Header dengan logo atau fallback ====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(mainPanel.getBackground());

        JLabel logoLabel = new JLabel();
        java.net.URL logoURL = getClass().getResource("/resources/logo.png");

        if (logoURL != null) {
            ImageIcon originalIcon = new ImageIcon(logoURL);
            Image scaledImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            logoLabel.setText("E-Wallet");
            logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            logoLabel.setForeground(Color.DARK_GRAY);
        }
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(logoLabel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ==== Tombol aksi ====
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 12, 12));
        buttonPanel.setBackground(mainPanel.getBackground());

        topupButton = createButton("Top-up", new Color(32, 145, 236));
        tarikButton = createButton("Tarik Saldo", new Color(32, 146, 237));
        cekSaldoButton = createButton("Cek Saldo", new Color(33, 150, 243));
        riwayatButton = createButton("Riwayat Transaksi", new Color(26, 218, 27));
        logoutButton = createButton("Logout", new Color(255, 87, 87));

        buttonPanel.add(topupButton);
        buttonPanel.add(tarikButton);
        buttonPanel.add(cekSaldoButton);
        buttonPanel.add(riwayatButton);
        buttonPanel.add(logoutButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);

        // ==== Event Listeners ====
        topupButton.addActionListener(this::handleTopup);
        tarikButton.addActionListener(this::handleTarik);
        cekSaldoButton.addActionListener(e -> showSaldoPopup());
        riwayatButton.addActionListener(e -> showRiwayat());
        logoutButton.addActionListener(e -> handleLogout());

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
            }
        });

        refreshSaldoLabel();
    }

    private JButton createButton(String text, Color baseColor) {
        return createButton(text, baseColor, baseColor.darker());
    }

    private JButton createButton(String text, Color baseColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        setHoverEffect(button, baseColor, hoverColor);
        return button;
    }

    private void setHoverEffect(JButton button, Color baseColor, Color hoverColor) {
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            public void mousePressed(MouseEvent e) {
                button.setLocation(button.getX(), button.getY() + 2);
            }

            public void mouseReleased(MouseEvent e) {
                button.setLocation(button.getX(), button.getY() - 2);
            }
        });
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new MainMenu().setVisible(true);
        }
    }



    private void refreshSaldoLabel() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT saldo FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                saldoLabel = new JLabel("Saldo: Rp " + String.format("%,.2f", rs.getDouble("saldo")));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat saldo: " + e.getMessage());
        }
    }

    private void showSaldoPopup() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT saldo FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double saldo = rs.getDouble("saldo");
                JOptionPane.showMessageDialog(this, "Saldo Anda saat ini adalah: Rp " + String.format("%,.2f", saldo));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mengambil saldo: " + e.getMessage());
        }
    }

    private void handleTopup(ActionEvent e) {
        String input = JOptionPane.showInputDialog(this, "Masukkan jumlah top-up:");
        if (input != null) {
            try {
                double amount = Double.parseDouble(input);
                updateSaldoDatabase(amount, "TOPUP");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Input tidak valid.");
            }
        }
    }

    private void handleTarik(ActionEvent e) {
        String input = JOptionPane.showInputDialog(this, "Masukkan jumlah penarikan:");
        if (input != null) {
            try {
                double amount = Double.parseDouble(input);
                if (!hasSaldoCukup(amount)) {
                    JOptionPane.showMessageDialog(this, "Saldo tidak cukup!");
                    return;
                }
                updateSaldoDatabase(-amount, "WITHDRAW");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage());
            }
        }
    }

    private boolean hasSaldoCukup(double amount) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT saldo FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getDouble("saldo") >= amount;
        }
    }

    private void updateSaldoDatabase(double amount, String type) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            String updateSaldo = "UPDATE users SET saldo = saldo + ? WHERE id = ?";
            PreparedStatement stmt1 = conn.prepareStatement(updateSaldo);
            stmt1.setDouble(1, amount);
            stmt1.setInt(2, userId);
            stmt1.executeUpdate();

            String insertTransaksi = "INSERT INTO transactions (user_id, type, amount) VALUES (?, ?, ?)";
            PreparedStatement stmt2 = conn.prepareStatement(insertTransaksi);
            stmt2.setInt(1, userId);
            stmt2.setString(2, type);
            stmt2.setDouble(3, Math.abs(amount));
            stmt2.executeUpdate();

            conn.commit();
            JOptionPane.showMessageDialog(this, "Transaksi berhasil.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal transaksi: " + e.getMessage());
        }
    }

    private void showRiwayat() {
        JDialog filterDialog = new JDialog(this, "Filter Riwayat Transaksi", true);
        filterDialog.setSize(400, 200);
        filterDialog.setLocationRelativeTo(this);
        filterDialog.setLayout(new BorderLayout(10, 10));
        filterDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(new EmptyBorder(20, 20, 0, 20));

        JLabel fromLabel = new JLabel("Dari (THN-BLN-TGL):");
        JLabel toLabel = new JLabel("Sampai (THN-BLN-TGL):");

        JTextField fromField = new JTextField();
        JTextField toField = new JTextField();

        inputPanel.add(fromLabel);
        inputPanel.add(fromField);
        inputPanel.add(toLabel);
        inputPanel.add(toField);

        JButton submitButton = new JButton("Tampilkan Riwayat");
        submitButton.setBackground(new Color(33, 150, 243));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);

        submitButton.addActionListener(e -> {
            String fromDate = fromField.getText().trim();
            String toDate = toField.getText().trim();
            if (fromDate.isEmpty() || toDate.isEmpty()) {
                JOptionPane.showMessageDialog(filterDialog, "Harap isi kedua tanggal!");
                return;
            }
            filterDialog.dispose();
            showRiwayatFiltered(fromDate, toDate);
        });

        filterDialog.add(inputPanel, BorderLayout.CENTER);
        filterDialog.add(submitButton, BorderLayout.SOUTH);
        filterDialog.setVisible(true);
    }

    private void showRiwayatFiltered(String fromDate, String toDate) {
        JFrame riwayatFrame = new JFrame("Riwayat Transaksi dari " + fromDate + " sampai " + toDate);
        riwayatFrame.setSize(500, 350);
        riwayatFrame.setLocationRelativeTo(this);

        DefaultTableModel model = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        model.addColumn("Tipe");
        model.addColumn("Jumlah");
        model.addColumn("Waktu");

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(0, 102, 153));
        header.setForeground(Color.WHITE);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(245, 250, 255) : Color.WHITE);
                } else {
                    c.setBackground(new Color(204, 229, 255));
                }
                return c;
            }
        });

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT type, amount, timestamp FROM transactions WHERE user_id = ? AND DATE(timestamp) BETWEEN ? AND ? ORDER BY timestamp DESC";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setString(2, fromDate);
            stmt.setString(3, toDate);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("type"),
                        "Rp " + String.format("%,.2f", rs.getDouble("amount")),
                        rs.getTimestamp("timestamp").toString()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mengambil riwayat: " + e.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        riwayatFrame.add(scrollPane);
        riwayatFrame.setVisible(true);
    }
}