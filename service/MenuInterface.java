package service;

import model.MenuItem;
import java.util.List;

public interface MenuInterface {

    int create(MenuItem menuItem);

    void update(MenuItem menuItem);

    void delete(int menuItemId);

    MenuItem findByIdItem(int id);

    List<MenuItem> searchByName(String keyword);

    List<MenuItem> getAll();
}