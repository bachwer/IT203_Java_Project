package model;

import constance.MenuItemStatus;

import java.math.BigDecimal;

public class MenuItem {
    private int id;
    private BigDecimal price;
    private String name;
    private String type;
    private MenuItemStatus status;


    public MenuItem() {
    }

    public MenuItem(int id, BigDecimal price, String name, String type, MenuItemStatus status) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.type = type;
        this.status = status;
    }

    public MenuItem( String name, BigDecimal price, String type) {
        this.price = price;
        this.name = name;
        this.type = type;
    }

    public MenuItem(int id, String name, BigDecimal price, String type) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(MenuItemStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public MenuItemStatus getStatus() {
        return status;
    }

    public static String[] tableHeaders() {
        return new String[]{"ID", "Name", "Price", "Type", "Status"};
    }

    public String[] toTableRow() {
        return new String[]{
                String.valueOf(id),
                name.toUpperCase(),
                price == null ? "" : price.toPlainString(),
                type,
                status == null ? "" : status.name()
        };
    }



    @Override
    public String toString() {
        return String.format(
                "%-5d | %-20s | %-10s | %-10s | %-10s",
                id,
                name,
                price == null ? "" : price.toPlainString(),
                type,
                status
        );
    }

}
