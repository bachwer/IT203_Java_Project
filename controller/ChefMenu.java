package controller;

import model.User;

import java.util.Scanner;

public class ChefMenu {

    private static final Scanner input = new Scanner(System.in);

    public void menu(User user) {
        int choice;
        while(true) {
            System.out.println("=== Chef Menu - " +  user.getUserName() + " ===");
            System.out.println("1. View Incoming Order Items");
            System.out.println("2. Update Item Status");
            System.out.println("0. Logout");

            choice = Integer.parseInt(input.nextLine());
            switch(choice) {
                case 1 -> {}
                case 2 -> {}
                case 0 -> {
                    return;
                }
            }
        }

    }
}
