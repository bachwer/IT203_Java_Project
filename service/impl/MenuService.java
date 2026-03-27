package service.impl;

import dao.MenuItemDao;
import dao.impl.MenuItemDaoImpl;
import model.MenuItem;
import service.MenuInterface;
import util.InputValidator;

import java.util.List;


public class MenuService implements MenuInterface {
    private final MenuItemDao menuItemDao = new MenuItemDaoImpl();
    @Override
    public int create(MenuItem menuItem){
        try{
            validate(menuItem);
            return menuItemDao.create(menuItem);
        }catch (Exception e) {
            throw new IllegalStateException("Cannot create menu item: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(MenuItem menuItem){
        try {
            validate(menuItem);
            if (!menuItemDao.update(menuItem)) {
                throw new IllegalArgumentException("Menu item not found.");
            }
        } catch (Exception e) {
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
        if (menuItem == null || !InputValidator.isNotBlank(menuItem.getName())) {
            throw new IllegalArgumentException("Menu item name is required.");
        }
        if (!InputValidator.isPositivePrice(menuItem.getPrice())) {
            throw new IllegalArgumentException("Price must be positive.");
        }

        if (!InputValidator.isNotBlank(menuItem.getType())) {
            throw new IllegalArgumentException("Type is required.");
        }
    }

}
