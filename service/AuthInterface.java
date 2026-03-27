package service;

import model.User;

import java.util.List;

public interface AuthInterface {
    void registerCustomer(String userName, String password);

    User login(String username, String rawPassword);

    void createChef(String username, String rawPassword);

    void createManager(String username, String rawPassword);

    List<User> getCustomers();

    List<User> getChefs();

    void banUser(int userId);
}
