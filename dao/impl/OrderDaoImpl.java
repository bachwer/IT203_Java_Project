package dao.impl;


import config.DBConnection;
import constance.OrderStatus;
import dao.OrderDao;
import model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDaoImpl implements OrderDao {
    @Override
    public int create(Order order) throws SQLException {
        String sql = "INSERT into orders(userId, tableId, status, approved) values (?,?,?,?)";

        try(Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ){
            ps.setInt(1, order.getUserId());
            ps.setInt(2, order.getTableId());
            ps.setString(3, order.getStatus().name());
            ps.setBoolean(4, order.isApproved());

            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return -1;
    }

    @Override
    public Optional<Order> findById(int orderId) throws SQLException {
        String sql = "SELECT id, userId, tableId, status, approved, createdAt FROM orders WHERE id = ?";

        try(Connection connection =DBConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)

        ){

            ps.setInt(1, orderId);
            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }


        }

        return Optional.empty();
    }



    @Override
    public List<Order> findByUserId(int userId) throws SQLException {
        String sql = "SELECT id, userId, tableId, status, approved, createdAt FROM orders WHERE userId = ? ORDER BY id DESC";

        List<Order> orders = new ArrayList<>();
        try(Connection connection =DBConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)
        ){
            ps.setInt(1, userId);

            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    orders.add(map(rs));
                }
            }
        }
        return orders;
    }

    @Override
    public List<Order> findAll() throws SQLException {
        String sql = "SELECT id, userId, tableId, status, approved, createdAt FROM orders ORDER BY id DESC";

        List<Order> orders = new ArrayList<>();
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                orders.add(map(rs));
            }
        }
        return orders;
    }

    @Override
    public boolean updateStatus(int orderId, OrderStatus status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateApproval(int orderId, boolean approved) throws SQLException {
        String sql = "UPDATE orders SET approved = ? WHERE id = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, approved);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        }
    }


    private Order map(ResultSet rs) throws SQLException{
        Timestamp createdAt = rs.getTimestamp("createdAt");
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setUserId(rs.getInt("userId"));
        order.setTableId(rs.getInt("tableId"));
        order.setStatus(OrderStatus.fromDb(rs.getString("status")));
        order.setApproved(rs.getBoolean("approved"));
        order.setCreatedAt(createdAt == null ? null : createdAt.toLocalDateTime());
        return order;
    }
}
