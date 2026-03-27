package controller;

import constance.Role;
import constance.UserStatus;
import model.MenuItem;
import model.Review;
import model.User;
import service.impl.AuthService;
import service.impl.MenuService;
import service.impl.OrderService;
import service.impl.ReviewService;

import java.util.List;
import java.util.Scanner;

import static util.PasswordHasher.hash;

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
            System.out.println("=== Manager Menu - " + user.getUserName() + " ===");
            System.out.println("1. CRUD Menu Items");
            System.out.println("2. CRUD Tables");
            System.out.println("3. Search Menu by Name");
            System.out.println("4. Approve Orders");
            System.out.println("5. Create Chef User");
            System.out.println("6. Ban Customer");
            System.out.println("7. View Reviews");
            System.out.println("0. Logout");

            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }
            switch(choice) {
                case 1 -> {menuCRUD.menu("MenuItem");}
                case 2 -> {menuCRUD.menu("Table");}
                case 3 -> {
                    System.out.print("Enter the name of menu to search: ");
                    String name = input.nextLine().trim();
                    if (name.isEmpty()) {
                        System.out.println("Name cannot be empty!");
                        break;
                    }
                    List<MenuItem> Items = menuService.searchByName(name);

                    if(Items.isEmpty()){
                        System.out.println("Don't Found!");
                        return;
                    }

                    for(MenuItem m :  Items){
                        System.out.println(m.toString());
                    }


                }
                case 4 -> {
                    System.out.println("Enter id order to Approve: ");
                    try {
                        int id = Integer.parseInt(input.nextLine());
                        if (id <= 0) {
                            System.out.println("Invalid order ID!");
                            break;
                        }
                        try {
                            orderService.approveOrder(id);
                            System.out.println("Order approved successfully.");
                        } catch (Exception ex) {
                            System.out.println("Failed to approve order.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Order ID must be a number!");
                    }
                }
                case 5 -> {
                    System.out.println();

                    System.out.print("Enter the name chef: ");
                    String name = input.nextLine().trim();
                    if (name.isEmpty()) {
                        System.out.println("Chef name cannot be empty!");
                        break;
                    }

                    System.out.print("Enter password: ");
                    String password = input.nextLine().trim();
                    if (password.length() < 6) {
                        System.out.println("Password must be at least 6 characters!");
                        break;
                    }

                    try {
                        authService.createChef(name, password);
                        System.out.println("Chef account created successfully.");
                    } catch (Exception e) {
                        System.out.println("Failed to create chef account.");
                    }
                }
                case 6 -> {
                    System.out.println("Enter user want to Ban: ");
                    try {
                        int id = Integer.parseInt(input.nextLine());
                        if (id <= 0) {
                            System.out.println("Invalid user ID!");
                            break;
                        }
                        try {
                            authService.banUser(id);
                            System.out.println("User has been banned successfully.");
                        } catch (Exception ex) {
                            System.out.println("Failed to ban user.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("User ID must be a number!");
                    }
                }
                case 7 -> {

                    List<Review> re = reviewService.getAllReviews();
                    if (re.isEmpty()) {
                        System.out.println("No reviews found!");
                        break;
                    }

                    for(Review r: re){
                        System.out.println(r.toString());
                    }

                }
                case 0 -> {
                    return;
                }
            }
        }
    }


}
