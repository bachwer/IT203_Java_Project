package service;


import dao.MenuItemDao;
import dao.OrderDao;
import dao.OrderItemDao;
import dao.TableDao;
import dao.impl.MenuItemDaoImpl;
import dao.impl.OrderDaoImpl;
import dao.impl.OrderItemDaoImpl;
import dao.impl.TableDaoImpl;


public class OrderService {
    private final OrderDao orderDao = new OrderDaoImpl();
    private final OrderItemDao orderItemDao = new OrderItemDaoImpl();
    private final MenuItemDao menuItemDao = new MenuItemDaoImpl();
    private final TableDao tableDao = new TableDaoImpl();





}
