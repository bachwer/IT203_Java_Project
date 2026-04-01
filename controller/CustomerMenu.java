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
import util.CliUi;
import util.InputValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
            CliUi.printMenu("CUSTOMER MENU - " + user.getUserName(), List.of(
                    "1. View Menu",
                    "2. Choose Table & Create Order",
                    "3. Add Item to Current Order",
                    "4. Remove Item from Current Order",
                    "5. View Current Order",
                    "6. Order History",
                    "7. Checkout Current Order",
                    "8. Add Review",
                    "0. Logout"
            ));


            Integer selected = InputValidator.readInt(input, "Select option: ");
            if (selected == null) {
                CliUi.error("Invalid input! Please enter a number.");
                continue;
            }
            choice = selected;

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
                        CliUi.warning("You already have a current order. Please checkout first.");
                        continue;
                    }

                    List<String[]> rows = tableService.getAll().stream()
                            .filter(item -> item.getStatus() == TableStatus.AVAILABLE)
                            .map(item -> item.toTableRow())
                            .toList();
                    CliTable.print("AVAILABLE TABLES", Table.tableHeaders(), rows, 3);

                    Integer idTable = InputValidator.readInt(input, "Enter table ID: ");
                    if (idTable == null || idTable <= 0) {
                        CliUi.warning("Invalid input! Please enter a positive number.");
                        continue;
                    }

                    try {
                        orderService.create(user.getId(), idTable);
                        CliUi.success("Order created successfully.");
                    } catch (IllegalStateException e) {
                        CliUi.error(e.getMessage());
                    }

                }
                case 3 -> {
                    var currentOrder = orderService.getCurrentOrderByUser(user.getId());

                    if (currentOrder.isEmpty()) {
                        CliUi.warning("You don't have an active order. Please create one first.");
                        continue;
                    }

                    var order = currentOrder.get();

                    Integer menuItemId = InputValidator.readInt(input, "Enter menu item ID: ");
                    if (menuItemId == null || menuItemId <= 0) {
                        CliUi.warning("Invalid menu item ID!");
                        continue;
                    }

                    Integer quantity = InputValidator.readInt(input, "Enter quantity: ");
                    if (quantity == null || quantity <= 0) {
                        CliUi.warning("Quantity must be greater than 0.");
                        continue;
                    }

                    orderService.addOrderItem(order.getId(), menuItemId, quantity);
                    CliUi.success("Item added successfully.");
                }
                case 4 -> {
                    var currentOrder = orderService.getCurrentOrderByUser(user.getId());
                    if (currentOrder.isEmpty()) {
                        CliUi.warning("No current order. Please create one first.");
                        continue;
                    }

                    Integer orderItemId = InputValidator.readInt(input, "Enter order item ID to remove: ");
                    if (orderItemId == null || orderItemId <= 0) {
                        CliUi.warning("Invalid order item ID!");
                        continue;
                    }

                    boolean belongsToCurrentOrder = orderService.getOrderItems(currentOrder.get().getId()).stream()
                            .anyMatch(item -> item.getId() == orderItemId);
                    if (!belongsToCurrentOrder) {
                        CliUi.warning("Order item does not belong to your current order.");
                        continue;
                    }

                    boolean removed = orderService.removePendingOrderItem(orderItemId);
                    if (removed) {
                        CliUi.success("Removed successfully.");
                    } else {
                        CliUi.warning("Cannot remove item.");
                    }
                }

                case 5 -> {
                    var currentOrder = orderService.getCurrentOrderByUser(user.getId());
                    if (currentOrder.isEmpty()) {
                        CliUi.warning("No current order. Please create one first.");
                        continue;
                    }

                    List<String[]> rows = orderService.getOrderItems(currentOrder.get().getId()).stream()
                            .map(OrderItem::toTableRow)
                            .toList();
                    if (rows.isEmpty()) {
                        CliUi.info("Current order has no items yet.");
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
                        CliUi.warning("No current order. Please create one first.");
                        continue;
                    }

                    int orderId = currentOrder.get().getId();

                    List<OrderItem> orderItems = orderService.getOrderItems(orderId);
                    if (orderItems.isEmpty()) {
                        CliUi.warning("Cannot checkout. Current order has no items.");
                        continue;
                    }

                    boolean hasNotReadyItem = orderItems.stream().anyMatch(item ->
                            item.getStatus() == OrderStatusItem.PENDING || item.getStatus() == OrderStatusItem.PROCESSING);

                    if (hasNotReadyItem) {
                        CliUi.warning("Cannot checkout. Some items are not completed yet.");
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
                    CliUi.info("Total Amount: " + totalAmount.setScale(2, RoundingMode.HALF_UP).toPlainString());
                    String confirm = InputValidator.readNonBlank(input, "Confirm payment? (Y/N): ");

                    if (!InputValidator.isYes(confirm)) {
                        CliUi.info("Payment cancelled.");
                        continue;
                    }

                    orderService.saveOrderTotal(orderId, totalAmount.setScale(2, RoundingMode.HALF_UP));
                    orderService.approveOrder(orderId);
                    CliUi.success("Checkout successful.");
                }

                case 8 -> {
                    Integer rating = InputValidator.readInt(input, "Enter rating (1-5): ");
                    if (rating == null) {
                        CliUi.warning("Invalid rating!");
                        continue;
                    }

                    if (!InputValidator.isRatingValid(rating)) {
                        CliUi.warning("Rating must be between 1 and 5.");
                        continue;
                    }

                    String comment = InputValidator.readNonBlank(input, "Enter comment: ");

                    if (comment == null) {
                        CliUi.warning("Comment cannot be empty.");
                        continue;
                    }

                    try {
                        reviewService.addReview(user.getId(), rating, comment);
                        CliUi.success("Review added successfully.");
                    } catch (Exception e) {
                        CliUi.error("Failed to add review: " + e.getMessage());
                    }
                }



                case 0 -> {
                    CliUi.info("Logout customer account.");
                    return;
                }
                default -> CliUi.warning("Invalid option!");
            }
        }

    }
}
