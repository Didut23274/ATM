import model.Account;
import model.User;
import ui.ATMUI;

public class MainApp {
    public static void main(String[] args) {
        Account acc = new Account("123456", 1000000);
        User user = new User("Andi", "1234", acc);
        ATMUI ui = new ATMUI(user);
        ui.start();
    }
}
