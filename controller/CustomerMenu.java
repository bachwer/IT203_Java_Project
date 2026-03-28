package controller;

import constance.MenuItemStatus;
import constance.TableStatus;
import model.User;
import service.impl.MenuService;
import service.impl.OrderService;
import service.impl.ReviewService;
import service.impl.TableService;

import java.util.Scanner;

public class CustomerMenu {
    private final OrderService orderService = new OrderService();
    private final ReviewService reviewService = new ReviewService();
    private final MenuService menuService = new MenuService();
    private final TableService tableService = new TableService();


    private static final Scanner input = new Scanner(System.in);
    public void menu(User user) {
        int choice;

        while(true){
            System.out.println("=== Customer Menu - " + user.getUserName() + " ===");
            System.out.println("1. View Menu");
            System.out.println("2. Choose Table & Create Order");
            System.out.println("3. Add Item to Current Order");
            System.out.println("4. Remove Item from Current Order");
            System.out.println("5. View My Orders");
            System.out.println("6. Checkout Current Order");
            System.out.println("7. Add Review");
            System.out.println("0. Logout");


            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }

            switch(choice){
                case 1 -> {
                    System.out.println("=== MENU ITEMS ===");
                    menuService.getAll().stream()
                            .filter(item -> item.getStatus() == MenuItemStatus.AVAILABLE)
                            .forEach(System.out::println);
                }
                case 2 -> {
                    System.out.print("====Choose table ==== ");
                    System.out.println("=== TABLES ===");
                    tableService.getAll().stream()
                            .filter(item -> item.getStatus() == TableStatus.AVAILABLE)
                            .forEach(System.out::println);

                    System.out.println("Enter id table: ");
                    int idTable;

                    try {
                        idTable = Integer.parseInt(input.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input! Please enter a number.");
                        continue;
                    }
                    orderService.create(user.getId(), idTable);
                    tableService.markOccupied(idTable);

                }
                case 3 -> {
                    var activeOrders = orderService.getOrdersByUser(user.getId());

                    if (activeOrders.isEmpty()) {
                        System.out.println("You don't have an active order. Please create one first.");
                        continue;
                    }

                    if (activeOrders.size() > 1) {
                        System.out.println("Multiple active orders found. Please contact staff.");
                        continue;
                    }

                    var order = activeOrders.get(0);

                    System.out.print("Enter menu item id: ");
                    int menuItemId;
                    try {
                        menuItemId = Integer.parseInt(input.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input!");
                        continue;
                    }

                    System.out.print("Enter quantity: ");
                    int quantity;
                    try {
                        quantity = Integer.parseInt(input.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input!");
                        continue;
                    }

                    orderService.addOrderItem(order.getId(), menuItemId, quantity);
                    System.out.println("Item added successfully.");
                }
                case 4 -> {
                    System.out.print("Enter order item id to remove: ");
                    int orderItemId;
                    try {
                        orderItemId = Integer.parseInt(input.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input!");
                        continue;
                    }

                    boolean removed = orderService.removePendingOrderItem(orderItemId);
                    System.out.println(removed ? "Removed successfully." : "Cannot remove item.");
                }

                case 5 -> {
                    System.out.println("=== MY ORDERS ===");
                    orderService.getOrdersByUser(user.getId())
                            .forEach(System.out::println);
                }

                case 6 -> {
                    System.out.print("Enter order id to checkout: ");
                    int orderId;
                    try {
                        orderId = Integer.parseInt(input.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input!");
                        continue;
                    }

                    orderService.approveOrder(orderId);
                    System.out.println("Order approved (checkout successful).");
                }

                case 7 -> {
                    System.out.print("Enter rating (1-5): ");
                    int rating;
                    try {
                        rating = Integer.parseInt(input.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input!");
                        continue;
                    }

                    if (rating < 1 || rating > 5) {
                        System.out.println("Rating must be between 1 and 5.");
                        continue;
                    }

                    System.out.print("Enter comment: ");
                    String comment = input.nextLine().trim();

                    if (comment.isEmpty()) {
                        System.out.println("Comment cannot be empty.");
                        continue;
                    }

                    try {
                        reviewService.addReview(user.getId(), rating, comment);
                        System.out.println("Review added successfully.");
                    } catch (Exception e) {
                        System.out.println("Failed to add review: " + e.getMessage());
                    }
                }



                case 0 -> {return;}
            }
        }

    }
}
