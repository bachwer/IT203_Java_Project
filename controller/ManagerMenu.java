package controller;

import model.MenuItem;
import model.Review;
import model.User;
import service.impl.AuthService;
import service.impl.MenuService;
import service.impl.OrderService;
import service.impl.ReviewService;
import util.CliTable;
import util.CliUi;
import util.InputValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ManagerMenu {
    ManagerCRUD menuCRUD = new ManagerCRUD();
    private final MenuService menuService = new MenuService();
    private final OrderService  orderService= new OrderService();
    private final AuthService authService = new AuthService();
    private final ReviewService reviewService = new ReviewService();
    private static final Scanner input = new Scanner(System.in);


    public void menu(User user) {
        int choice;
        while(true) {
            CliUi.printMenu("MANAGER MENU - " + user.getUserName(), List.of(
                    "1. CRUD Menu Items",
                    "2. CRUD Tables",
                    "3. Search Menu by Name",
                    "4. Create Chef User",
                    "5. Ban Customer",
                    "6. Unban Customer",
                    "7. Show Full Users",
                    "8. View Reviews",
                    "9. View Revenue Report",
                    "0. Logout"
            ));

            Integer selected = InputValidator.readInt(input, "Select option: ");
            if (selected == null) {
                CliUi.error("Invalid input! Please enter a number.");
                continue;
            }
            choice = selected;
            switch(choice) {
                case 1 -> menuCRUD.menu("MenuItem");
                case 2 -> menuCRUD.menu("Table");
                case 3 -> {
                    String name = InputValidator.readNonBlank(input, "Enter menu name to search: ");
                    if (name == null) {
                        CliUi.warning("Name cannot be empty!");
                        break;
                    }
                    List<MenuItem> items = menuService.searchByName(name);

                    if(items.isEmpty()){
                        CliUi.info("No menu item found!");
                        break;
                    }

                    List<String[]> rows = items.stream().map(MenuItem::toTableRow).toList();
                    CliTable.print("SEARCH MENU ITEMS", MenuItem.tableHeaders(), rows, 4);
                    pause();


                }
                case 4 -> {
                    String name = InputValidator.readNonBlank(input, "Enter chef username: ");
                    if (name == null) {
                        CliUi.warning("Chef name cannot be empty!");
                        break;
                    }

                    String password = InputValidator.readNonBlank(input, "Enter password: ");
                    if (password == null) {
                        CliUi.warning("Password cannot be empty!");
                        break;
                    }

                    if (password.length() < 6) {
                        CliUi.warning("Password must be at least 6 characters!");
                        break;
                    }

                    try {
                        authService.createChef(name, password);
                        CliUi.success("Chef account created successfully.");
                    } catch (Exception e) {
                        CliUi.error("Failed to create chef account: " + e.getMessage());
                    }
                }
                case 5 -> {
                    Integer id = InputValidator.readInt(input, "Enter customer user ID to ban: ");
                    if (id == null || id <= 0) {
                        CliUi.warning("User ID must be a positive number!");
                        break;
                    }
                    if (id == user.getId()) {
                        CliUi.warning("You cannot ban your own account.");
                        break;
                    }
                    try {
                        authService.banUser(id);
                        CliUi.success("User has been banned successfully.");
                    } catch (Exception ex) {
                        CliUi.error("Failed to ban user: " + ex.getMessage());
                    }
                }
                case 6 -> {
                    Integer id = InputValidator.readInt(input, "Enter customer user ID to unban: ");
                    if (id == null || id <= 0) {
                        CliUi.warning("User ID must be a positive number!");
                        break;
                    }
                    try {
                        authService.unbanUser(id);
                        CliUi.success("User has been unbanned successfully.");
                    } catch (Exception ex) {
                        CliUi.error("Failed to unban user: " + ex.getMessage());
                    }
                }
                case 7 -> {
                    List<User> users = authService.getAllUsers();
                    if (users.isEmpty()) {
                        CliUi.info("No users found!");
                        break;
                    }

                    List<String[]> rows = users.stream().map(User::toTableRow).toList();
                    CliTable.print("FULL USER LIST", User.tableHeaders(), rows, 3);
                    pause();
                }
                case 8 -> {

                    List<Review> re = reviewService.getAllReviews();
                    if (re.isEmpty()) {
                        CliUi.info("No reviews found!");
                        break;
                    }

                    List<String[]> rows = re.stream().map(Review::toTableRow).toList();
                    CliTable.print("CUSTOMER REVIEWS", Review.tableHeaders(), rows);
                    pause();

                }
                case 9 -> {
                    int checkedOutOrders = orderService.countCheckedOutOrders();
                    BigDecimal totalRevenue = orderService.getTotalRevenue().setScale(2, RoundingMode.HALF_UP);

                    List<String[]> rows = new ArrayList<>();
                    rows.add(new String[]{
                            String.valueOf(checkedOutOrders),
                            totalRevenue.toPlainString()
                    });
                    CliTable.print("REVENUE REPORT", new String[]{"Checked Out Orders", "Total Revenue"}, rows);
                    pause();
                }
                case 0 -> {
                    CliUi.info("Logout manager account.");
                    return;
                }
                default -> CliUi.warning("Invalid option!");
            }
        }
    }

    private void pause() {
        System.out.print("Press Enter to continue...");
        input.nextLine();
    }


}
