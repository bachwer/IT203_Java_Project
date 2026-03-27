package view;

public class UIView {

    public void mainMenu() {
        System.out.println("=== Restaurant Management ===");
        System.out.println("1. Register (Customer)");
        System.out.println("2. Login");
        System.out.println("0. Exit");
    }

    public void managerMenu(String user) {
        System.out.println("=== Manager Menu - " + user + " ===");
        System.out.println("1. CRUD Menu Items");
        System.out.println("2. CRUD Tables");
        System.out.println("3. Search Menu by Name");
        System.out.println("4. Approve Orders");
        System.out.println("5. Create Chef User");
        System.out.println("6. Ban Customer");
        System.out.println("7. View Reviews");
        System.out.println("0. Logout");
    }

    public void managerMenuCrud() {
        System.out.println("=== Menu Item Management ===");
        System.out.println("1. Create");
        System.out.println("2. Update");
        System.out.println("3. Delete");
        System.out.println("4. View All");
        System.out.println("0. Back");
    }

    public void managerTableCrud() {
        System.out.println("=== Table Management ===");
        System.out.println("1. Create");
        System.out.println("2. Update");
        System.out.println("3. Delete");
        System.out.println("4. View All");
        System.out.println("0. Back");
    }

    public void customerMenu(String user) {
        System.out.println("=== Customer Menu - " + user + " ===");
        System.out.println("1. View Menu");
        System.out.println("2. Choose Table & Create Order");
        System.out.println("3. Add Item to Current Order");
        System.out.println("4. Remove Item from Current Order");
        System.out.println("5. View My Orders");
        System.out.println("6. Checkout Current Order");
        System.out.println("7. Add Review");
        System.out.println("0. Logout");
    }

    public void chefMenu() {
        System.out.println("=== Chef Menu ===");
        System.out.println("1. View Incoming Order Items");
        System.out.println("2. Update Item Status");
        System.out.println("0. Logout");
    }
}
