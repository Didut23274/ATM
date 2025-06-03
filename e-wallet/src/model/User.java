package model;

public class User {
    private int id;
    private String username;
    private String password;
    private double saldo;

    public User(int id, String username, String password, double saldo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.saldo = saldo;
    }

    // Konstruktor untuk registrasi
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.saldo = 0;
    }

    // Getter dan Setter
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public double getSaldo() { return saldo; }

    public void setId(int id) { this.id = id; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
}