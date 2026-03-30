package service.impl;

import dao.MenuItemDao;
import dao.impl.MenuItemDaoImpl;
import model.MenuItem;
import service.MenuInterface;
import util.InputValidator;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public class MenuService implements MenuInterface {
    private final MenuItemDao menuItemDao = new MenuItemDaoImpl();
    @Override
    public int create(MenuItem menuItem){
        try {
            validate(menuItem);
            if (isDuplicateNameForCreate(menuItem.getName())) {
                throw new IllegalArgumentException("Menu item name already exists.");
            }
            return menuItemDao.create(menuItem);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create menu item: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(MenuItem menuItem){
        try {
            validate(menuItem);
            if (isDuplicateNameForUpdate(menuItem.getId(), menuItem.getName())) {
                throw new IllegalArgumentException("Menu item name already exists.");
            }
            if (!menuItemDao.update(menuItem)) {
                throw new IllegalArgumentException("Menu item not found.");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot update menu item: " + e.getMessage(), e);
        }
    }
    @Override
    public void delete(int menuItemId) {
        try {
            if (!menuItemDao.delete(menuItemId)) {
                throw new IllegalArgumentException("Menu item not found.");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Cannot delete menu item: " + e.getMessage(), e);
        }
    }

    @Override
    public MenuItem findByIdItem(int id) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid ID.");
            }

            Optional<MenuItem> item = menuItemDao.findById(id);
            return item.orElse(null);

        } catch (Exception e) {
            throw new IllegalStateException("Cannot find menu item: " + e.getMessage(), e);
        }
    }

    @Override
    public List<MenuItem> searchByName(String keyword) {
        try {
            return menuItemDao.searchByName(keyword == null ? "" : keyword.trim());
        } catch (Exception e) {
            throw new IllegalStateException("Cannot search menu: " + e.getMessage(), e);
        }
    }
    @Override
    public List<MenuItem> getAll() {
        try {
            return menuItemDao.findAll();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot fetch menu: " + e.getMessage(), e);
        }
    }



    private void validate(MenuItem menuItem) {
        if (menuItem == null || InputValidator.isNotBlank(menuItem.getName())) {
            throw new IllegalArgumentException("Menu item name is required.");
        }
        if (!InputValidator.isPositivePrice(menuItem.getPrice())) {
            throw new IllegalArgumentException("Price must be positive.");
        }

        if (InputValidator.isNotBlank(menuItem.getType())) {
            throw new IllegalArgumentException("Type is required.");
        }
    }

    private boolean isDuplicateNameForCreate(String name) throws SQLException {
        String normalized = normalizeName(name);
        return menuItemDao.findAll().stream()
                .anyMatch(item -> normalizeName(item.getName()).equalsIgnoreCase(normalized));
    }

    private boolean isDuplicateNameForUpdate(int id, String name) throws SQLException {
        String normalized = normalizeName(name);
        return menuItemDao.findAll().stream()
                .anyMatch(item -> item.getId() != id && normalizeName(item.getName()).equalsIgnoreCase(normalized));
    }

    private String normalizeName(String value) {
        return value == null ? "" : value.trim();
    }

}
