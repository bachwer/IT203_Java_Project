package dao;

import constance.OrderStatusItem;
import model.OrderItem;

import java.sql.SQLException;
import java.util.List;

public interface OrderItemDao {
    int create(OrderItem orderItem) throws SQLException;

    List<OrderItem> findByOrderId(int orderId) throws SQLException;

    List<OrderItem> findIncomingForChef() throws SQLException;

    boolean updateStatus(int orderItemId, OrderStatusItem status) throws SQLException;

    boolean removePendingItem(int orderItemId) throws SQLException;
}
