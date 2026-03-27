package dao;

import constance.MenuItemStatus;
import model.MenuItem;


import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MenuItemDao {
    int create(MenuItem menuItem) throws SQLException;

    boolean update(MenuItem menuItem) throws SQLException;

    boolean delete(int menuItemId) throws SQLException;

    Optional<MenuItem> findById(int menuItemId) throws SQLException;

    List<MenuItem> findAll() throws SQLException;

    List<MenuItem> searchByName(String name) throws SQLException;

    boolean updateStatus(int menuItemId, MenuItemStatus status) throws SQLException;
}
