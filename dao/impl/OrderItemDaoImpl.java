package dao.impl;

import config.DBConnection;
import constance.OrderStatusItem;
import dao.OrderItemDao;
import model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDaoImpl implements OrderItemDao {
    @Override
    public int create(OrderItem orderItem) throws SQLException {
        String sql = "INSERT INTO orderItem(orderId, menuItemId, quantity, status) value(?,?,?,?)";

        try(Connection connection =DBConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ){
            ps.setInt(1, orderItem.getOrderId());
            ps.setInt(2, orderItem.getMenuItemId());
            ps.setInt(3, orderItem.getQuantity());
            ps.setString(4, orderItem.getStatus().name());

            ps.executeUpdate();
            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    return rs.getInt(1);
                }
            }

        }
        return -1;
    }

    @Override
    public List<OrderItem> findByOrderId(int orderId) throws SQLException {
        String sql = "SELECT oi.id, oi.orderId, oi.menuItemId, oi.quantity, oi.status, mi.name AS menu_item_name, mi.price AS unit_price FROM orderItem oi JOIN menu_item mi ON oi.menuItemId = mi.id WHERE oi.orderId = ? ORDER BY oi.id";
        List<OrderItem> orderItem = new ArrayList<>();
        try(Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)
        ){
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orderItem.add(map(rs));
                }
            }

        }
        return orderItem;
    }

    @Override
    public List<OrderItem> findIncomingForChef() throws SQLException {
        String sql = "Select  oi.id, oi.orderId, oi.menuItemId, mi.name AS menu_item_name, oi.quantity, oi.status, mi.price AS unit_price from orderItem oi join menu_item mi  ON oi.menuItemId = mi.id JOIN orders o ON oi.orderId = o.id WHERE o.approved = FALSE AND oi.status <> 'COMPLETED' ORDER BY oi.id";
        List<OrderItem> items = new ArrayList<>();

        try(Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                items.add(map(rs));
            }
        }
        return items;
    }

    @Override
    public boolean updateStatus(int orderItemId, OrderStatusItem status) throws SQLException {
        String sql = "UPDATE orderItem SET status = ? WHERE id = ?";

        try(Connection connection =DBConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)
        ){
            ps.setString(1, status.name());
            ps.setInt(2, orderItemId);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean removePendingItem(int orderItemId) throws SQLException {
        String sql = "DELETE FROM orderItem WHERE id = ? AND status = 'PENDING'";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderItemId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public long countByMenuItemId(int menuItemId) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM orderItem WHERE menuItemId = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, menuItemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("count");
                }
            }
        }
        return 0;
    }


    private OrderItem map(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem(
                rs.getInt("id"),
                rs.getInt("orderId"),
                rs.getInt("menuItemId"),
                rs.getString("menu_item_name"),
                rs.getInt("quantity"),
                OrderStatusItem.valueOf(rs.getString("status"))
        );
        item.setPrice(rs.getDouble("unit_price"));
        return item;
    }
}
