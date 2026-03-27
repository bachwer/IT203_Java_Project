package controller;

import model.User;
import service.impl.OrderService;
import service.impl.ReviewService;

import java.util.Scanner;

public class CustomerMenu {
    private final OrderService orderService = new OrderService();
    private final ReviewService reviewService = new ReviewService();

    private static final Scanner input = new Scanner(System.in);
    public void menu(User user) {
        int choice;

        while(true){
            System.out.println("=== Customer Menu - " + user.getUserName() + " ===");
            System.out.println("1. View Menu");
            System.out.println("2. Choose Table & Create Order");
            System.out.println("3. Add Item to Current Order");
            System.out.println("4. Remove Item from Current Order");
            System.out.println("5. View My Orders");
            System.out.println("6. Checkout Current Order");
            System.out.println("7. Add Review");
            System.out.println("0. Logout");


            choice = Integer.parseInt(input.nextLine());
            switch(choice){
                case 1 -> {}
                case 2 -> {}
                case 3 -> {}
                case 4 -> {}
                case 5 -> {}
                case 6 -> {}
                case 7 -> {}
                case 0 -> {return;}
            }
        }

    }
}
