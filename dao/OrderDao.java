package dao;

import constance.OrderStatus;
import model.Order;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface OrderDao {
    int create(Order order) throws SQLException;
    Optional<Order> findById(int orderId) throws SQLException;
    List<Order> findByUserId(int userId) throws SQLException;
    List<Order> findAll() throws SQLException;
    boolean updateStatus(int orderId, OrderStatus status) throws SQLException;
    boolean updateApproval(int orderId, boolean approved) throws SQLException;
}
