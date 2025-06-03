package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainMenu extends JFrame {
    public MainMenu() {
        setTitle("Selamat Datang di E-Wallet");
        setSize(400, 350); // ukuran ditambah untuk ruang logo
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Warna dan font
        Color primaryColor = new Color(0, 123, 255);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 16);

        // Panel Utama
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Logo
        JLabel logoLabel = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/logo.png")); // sesuaikan path
        Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        logoLabel.setIcon(new ImageIcon(scaledImage));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Judul / Header
        JLabel titleLabel = new JLabel("Dompet Digital", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(primaryColor);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Tombol login dan daftar
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Daftar");

        JButton[] buttons = {loginButton, registerButton};
        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setBackground(primaryColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
        }

        loginButton.addActionListener(this::openLogin);
        registerButton.addActionListener(this::openRegister);

        // Panel tombol
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 20, 60));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Tambahkan ke panel utama
        mainPanel.add(logoLabel, BorderLayout.NORTH);
        mainPanel.add(titleLabel, BorderLayout.CENTER);

        // Tambahkan ke frame
        add(mainPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    private void openLogin(ActionEvent e) {
        this.dispose();
        new LoginForm().setVisible(true);
    }

    private void openRegister(ActionEvent e) {
        this.dispose();
        new RegisterForm().setVisible(true);
    }

    public void setVisible(boolean b) {
    }
}