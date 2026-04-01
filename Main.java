import constance.Role;
import model.User;
import service.impl.*;
import controller.ChefMenu;
import controller.CustomerMenu;
import controller.MainMenu;
import controller.ManagerMenu;
import util.CliUi;
import util.InputValidator;

import java.util.Scanner;

public class Main {
    static CustomerMenu customermenu = new CustomerMenu();
    static MainMenu mainmenu = new MainMenu();
    static ManagerMenu managermenu = new ManagerMenu();
    static ChefMenu chefmenu = new ChefMenu();

    private static final Scanner input = new Scanner(System.in);
    private static final AuthService authService = new AuthService();
    public static void main(String[] args) {

        int choice;
        while (true) {
            mainmenu.menu();
            Integer selected = InputValidator.readInt(input, "Select option: ");
            if (selected == null) {
                CliUi.error("Invalid input! Please enter a valid number.");
                continue;
            }
            choice = selected;


            switch (choice) {
                case 1 -> {
                    CliUi.printSection("CUSTOMER REGISTER");
                    String name = InputValidator.readNonBlank(input, "UserName: ");
                    String pass = InputValidator.readNonBlank(input, "Password: ");
                    String pass1 = InputValidator.readNonBlank(input, "Confirm Password: ");

                    if (name == null || pass == null || pass1 == null) {
                        CliUi.error("Username/password must not be empty.");
                        break;
                    }

                    if (!pass.equals(pass1)) {
                        CliUi.error("Password confirmation does not match!");
                        break;
                    }

                    try {
                        authService.registerCustomer(name, pass);
                        CliUi.success("Register success!");
                    } catch (Exception e) {
                        CliUi.error("Register failed: " + e.getMessage());
                    }
                }
                case 2 -> {
                    CliUi.printSection("LOGIN");
                    String userName = InputValidator.readNonBlank(input, "UserName: ");
                    String pass = InputValidator.readNonBlank(input, "Password: ");

                    if (userName == null || pass == null) {
                        CliUi.error("Username and password must not be empty!");
                        break;
                    }
                    try {
                        User user = authService.login(userName, pass);
                        CliUi.success("Login success: hi " + user.getUserName());
                        switch (user.getRole()){
                            case Role.CUSTOMER ->  customermenu.menu(user);
                            case Role.MANAGER -> managermenu.menu(user);
                            case Role.CHEF -> chefmenu.menu(user);
                        }

                    } catch (Exception e) {
                        CliUi.error("Login failed: " + e.getMessage());
                    }
                }

                default -> {
                    CliUi.warning("Invalid option!");
                }
                case 0 -> {
                    CliUi.info("Good bye!");
                    return;
                }
            }
        }
    }
}
