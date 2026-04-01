package controller;

import constance.OrderStatusItem;
import model.OrderItem;
import model.User;
import service.impl.OrderService;
import util.CliTable;
import util.CliUi;
import util.InputValidator;

import java.util.List;
import java.util.Scanner;

public class ChefMenu {

    private static final Scanner input = new Scanner(System.in);
    private final OrderService orderService= new OrderService();
    public void menu(User user) {
        int CHOICE_VIEW_INCOMING = 1;


        while(true) {
            CliUi.printMenu("CHEF MENU - " + user.getUserName(), List.of(
                    "1. View Incoming Order Items",
                    "2. Start Processing (PENDING -> PROCESSING)",
                    "3. Mark as Ready (PROCESSING -> COMPLETED)",
                    "0. Logout"
            ));

            Integer selected = InputValidator.readInt(input, "Select option: ");
            if (selected == null) {
                CliUi.error("Invalid input! Please enter a number.");
                continue;
            }
            CHOICE_VIEW_INCOMING = selected;

            switch(CHOICE_VIEW_INCOMING) {
                case 1 -> {
                    List<String[]> rows = orderService.findIncomingForChef().stream()
                            .map(OrderItem::toTableRow)
                            .toList();

                    if (rows.isEmpty()) {
                        CliUi.info("No incoming order items.");
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
                        CliUi.info("No items available.");
                        continue;
                    }

                    CliTable.print("PENDING ITEMS", OrderItem.tableHeaders(), rows, 5);

                    Integer orderItemId = InputValidator.readInt(input, "Enter order item ID (PENDING -> PROCESSING): ");
                    if (orderItemId == null || orderItemId <= 0) {
                        CliUi.warning("Invalid order item ID!");
                        continue;
                    }

                    try {
                        orderService.advanceOrderItemStatus(orderItemId, OrderStatusItem.PROCESSING);
                        CliUi.success("Order item moved to PROCESSING.");
                    } catch (Exception e) {
                        CliUi.error("Failed: " + e.getMessage());
                    }
                }
                case 3 -> {
                    List<String[]> rows = orderService.findIncomingForChef().stream()
                            .filter(item -> item.getStatus() == OrderStatusItem.PROCESSING)
                            .map(OrderItem::toTableRow)
                            .toList();

                    if (rows.isEmpty()) {
                        CliUi.info("No items available.");
                        continue;
                    }

                    CliTable.print("PROCESSING ITEMS", OrderItem.tableHeaders(), rows, 5);

                    Integer orderItemId = InputValidator.readInt(input, "Enter order item ID (PROCESSING -> COMPLETED): ");
                    if (orderItemId == null || orderItemId <= 0) {
                        CliUi.warning("Invalid order item ID!");
                        continue;
                    }

                    try {
                        orderService.advanceOrderItemStatus(orderItemId, OrderStatusItem.COMPLETED);
                        CliUi.success("Order item marked as COMPLETED.");
                    } catch (Exception e) {
                        CliUi.error("Failed: " + e.getMessage());
                    }
                }
                case 0 -> {
                    CliUi.info("Logout chef account.");
                    return;
                }
                default -> CliUi.warning("Invalid option!");
            }
        }

    }
}
