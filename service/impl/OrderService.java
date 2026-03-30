package service.impl;


import constance.OrderStatus;
import constance.OrderStatusItem;
import constance.TableStatus;
import constance.MenuItemStatus;
import dao.MenuItemDao;
import dao.OrderDao;
import dao.OrderItemDao;
import dao.TableDao;
import dao.impl.MenuItemDaoImpl;
import dao.impl.OrderDaoImpl;
import dao.impl.OrderItemDaoImpl;
import dao.impl.TableDaoImpl;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import service.OrderInterface;
import util.InputValidator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class OrderService implements OrderInterface {
    private final OrderDao orderDao = new OrderDaoImpl();
    private final OrderItemDao orderItemDao = new OrderItemDaoImpl();
    private final MenuItemDao menuItemDao = new MenuItemDaoImpl();
    private final TableDao tableDao = new TableDaoImpl();

    @Override
    public int create(int userId, int tableId){
        try {
            boolean userHasActiveOrder = orderDao.findByUserId(userId).stream()
                    .anyMatch(order -> !order.isApproved());
            if (userHasActiveOrder) {
                throw new IllegalArgumentException("You already have a current order.");
            }

            Optional<model.Table> tableOpt = tableDao.findById(tableId);
            if (tableOpt.isEmpty()) {
                throw new IllegalArgumentException("Table does not exist.");
            }

            if (tableOpt.get().getStatus() != TableStatus.AVAILABLE) {
                throw new IllegalArgumentException("Table is not available.");
            }

            boolean tableHasActiveOrder = orderDao.findAll().stream()
                    .anyMatch(order -> order.getTableId() == tableId && !order.isApproved());
            if (tableHasActiveOrder) {
                throw new IllegalArgumentException("Table already has an active order.");
            }

            Order order = new Order(0, userId, tableId, OrderStatus.CheckIn, false, LocalDateTime.now());
            int orderId = orderDao.create(order);
            if (orderId <= 0) {
                throw new IllegalStateException("Failed to create order.");
            }
            tableDao.updateStatus(tableId, TableStatus.OCCUPIED.name());
            return orderId;
        } catch (Exception e) {
            throw new IllegalStateException("Cannot create order: " + e.getMessage(), e);
        }
    }
    @Override
    public void addOrderItem(int orderId, int menuItemId, int quantity) {
        try {
            if (InputValidator.isPositiveInt(quantity)) {
                throw new IllegalArgumentException("Quantity must be positive.");
            }
            Optional<MenuItem> menuItemOpt = menuItemDao.findById(menuItemId);
            if (menuItemOpt.isEmpty()) {
                throw new IllegalArgumentException("Menu item does not exist.");
            }
            if (menuItemOpt.get().getStatus() != MenuItemStatus.AVAILABLE) {
                throw new IllegalArgumentException("Menu item is not available.");
            }

            orderItemDao.create(new OrderItem(0, orderId, menuItemId, null, quantity, OrderStatusItem.PENDING));
        } catch (Exception e) {
            throw new IllegalStateException("Cannot add order item: " + e.getMessage(), e);
        }
    }
    @Override
    public boolean removePendingOrderItem(int orderItemId) {
        try {
            return orderItemDao.removePendingItem(orderItemId);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot remove item: " + e.getMessage(), e);
        }
    }
    @Override
    public List<OrderItem> getOrderItems(int orderId) {
        try {
            return orderItemDao.findByOrderId(orderId);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot get order items: " + e.getMessage(), e);
        }
    }

    @Override
    public List<OrderItem> findIncomingForChef() {
        try {
            return orderItemDao.findIncomingForChef();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot get incoming order items: " + e.getMessage(), e);
        }
    }



    @Override
    public List<Order> getOrdersByUser(int userId) {
        try {
            return orderDao.findByUserId(userId);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot get orders: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Order> getCurrentOrderByUser(int userId) {
        try {
            return orderDao.findByUserId(userId).stream()
                    .filter(order -> !order.isApproved())
                    .findFirst();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot get current order: " + e.getMessage(), e);
        }
    }


    @Override
    public List<Order> getAllOrders() {
        try {
            return orderDao.findAll();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot get orders: " + e.getMessage(), e);
        }
    }

    @Override
    public void saveOrderTotal(int orderId, BigDecimal total) {
        try {
            if (total == null || total.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Order total is invalid.");
            }

            if (!orderDao.updateTotal(orderId, total)) {
                throw new IllegalArgumentException("Order not found.");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Cannot save order total: " + e.getMessage(), e);
        }
    }

    @Override
    public BigDecimal getTotalRevenue() {
        try {
            return orderDao.getTotalRevenue();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot get total revenue: " + e.getMessage(), e);
        }
    }

    @Override
    public int countCheckedOutOrders() {
        try {
            return orderDao.countCheckedOutOrders();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot count checked out orders: " + e.getMessage(), e);
        }
    }


    @Override
    public void approveOrder(int orderId) {
        try {
            Optional<Order> orderOpt = orderDao.findById(orderId);
            if (orderOpt.isEmpty()) {
                throw new IllegalArgumentException("Order not found.");
            }

            Order order = orderOpt.get();
            if (order.isApproved()) {
                throw new IllegalArgumentException("Order already checked out.");
            }

            List<OrderItem> items = orderItemDao.findByOrderId(orderId);
            if (items.isEmpty()) {
                throw new IllegalArgumentException("Cannot checkout order without items.");
            }

            boolean hasNotReadyItem = items.stream().anyMatch(item ->
                    item.getStatus() == OrderStatusItem.PENDING || item.getStatus() == OrderStatusItem.PROCESSING);
            if (hasNotReadyItem) {
                throw new IllegalArgumentException("Cannot checkout. Some items are not completed yet.");
            }

            boolean statusUpdated = orderDao.updateStatus(orderId, OrderStatus.CheckOuted);
            boolean approvedUpdated = orderDao.updateApproval(orderId, true);
            if (!statusUpdated || !approvedUpdated) {
                throw new IllegalArgumentException("Order not found.");
            }

            tableDao.updateStatus(order.getTableId(), TableStatus.AVAILABLE.name());
        } catch (Exception e) {
            throw new IllegalStateException("Cannot approve order: " + e.getMessage(), e);
        }
    }
    @Override
    public void advanceOrderItemStatus(int orderItemId, OrderStatusItem nextStatus) {
        try {
            OrderStatusItem current = findOrderItem(orderItemId).getStatus();
            if (!isNextStatusValid(current, nextStatus)) {
                throw new IllegalArgumentException("Cannot skip status steps.");
            }
            orderItemDao.updateStatus(orderItemId, nextStatus);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot update item status: " + e.getMessage(), e);
        }
    }
    @Override
    public OrderItem findOrderItem(int orderItemId) {
        try {
            for (Order order : orderDao.findAll()) {
                List<OrderItem> items = orderItemDao.findByOrderId(order.getId());
                for (OrderItem item : items) {
                    if (item.getId() == orderItemId) {
                        return item;
                    }
                }
            }
            throw new IllegalArgumentException("Order item not found.");
        } catch (Exception e) {
            throw new IllegalStateException("Cannot find order item: " + e.getMessage(), e);
        }
    }


    private boolean isNextStatusValid(OrderStatusItem current, OrderStatusItem next) {
        switch (current) {
            case PENDING:
                return next == OrderStatusItem.PROCESSING || next == OrderStatusItem.CANCELLED;
            case PROCESSING:
                return next == OrderStatusItem.COMPLETED || next == OrderStatusItem.CANCELLED;
            default:
                return false;
        }
    }

}
