package model;

import constance.TableStatus;

public class Table {
    private int id;
    private String name;
    private int capacity;
    private TableStatus status;


    public Table() {
    }

    public Table(int id, String name, int capacity, TableStatus status) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.status = status;
    }

    public Table(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public Table(int id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public TableStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format("%-5d | %-10s | %-5d | %-10s",
                id, name, capacity, status);
    }
}
