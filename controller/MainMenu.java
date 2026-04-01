package controller;

import util.CliUi;

import java.util.List;

public class MainMenu {
    public void menu() {
        CliUi.printMenu("RESTAURANT MANAGEMENT", List.of(
                "1. Register (Customer)",
                "2. Login",
                "0. Exit"
        ));
    }
}
