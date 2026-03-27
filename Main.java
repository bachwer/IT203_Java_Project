import constance.Role;
import model.User;
import service.impl.*;
import controller.ChefMenu;
import controller.CustomerMenu;
import controller.MainMenu;
import controller.ManagerMenu;

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
            try {
                String line = input.nextLine().trim();
                if (line.isEmpty()) {
                    System.out.println("Please enter a number!");
                    continue;
                }
                choice = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
                continue;
            }


            switch (choice) {
                case 1 -> {
                    System.out.print("UserName: ");
                    String name = input.nextLine();
                    System.out.print("passWord: ");
                    String pass = input.nextLine();
                    System.out.print("ConfirmPass: ");
                    String pass1 = input.nextLine();

                    if (!pass.equals(pass1)) {
                        System.out.println("Password confirmation does not match!");
                        break;
                    }

                    try {
                        authService.registerCustomer(name, pass);
                        System.out.println("Register success!");
                    } catch (Exception e) {
                        System.out.println("Register failed: " + e.getMessage());
                    }
                }
                case 2 -> {
                    System.out.print("UserName: ");
                    String userName = input.nextLine().trim();
                    System.out.print("Password: ");
                    String pass = input.nextLine();

                    if (userName.isEmpty() || pass.isEmpty()) {
                        System.out.println("Username and password must not be empty!");
                        break;
                    }
                    try {
                        User user = authService.login(userName, pass);
                        System.out.println("Login success: hi " + user.getUserName());
                        switch (user.getRole()){
                            case Role.CUSTOMER ->  customermenu.menu(user);
                            case Role.MANAGER -> managermenu.menu(user);
                            case Role.CHEF -> chefmenu.menu(user);
                        }

                    } catch (Exception e) {
                        System.out.println("Login failed: " + e.getMessage());
                    }
                }

                default -> {
                    System.out.println("invalid input !");
                }
                case 0 -> {
                    return;
                }
            }
        }
    }
}
