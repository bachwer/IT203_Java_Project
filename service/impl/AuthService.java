package service.impl;

import constance.Role;
import constance.UserStatus;
import dao.UserDao;
import dao.impl.UserDaoImpl;
import model.User;
import service.AuthInterface;
import util.InputValidator;

import java.sql.SQLException;
import java.util.List;

import static util.PasswordHasher.hash;
import static util.PasswordHasher.verify;

public class AuthService implements AuthInterface {
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public void registerCustomer(String userName, String password){
        try{
            validateCredential(userName, password);
            String normalizedUserName = userName.trim();
            if(userDao.findByUsername(normalizedUserName).isPresent()){
                throw new IllegalArgumentException("Username already exists.");
            }

            User user = new User(normalizedUserName, hash(password), Role.CUSTOMER, UserStatus.ACTIVE);
            userDao.create(user);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot register: " + e.getMessage(), e);
        }
    }
    @Override
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
    @Override
    public void createChef(String username, String rawPassword) {
        createInternalUser(username, rawPassword, Role.CHEF);
    }
    @Override
    public void createManager(String username, String rawPassword) {
        createInternalUser(username, rawPassword, Role.MANAGER);
    }
    @Override
    public List<User> getCustomers() {
        try {
            return userDao.findByRole(Role.CUSTOMER);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot get customers: " + e.getMessage(), e);
        }
    }
    @Override
    public List<User> getChefs() {
        try {
            return userDao.findByRole(Role.CHEF);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot get chefs: " + e.getMessage(), e);
        }
    }
    @Override
    public void banUser(int userId) {
        try {
            User user = userDao.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found."));

            if (user.getRole() != Role.CUSTOMER) {
                throw new IllegalArgumentException("Only customer accounts can be banned.");
            }

            if (user.getStatus() == UserStatus.DISABLE) {
                throw new IllegalArgumentException("User is already banned.");
            }

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
            String normalizedUserName = username.trim();
            if (userDao.findByUsername(normalizedUserName).isPresent()) {
                throw new IllegalArgumentException("Username already exists.");
            }
            User user = new User(0, normalizedUserName, hash(rawPassword), role, UserStatus.ACTIVE);
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
        if (rawPassword.trim().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
    }
}
