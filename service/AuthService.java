package service;

import constance.Role;
import constance.UserStatus;
import dao.UserDao;
import dao.impl.UserDaoImpl;
import model.User;
import util.InputValidator;

import java.sql.SQLException;
import java.util.List;

import static util.PasswordHasher.hash;
import static util.PasswordHasher.verify;

public class AuthService {
    private final UserDao userDao = new UserDaoImpl();

    public void registerCustomer(String userName, String password){
        try{
            validateCredential(userName, password);
            if(userDao.findByUsername(userName).isPresent()){
                throw new IllegalArgumentException("Username already exists.");
            }
            User user = new User(userName.trim(), hash(password), Role.CUSTOMER, UserStatus.ACTIVE);
            userDao.create(user);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot register: " + e.getMessage(), e);
        }
    }

    public User login(String username, String rawPassword) {
        try {
            validateCredential(username, rawPassword);
            User user = userDao.findByUsername(username.trim())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));


            if (!verify(rawPassword, user.getPassword())) {
                throw new IllegalArgumentException("Invalid username or password.");
            }
            if (user.getStatus() == UserStatus.DISABLE) {
                throw new IllegalStateException("User is banned.");
            }

            return user;


        } catch (SQLException e) {
            throw new IllegalStateException("Cannot login: " + e.getMessage(), e);
        }
    }

    public void createChef(String username, String rawPassword) {
        createInternalUser(username, rawPassword, Role.CHEF);
    }

    public void createManager(String username, String rawPassword) {
        createInternalUser(username, rawPassword, Role.MANAGER);
    }

    public List<User> getCustomers() {
        try {
            return userDao.findByRole(Role.CUSTOMER);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot get customers: " + e.getMessage(), e);
        }
    }

    public List<User> getChefs() {
        try {
            return userDao.findByRole(Role.CHEF);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot get chefs: " + e.getMessage(), e);
        }
    }

    public void banUser(int userId) {
        try {
            boolean ok = userDao.updateStatus(userId, UserStatus.DISABLE.name());
            if (!ok) {
                throw new IllegalArgumentException("User not found.");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Cannot ban user: " + e.getMessage(), e);
        }
    }

    private void createInternalUser(String username, String rawPassword, Role role) {
        try {
            validateCredential(username, rawPassword);
            if (userDao.findByUsername(username).isPresent()) {
                throw new IllegalArgumentException("Username already exists.");
            }
            User user = new User(0, username.trim(), hash(rawPassword), role, UserStatus.ACTIVE);
            userDao.create(user);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot create user: " + e.getMessage(), e);
        }
    }
        private void validateCredential(String username, String rawPassword) {
        if (InputValidator.isNotBlank(username)) {
            throw new IllegalArgumentException("Username must not be empty.");
        }
        if (InputValidator.isNotBlank(rawPassword)) {
            throw new IllegalArgumentException("Password must not be empty.");
        }
    }


}
