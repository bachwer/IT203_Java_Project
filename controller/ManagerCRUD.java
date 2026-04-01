package controller;

import model.MenuItem;
import model.Table;
import service.impl.MenuService;
import service.impl.TableService;
import util.CliTable;
import util.CliUi;
import util.InputValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;


public class ManagerCRUD {
    private static final Scanner input = new Scanner(System.in);
    private final MenuService menuService = new MenuService();
    private final TableService tableService = new TableService();

    public void menu(String s) {
        int choice;
        while(true) {
            CliUi.printMenu(("MANAGER CRUD - " + s).toUpperCase(Locale.ROOT), List.of(
                    "1. Create " + s,
                    "2. Update " + s,
                    "3. Delete " + s,
                    "4. View All " + s,
                    "0. Back"
            ));

            Integer selected = InputValidator.readInt(input, "Select option: ");
            if (selected == null) {
                CliUi.error("Invalid input! Please enter a number.");
                continue;
            }
            choice = selected;
            switch(choice) {
                case 1 -> {
                    if (s.equals("MenuItem")) {

                        String name = InputValidator.readNonBlank(input, "Enter menu name: ");

                        if (InputValidator.isNotBlank(name)) {
                            CliUi.warning("Name must not be empty!");
                            break;
                        }

                        BigDecimal price = InputValidator.readBigDecimal(input, "Enter price: ");

                        if (price == null) {
                            CliUi.warning("Invalid price format!");
                            break;
                        }
                        if (!InputValidator.isPositivePrice(price)) {
                            CliUi.warning("Price must be greater than 0!");
                            break;
                        }

                        String type = InputValidator.readNonBlank(input, "Enter type (FOOD/DRINK): ");

                        if (InputValidator.isNotBlank(type)) {
                            CliUi.warning("Type must not be empty!");
                            break;
                        }

                        if (!type.equalsIgnoreCase("FOOD") && !type.equalsIgnoreCase("DRINK")) {
                            CliUi.warning("Type must be FOOD or DRINK!");
                            break;
                        }

                        MenuItem item = new MenuItem(name, price, type);
                        try {
                            menuService.create(item);
                            CliUi.success("Create menu item success!");
                        } catch (IllegalArgumentException | IllegalStateException e) {
                            CliUi.error(resolveErrorMessage(e));
                        }

                    } else if (s.equals("Table")) {

                        String name = InputValidator.readNonBlank(input, "Enter table name: ");

                        if (InputValidator.isNotBlank(name)) {
                            CliUi.warning("Name must not be empty!");
                            break;
                        }

                        Integer capacity = InputValidator.readInt(input, "Enter capacity: ");
                        if (capacity == null) {
                            CliUi.warning("Invalid number!");
                            break;
                        }

                        if (InputValidator.isPositiveInt(capacity)) {
                            CliUi.warning("Capacity must be positive!");
                            break;
                        }


                        Table table = new Table(name, capacity);
                        try {
                            tableService.create(table);
                            CliUi.success("Create table success!");
                        } catch (IllegalArgumentException | IllegalStateException e) {
                            CliUi.error(resolveErrorMessage(e));
                        }
                    }
                }
                case 2 -> {
                    if (s.equals("MenuItem")) {
                        Integer id = InputValidator.readInt(input, "Enter ID: ");
                        if (id == null) {
                            CliUi.warning("Invalid ID!");
                            break;
                        }

                        MenuItem oldItem = menuService.findByIdItem(id);
                        if (oldItem == null) {
                            CliUi.warning("Menu item not found!");
                            break;
                        }

                        String name = InputValidator.readNonBlank(input, "Enter new name: ");
                        if (InputValidator.isNotBlank(name)) {
                            CliUi.warning("Name must not be empty!");
                            break;
                        }

                        BigDecimal price = InputValidator.readBigDecimal(input, "Enter new price: ");
                        if (price == null) {
                            CliUi.warning("Invalid price!");
                            break;
                        }
                        if (!InputValidator.isPositivePrice(price)) {
                            CliUi.warning("Price must be greater than 0!");
                            break;
                        }

                        String type = InputValidator.readNonBlank(input, "Enter new type (FOOD/DRINK): ");
                        if (InputValidator.isNotBlank(type)) {
                            CliUi.warning("Type must not be empty!");
                            break;
                        }
                        if (!type.equalsIgnoreCase("FOOD") && !type.equalsIgnoreCase("DRINK")) {
                            CliUi.warning("Type must be FOOD or DRINK!");
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
                            CliUi.success("Update menu item success!");
                        } catch (IllegalArgumentException | IllegalStateException e) {
                            CliUi.error(resolveErrorMessage(e));
                        }

                    } else if (s.equals("Table")) {
                        Integer id = InputValidator.readInt(input, "Enter ID: ");
                        if (id == null) {
                            CliUi.warning("Invalid ID!");
                            break;
                        }

                        Table oldTable = tableService.findByIdTable(id);
                        if (oldTable == null) {
                            CliUi.warning("Table not found!");
                            break;
                        }

                        String name = InputValidator.readNonBlank(input, "Enter new name: ");
                        if (InputValidator.isNotBlank(name)) {
                            CliUi.warning("Name must not be empty!");
                            break;
                        }

                        Integer capacity = InputValidator.readInt(input, "Enter new capacity: ");
                        if (capacity == null) {
                            CliUi.warning("Invalid capacity!");
                            break;
                        }
                        if (InputValidator.isPositiveInt(capacity)) {
                            CliUi.warning("Capacity must be positive!");
                            break;
                        }

                        Table table = new Table();
                        table.setId(oldTable.getId());
                        table.setName(name);
                        table.setCapacity(capacity);
                        table.setStatus(oldTable.getStatus());
                        try {
                            tableService.update(table);
                            CliUi.success("Update table success!");
                        } catch (IllegalArgumentException | IllegalStateException e) {
                            CliUi.error(resolveErrorMessage(e));
                        }
                    }
                }
                case 3 -> {
                    Integer id = InputValidator.readInt(input, "Enter ID to delete: ");
                    if (id == null) {
                        CliUi.warning("Invalid ID!");
                        break;
                    }

                    String confirm = InputValidator.readNonBlank(input, "Confirm delete (Y/N): ");
                    if (!InputValidator.isYes(confirm)) {
                        CliUi.info("Delete cancelled.");
                        break;
                    }

                    if (s.equals("MenuItem")) {
                        try {
                            menuService.delete(id);
                            CliUi.success("Delete menu item success!");
                        } catch (IllegalArgumentException | IllegalStateException e) {
                            CliUi.error(resolveErrorMessage(e));
                        }
                    } else if (s.equals("Table")) {
                        try {
                            tableService.delete(id);
                            CliUi.success("Delete table success!");
                        } catch (IllegalArgumentException | IllegalStateException e) {
                            CliUi.error(resolveErrorMessage(e));
                        }
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
                default -> CliUi.warning("Invalid option!");
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
