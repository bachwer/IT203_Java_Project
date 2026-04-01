package model;

import constance.Role;
import constance.UserStatus;

public class User {
    private int id;
    private String userName;
    private String password;
    private Role role;
    private UserStatus status;


    public User() {
    }


    public User(int id, String userName, String password, Role role, UserStatus status) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public User(String trim, String hash, Role role, UserStatus userStatus) {
        this.userName = trim;
        this.password = hash;
        this.role = role;
        this.status = userStatus;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public static String[] tableHeaders() {
        return new String[]{"ID", "Username", "Role", "Status"};
    }

    public String[] toTableRow() {
        return new String[]{
                String.valueOf(id),
                userName,
                role.name(),
                status.name()
        };
    }
}
