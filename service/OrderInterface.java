package service;

import constance.OrderStatusItem;
import model.Order;
import model.OrderItem;

import java.util.List;

public interface OrderInterface {
    int create(int userId, int tableId);

    void addOrderItem(int orderId, int menuItemId, int quantity);

    boolean removePendingOrderItem(int orderItemId);

    List<OrderItem> getOrderItems(int orderId);

    List<Order> getOrdersByUser(int userId);

    List<Order> getAllOrders();

    void approveOrder(int orderId);

    void advanceOrderItemStatus(int orderItemId, OrderStatusItem nextStatus);

    OrderItem findOrderItem(int orderItemId);
}
