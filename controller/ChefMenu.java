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
            System.out.println("2. Start Processing (PENDING -> PROCESSING)");
            System.out.println("3. Mark as Ready (PROCESSING -> COMPLETED)");
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
                    List<String[]> rows = orderService.findIncomingForChef().stream()
                            .filter(item -> item.getStatus() == OrderStatusItem.PENDING)
                            .map(OrderItem::toTableRow)
                            .toList();

                    if (rows.isEmpty()) {
                        System.out.println("No items available.");
                        continue;
                    }

                    CliTable.print("PENDING ITEMS", OrderItem.tableHeaders(), rows, 5);

                    System.out.print("Enter Order Item ID (PENDING -> PROCESSING): ");
                    int orderItemId;
                    try {
                        orderItemId = Integer.parseInt(input.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input! Please enter a number.");
                        continue;
                    }

                    try {
                        orderService.advanceOrderItemStatus(orderItemId, OrderStatusItem.PROCESSING);
                        System.out.println("Order item moved to PROCESSING.");
                    } catch (Exception e) {
                        System.out.println("Failed: " + e.getMessage());
                    }
                }
                case 3 -> {
                    List<String[]> rows = orderService.findIncomingForChef().stream()
                            .filter(item -> item.getStatus() == OrderStatusItem.PROCESSING)
                            .map(OrderItem::toTableRow)
                            .toList();

                    if (rows.isEmpty()) {
                        System.out.println("No items available.");
                        continue;
                    }

                    CliTable.print("PROCESSING ITEMS", OrderItem.tableHeaders(), rows, 5);

                    System.out.print("Enter Order Item ID (PROCESSING -> COMPLETED): ");
                    int orderItemId;
                    try {
                        orderItemId = Integer.parseInt(input.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input! Please enter a number.");
                        continue;
                    }

                    try {
                        orderService.advanceOrderItemStatus(orderItemId, OrderStatusItem.COMPLETED);
                        System.out.println("Order item marked as COMPLETED.");
                    } catch (Exception e) {
                        System.out.println("Failed: " + e.getMessage());
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
