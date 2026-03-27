package dao.impl;

import config.DBConnection;
import constance.MenuItemStatus;
import dao.MenuItemDao;
import model.MenuItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class MenuItemDaoImpl implements MenuItemDao {

    @Override
    public int create(MenuItem menuItem) throws SQLException {
        String sql = "INSERT INTO menu_item(name, price, type, status) VALUE(?,?,?,?)";
        try(Connection connection = DBConnection.connectionDB();
            PreparedStatement ps =connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ){
            ps.setString(1, menuItem.getName());
            ps.setBigDecimal(2, menuItem.getPrice());
            ps.setString(3, menuItem.getType());
            ps.setString(4, menuItem.getStatus().name());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public boolean update(MenuItem menuItem) throws SQLException {
        String sql = "UPDATE menu_item SET name = ?, price = ?, type = ?, status = ? WHERE id  = ?";

        try(Connection connection  = DBConnection.connectionDB();
            PreparedStatement ps = connection.prepareStatement(sql)
                ){
            ps.setString(1, menuItem.getName());
            ps.setBigDecimal(2, menuItem.getPrice());
            ps.setString(3, menuItem.getType());
            ps.setString(4, menuItem.getStatus().name());
            ps.setInt(4, menuItem.getId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int menuItemId) throws SQLException {
        String sql = "DELETE from menu_item where id = ?";
        try(Connection connection = DBConnection.connectionDB();
            PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setInt(1, menuItemId);
                return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<MenuItem> findById(int menuItemId) throws SQLException {
        String sql = "SELECT id, name, price, type, status FROM menu_item WHERE id = ?";

        try(Connection connection = DBConnection.connectionDB();
        PreparedStatement ps =connection.prepareStatement(sql)
        ){
            ps.setInt(1, menuItemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<MenuItem> findAll() throws SQLException {
        String sql = "SELECT id, name, price, type, status FROM menu_item ORDER BY id";
        List<MenuItem> menuItems = new ArrayList<>();
        try (Connection connection = DBConnection.connectionDB();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                menuItems.add(map(rs));
            }
        }
        return menuItems;

    }

    @Override
    public List<MenuItem> searchByName(String name) throws SQLException {
        String sql = "SELECT id, name, price, type, status FROM menu_item WHERE LOWER(name) LIKE LOWER(?) ORDER BY id";
        List<MenuItem> menuItems = new ArrayList<>();
        try (Connection connection = DBConnection.connectionDB();
             PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, "%" + name + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    menuItems.add(map(rs));
                }
            }


        }
        return menuItems;
    }

    @Override
    public boolean updateStatus(int menuItemId, MenuItemStatus status) throws SQLException {
        String sql = "UPDATE menu_item SET  status = ? WHERE id  = ?";

        try(Connection connection  = DBConnection.connectionDB();
            PreparedStatement ps = connection.prepareStatement(sql)
        ){
            ps.setString(1, status.name());
            ps.setInt(2, menuItemId);

            return ps.executeUpdate() > 0;
        }

    }


    // Map ResultSet -> MenuItem
    private MenuItem map(ResultSet rs) throws SQLException {
        MenuItem item = new MenuItem();
        item.setId(rs.getInt("id"));
        item.setName(rs.getString("name"));
        item.setPrice(rs.getBigDecimal("price"));
        item.setType(rs.getString("type"));
        item.setStatus(MenuItemStatus.valueOf(rs.getString("status")));
        return item;
    }
}
