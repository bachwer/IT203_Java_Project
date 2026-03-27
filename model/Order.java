package model;

import constance.OrderStatus;

import java.time.LocalDateTime;

public class Order {
    private int id;
    private int userId;
    private int tableId;
    private OrderStatus status;
    private boolean approved;
    private LocalDateTime createdAt;

    public Order() {
    }

    public Order(int userId, int tableId, OrderStatus status, boolean approved) {
        this.userId = userId;
        this.tableId = tableId;
        this.status = status;
        this.approved = approved;
    }

    public Order(int id, int userId, int tableId, OrderStatus status, boolean approved, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.tableId = tableId;
        this.status = status;
        this.approved = approved;
        this.createdAt = createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getTableId() {
        return tableId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public boolean isApproved() {
        return approved;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
