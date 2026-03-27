package dao.impl;

import config.DBConnection;
import constance.Role;
import constance.UserStatus;
import dao.UserDao;
import model.User;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    @Override
    public int create(User user) throws SQLException {
        String sql = "INSERT into users(name, password, role, status) value(?, ?, ? , ?)";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole().name());
            ps.setString(4, user.getStatus().name());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }


        }
        return -1;
    }

    @Override
    public Optional<User> findById(int id) throws SQLException {
        String sql = "SELECT id, name, password, role, status FROM users WHERE id = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) throws SQLException {
        String sql = "SELECT id, name, password, role, status FROM users WHERE name = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean updateStatus(int userId, String status) throws SQLException {
        String sql = "UPDATE users SET status = ? WHERE id = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<User> findByRole(Role role) throws SQLException {
        String sql = "SELECT id, name, password, role, status FROM users WHERE role = ?";
        List<User> users = new java.util.ArrayList<>();

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, role.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(map(rs));
                }
            }
        }

        return users;
    }

    private User map(ResultSet rs) throws SQLException {
        User item = new User();
        item.setId(rs.getInt("id"));
        item.setUserName(rs.getString("name"));
        item.setRole(Role.valueOf(rs.getString("role")));
        item.setStatus(UserStatus.valueOf(rs.getString("status")));
        item.setPassword(rs.getString("password"));
        return item;
    }
}
