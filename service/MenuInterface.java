package service;

import model.MenuItem;
import java.util.List;

public interface MenuInterface {

    int create(MenuItem menuItem);

    void update(MenuItem menuItem);

    void delete(int menuItemId);

    List<MenuItem> searchByName(String keyword);

    List<MenuItem> getAll();
}