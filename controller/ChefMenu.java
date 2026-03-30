package controller;

import constance.OrderStatusItem;
import model.OrderItem;
import model.User;
import service.impl.OrderService;
import util.CliTable;

import java.util.List;
import java.util.Scanner;

public class ChefMenu {

    private static final Scanner input = new Scanner(System.in);
    private final OrderService orderService= new OrderService();
    public void menu(User user) {
        int CHOICE_VIEW_INCOMING = 1;


        while(true) {
            System.out.println("=== Chef Menu - " +  user.getUserName() + " ===");
            System.out.println("1. View Incoming Order Items");
            System.out.println("2. Update Item Status");
            System.out.println("0. Logout");

            try {
                CHOICE_VIEW_INCOMING = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }

            switch(CHOICE_VIEW_INCOMING) {
                case 1 -> {
                    List<String[]> rows = orderService.findIncomingForChef().stream()
                            .map(OrderItem::toTableRow)
                            .toList();

                    if (rows.isEmpty()) {
                        System.out.println("No incoming order items.");
                        continue;
                    }

                    CliTable.print("INCOMING ORDER ITEMS", OrderItem.tableHeaders(), rows, 5);

                }
                case 2 -> {
                    System.out.print("Enter Order Item ID to update: ");
                    int orderItemId;
                    try {
                        orderItemId = Integer.parseInt(input.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input! Please enter a number.");
                        continue;
                    }


                    System.out.println("Select new status:");
                    System.out.println("1. PENDING");
                    System.out.println("2. PROCESSING");
                    System.out.println("3. COMPLETED");
                    System.out.println("4. CANCELLED");

                    System.out.print("Enter choice (1-4): ");

                    int choice;
                    try {
                        choice = Integer.parseInt(input.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input! Please enter a number.");
                        continue;
                    }

                    OrderStatusItem status;
                    switch (choice) {
                        case 1:
                            status = OrderStatusItem.PENDING;
                            break;
                        case 2:
                            status = OrderStatusItem.PROCESSING;
                            break;
                        case 3:
                            status = OrderStatusItem.COMPLETED;
                            break;
                        case 4:
                            status = OrderStatusItem.CANCELLED;
                            break;
                        default:
                            System.out.println("Invalid choice! Please select 1-4.");
                            continue;
                    }

                    try {
                        orderService.advanceOrderItemStatus(orderItemId, status);
                        System.out.println("Order item status updated successfully.");
                    } catch (Exception e) {
                        System.out.println("Failed to update order item status: " + e.getMessage());
                    }
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid input!");
            }
        }

    }
}
