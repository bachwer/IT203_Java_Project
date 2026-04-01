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

    List<User> getAllUsers();

    void banUser(int userId);

    void unbanUser(int userId);
}
