package util;

import java.util.List;
import java.util.Scanner;

public final class CliUi {
    private CliUi() {
    }

    public static void printMenu(String title, List<String> options) {
        printSeparator();
        System.out.println(CliColor.title(title));
        printSeparator();
        for (String option : options) {
            System.out.println(CliColor.info(option));
        }
        printSeparator();
    }

    public static void printPrompt(String text) {
        System.out.print(CliColor.header(text));
    }

    public static void printSection(String text) {
        System.out.println();
        System.out.println(CliColor.title(text));
    }

    public static void success(String message) {
        System.out.println(CliColor.success("[OK] " + message));
    }

    public static void info(String message) {
        System.out.println(CliColor.info("[INFO] " + message));
    }

    public static void warning(String message) {
        System.out.println(CliColor.warning("[WARN] " + message));
    }

    public static void error(String message) {
        System.out.println(CliColor.error("[ERROR] " + message));
    }

    public static void pause(Scanner scanner) {
        if (scanner == null) {
            return;
        }
        System.out.print(CliColor.header("Press Enter to continue..."));
        scanner.nextLine();
    }

    private static void printSeparator() {
        System.out.println(CliColor.header("============================================================"));
    }
}
