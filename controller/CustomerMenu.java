package controller;

import constance.MenuItemStatus;
import constance.OrderStatusItem;
import constance.TableStatus;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.Table;
import model.User;
import service.impl.MenuService;
import service.impl.OrderService;
import service.impl.ReviewService;
import service.impl.TableService;
import util.CliTable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
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
            System.out.println("5. View Current Order");
            System.out.println("6. Order History");
            System.out.println("7. Checkout Current Order");
            System.out.println("8. Add Review");
            System.out.println("0. Logout");


            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }

            switch(choice){
                case 1 -> {
                    List<String[]> rows = menuService.getAll().stream()
                            .filter(item -> item.getStatus() == MenuItemStatus.AVAILABLE)
                            .map(item -> item.toTableRow())
                            .toList();
                    CliTable.print("MENU ITEMS", MenuItem.tableHeaders(), rows, 4);
                }
                case 2 -> {
                    if (orderService.getCurrentOrderByUser(user.getId()).isPresent()) {
                        System.out.println("You already have a current order. Please checkout first.");
                        continue;
                    }

                    System.out.print("====Choose table ==== ");
                    List<String[]> rows = tableService.getAll().stream()
                            .filter(item -> item.getStatus() == TableStatus.AVAILABLE)
                            .map(item -> item.toTableRow())
                            .toList();
                    CliTable.print("AVAILABLE TABLES", Table.tableHeaders(), rows, 3);

                    System.out.println("Enter id table: ");
                    int idTable;

                    try {
                        idTable = Integer.parseInt(input.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input! Please enter a number.");
                        continue;
                    }

                    try {
                        orderService.create(user.getId(), idTable);
                        System.out.println("Order created successfully.");
                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage());
                    }

                }
                case 3 -> {
                    var currentOrder = orderService.getCurrentOrderByUser(user.getId());

                    if (currentOrder.isEmpty()) {
                        System.out.println("You don't have an active order. Please create one first.");
                        continue;
                    }

                    var order = currentOrder.get();

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
                    var currentOrder = orderService.getCurrentOrderByUser(user.getId());
                    if (currentOrder.isEmpty()) {
                        System.out.println("No current order. Please create one first.");
                        continue;
                    }

                    System.out.print("Enter order item id to remove: ");
                    int orderItemId;
                    try {
                        orderItemId = Integer.parseInt(input.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input!");
                        continue;
                    }

                    boolean belongsToCurrentOrder = orderService.getOrderItems(currentOrder.get().getId()).stream()
                            .anyMatch(item -> item.getId() == orderItemId);
                    if (!belongsToCurrentOrder) {
                        System.out.println("Order item does not belong to your current order.");
                        continue;
                    }

                    boolean removed = orderService.removePendingOrderItem(orderItemId);
                    System.out.println(removed ? "Removed successfully." : "Cannot remove item.");
                }

                case 5 -> {
                    var currentOrder = orderService.getCurrentOrderByUser(user.getId());
                    if (currentOrder.isEmpty()) {
                        System.out.println("No current order. Please create one first.");
                        continue;
                    }

                    List<String[]> rows = orderService.getOrderItems(currentOrder.get().getId()).stream()
                            .map(OrderItem::toTableRow)
                            .toList();
                    if (rows.isEmpty()) {
                        System.out.println("Current order has no items yet.");
                        continue;
                    }
                    CliTable.print("CURRENT ORDER ITEMS", OrderItem.tableHeaders(), rows, 5);
                }

                case 6 -> {
                    List<String[]> rows = orderService.getOrdersByUser(user.getId()).stream()
                            .map(order -> order.toTableRow())
                            .toList();
                    CliTable.print("ORDER HISTORY", Order.tableHeaders(), rows, 3);
                }

                case 7 -> {
                    var currentOrder = orderService.getCurrentOrderByUser(user.getId());
                    if (currentOrder.isEmpty()) {
                        System.out.println("No current order. Please create one first.");
                        continue;
                    }

                    int orderId = currentOrder.get().getId();

                    List<OrderItem> orderItems = orderService.getOrderItems(orderId);
                    if (orderItems.isEmpty()) {
                        System.out.println("Cannot checkout. Current order has no items.");
                        continue;
                    }

                    boolean hasNotReadyItem = orderItems.stream().anyMatch(item ->
                            item.getStatus() == OrderStatusItem.PENDING || item.getStatus() == OrderStatusItem.PROCESSING);

                    if (hasNotReadyItem) {
                        System.out.println("Cannot checkout. Some items are not completed yet.");
                        continue;
                    }

                    List<String[]> billRows = new ArrayList<>();
                    BigDecimal totalAmount = BigDecimal.ZERO;

                    for (OrderItem item : orderItems) {
                        BigDecimal unitPrice = BigDecimal.valueOf(item.getPrice()).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                        totalAmount = totalAmount.add(lineTotal);

                        billRows.add(new String[]{
                                String.valueOf(item.getId()),
                                item.getMenuItemName(),
                                String.valueOf(item.getQuantity()),
                                unitPrice.toPlainString(),
                                lineTotal.setScale(2, RoundingMode.HALF_UP).toPlainString(),
                                item.getStatus() == null ? "" : item.getStatus().name()
                        });
                    }

                    CliTable.print(
                            "PAYMENT BILL",
                            new String[]{"Item ID", "Menu Name", "Qty", "Unit Price", "Line Total", "Status"},
                            billRows,
                            5
                    );
                    System.out.println("Total Amount: " + totalAmount.setScale(2, RoundingMode.HALF_UP).toPlainString());
                    System.out.print("Confirm payment? (Y/N): ");
                    String confirm = input.nextLine().trim();

                    if (!confirm.equalsIgnoreCase("Y")) {
                        System.out.println("Payment cancelled.");
                        continue;
                    }

                    orderService.saveOrderTotal(orderId, totalAmount.setScale(2, RoundingMode.HALF_UP));
                    orderService.approveOrder(orderId);
                    System.out.println("Order approved (checkout successful).");
                }

                case 8 -> {
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
