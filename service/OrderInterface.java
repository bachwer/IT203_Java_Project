package service;

import constance.OrderStatusItem;
import model.Order;
import model.OrderItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderInterface {
    int create(int userId, int tableId);

    void addOrderItem(int orderId, int menuItemId, int quantity);

    boolean removePendingOrderItem(int orderItemId);

    List<OrderItem> getOrderItems(int orderId);

    List<OrderItem> findIncomingForChef();

    List<Order> getOrdersByUser(int userId);

    Optional<Order> getCurrentOrderByUser(int userId);

    List<Order> getAllOrders();

    void saveOrderTotal(int orderId, BigDecimal total);

    BigDecimal getTotalRevenue();

    int countCheckedOutOrders();

    void approveOrder(int orderId);

    void advanceOrderItemStatus(int orderItemId, OrderStatusItem nextStatus);

    OrderItem findOrderItem(int orderItemId);
}
