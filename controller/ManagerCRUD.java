package controller;

import model.MenuItem;
import model.Table;
import service.impl.MenuService;
import service.impl.TableService;
import util.CliTable;
import util.InputValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;


public class ManagerCRUD {
    private static final Scanner input = new Scanner(System.in);
    private final MenuService menuService = new MenuService();
    private final TableService tableService = new TableService();

    public void menu(String s) {
        int choice;
        while(true) {
            System.out.println("=== Menu Item ManagementL: " + s + " === ");
            System.out.println("1. Create: " + s);
            System.out.println("2. Update: " + s);
            System.out.println("3. Delete: " + s);
            System.out.println("4. View All: " + s);
            System.out.println("0. Back");

            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }
            switch(choice) {
                case 1 -> {
                    if (s.equals("MenuItem")) {

                        System.out.print("Enter the name MenuItem: ");
                        String name = input.nextLine().trim();

                        if (InputValidator.isNotBlank(name)) {
                            System.out.println("Name must not be empty!");
                            break;
                        }

                        System.out.print("Enter Price: ");
                        BigDecimal price;

                        try {
                            price = new BigDecimal(input.nextLine().trim());

                            if (!InputValidator.isPositivePrice(price)) {
                                System.out.println("Price must be greater than 0!");
                                break;
                            }

                        } catch (NumberFormatException e) {
                            System.out.println("Invalid price format!");
                            break;
                        }

                        System.out.print("Enter Type (FOOD/DRINK): ");
                        String type = input.nextLine().trim();

                        if (InputValidator.isNotBlank(type)) {
                            System.out.println("Type must not be empty!");
                            break;
                        }

                        if (!type.equalsIgnoreCase("FOOD") && !type.equalsIgnoreCase("DRINK")) {
                            System.out.println("Type must be FOOD or DRINK!");
                            break;
                        }

                        MenuItem item = new MenuItem(name, price, type);
                        System.out.println(item);
                        try {
                            menuService.create(item);
                            System.out.println("Create MenuItem success!");
                        } catch (IllegalArgumentException | IllegalStateException e) {
                            System.out.println(resolveErrorMessage(e));
                        }

                    } else if (s.equals("Table")) {

                        System.out.print("Enter the table name: ");
                        String name = input.nextLine().trim();

                        if (InputValidator.isNotBlank(name)) {
                            System.out.println("Name must not be empty!");
                            break;
                        }

                        System.out.print("Enter Capacity: ");
                        int capacity;

                        try {
                            capacity = Integer.parseInt(input.nextLine().trim());

                            if (InputValidator.isPositiveInt(capacity)) {
                                System.out.println("Capacity must be positive!");
                                break;
                            }

                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number!");
                            break;
                        }


                        Table table = new Table(name, capacity);
                        try {
                            tableService.create(table);
                            System.out.println("Create Table success!");
                        } catch (IllegalArgumentException | IllegalStateException e) {
                            System.out.println(resolveErrorMessage(e));
                        }
                    }
                }
                case 2 -> {
                    if (s.equals("MenuItem")) {
                        System.out.print("Enter ID: ");
                        int id;
                        try {
                            id = Integer.parseInt(input.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid ID!");
                            break;
                        }

                        MenuItem oldItem = menuService.findByIdItem(id);
                        if (oldItem == null) {
                            System.out.println("MenuItem not found!");
                            break;
                        }

                        System.out.print("Enter new name: ");
                        String name = input.nextLine().trim();
                        if (InputValidator.isNotBlank(name)) {
                            System.out.println("Name must not be empty!");
                            break;
                        }

                        System.out.print("Enter new price: ");
                        BigDecimal price;
                        try {
                            price = new BigDecimal(input.nextLine().trim());
                            if (!InputValidator.isPositivePrice(price)) {
                                System.out.println("Price must be greater than 0!");
                                break;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid price!");
                            break;
                        }

                        System.out.print("Enter new type (FOOD/DRINK): ");
                        String type = input.nextLine().trim();
                        if (InputValidator.isNotBlank(type)) {
                            System.out.println("Type must not be empty!");
                            break;
                        }

                        MenuItem item = new MenuItem();
                        item.setId(oldItem.getId());
                        item.setName(name);
                        item.setPrice(price);
                        item.setType(type);
                        item.setStatus(oldItem.getStatus());
                        try {
                            menuService.update(item);
                            System.out.println("Update MenuItem success!");
                        } catch (IllegalArgumentException | IllegalStateException e) {
                            System.out.println(resolveErrorMessage(e));
                        }

                    } else if (s.equals("Table")) {
                        System.out.print("Enter ID: ");
                        int id;
                        try {
                            id = Integer.parseInt(input.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid ID!");
                            break;
                        }

                        Table oldTable = tableService.findByIdTable(id);
                        if (oldTable == null) {
                            System.out.println("Table not found!");
                            break;
                        }

                        System.out.print("Enter new name: ");
                        String name = input.nextLine().trim();
                        if (InputValidator.isNotBlank(name)) {
                            System.out.println("Name must not be empty!");
                            break;
                        }

                        System.out.print("Enter new capacity: ");
                        int capacity;
                        try {
                            capacity = Integer.parseInt(input.nextLine().trim());
                            if (InputValidator.isPositiveInt(capacity)) {
                                System.out.println("Capacity must be positive!");
                                break;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid capacity!");
                            break;
                        }

                        Table table = new Table();
                        table.setId(oldTable.getId());
                        table.setName(name);
                        table.setCapacity(capacity);
                        table.setStatus(oldTable.getStatus());
                        try {
                            tableService.update(table);
                            System.out.println("Update Table success!");
                        } catch (IllegalArgumentException | IllegalStateException e) {
                            System.out.println(resolveErrorMessage(e));
                        }
                    }
                }
                case 3 -> {
                    System.out.print("Enter ID to delete: ");
                    int id;
                    try {
                        id = Integer.parseInt(input.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID!");
                        break;
                    }

                    if (s.equals("MenuItem")) {
                        menuService.delete(id);
                        System.out.println("Delete MenuItem success!");
                    } else if (s.equals("Table")) {
                        tableService.delete(id);
                        System.out.println("Delete Table success!");
                    }
                }
                case 4 -> {
                    if (s.equals("MenuItem")) {
                        List<String[]> rows = menuService.getAll().stream()
                                .map(MenuItem::toTableRow)
                                .toList();
                        CliTable.print("MENU ITEMS", MenuItem.tableHeaders(), rows, 4);
                    } else if (s.equals("Table")) {
                        List<String[]> rows = tableService.getAll().stream()
                                .map(Table::toTableRow)
                                .toList();
                        CliTable.print("TABLES", Table.tableHeaders(), rows, 3);
                    }
                }

                case 0 -> {
                    return;
                }
                default ->  System.out.println("invalid input !");
            }
        }
    }

    private String resolveErrorMessage(Exception e) {
        if (e.getMessage() != null && !e.getMessage().isBlank()) {
            return e.getMessage();
        }
        return "Operation failed.";
    }
}
