package model;

import constance.OrderStatusItem;

public class OrderItem {
    private int id;
    private int orderId;
    private int menuItemId;
    private String menuItemName;
    private int quantity;
    private double price;
    private OrderStatusItem status;


    public OrderItem() {
    }

    public OrderItem(int id, int orderId, int menuItemId, String menuItemName, int quantity, OrderStatusItem status) {
        this.id = id;
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.quantity = quantity;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStatus(OrderStatusItem status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public OrderStatusItem getStatus() {
        return status;
    }

    public static String[] tableHeaders() {
        return new String[]{"ID", "Order ID", "Menu ID", "Menu Name", "Qty", "Status"};
    }

    public String[] toTableRow() {
        return new String[]{
                String.valueOf(id),
                String.valueOf(orderId),
                String.valueOf(menuItemId),
                menuItemName,
                String.valueOf(quantity),
                status == null ? "" : status.name()
        };
    }

    @Override
    public String toString() {
        return String.format(
                "%-5d | %-10d | %-10d | %-20s | %-10d | %-10s",
                id,
                orderId,
                menuItemId,
                menuItemName,
                quantity,
                status
        );
    }
}
