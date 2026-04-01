package dao;

import constance.Role;
import model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    int create(User user) throws SQLException;

    Optional<User> findById(int id) throws SQLException;

    Optional<User> findByUsername(String username) throws SQLException;

    boolean updateStatus(int userId, String status) throws SQLException;

    List<User> findByRole(Role role) throws SQLException;

    List<User> findAll() throws SQLException;
}
