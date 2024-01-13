package com.example.newClientWebservice.Menu;

import com.example.newClientWebservice.Service.UtilService;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
/**
 * Denna klass är en hub som leder till andra klasser
 *
 * @author Jafar
 **/

public class MainMenu {
   private static boolean isRunning = true; // global variabel så att vi kan använda den för att stänga av programmet

    /**
     * Denna metod som beter sig som en hub för att leda till andra metoder
     * @throws IOException ParseException är exceptions som kastas om det blir fel
     */

    public static void runMeny() throws IOException, ParseException {
        while (isRunning) {
                printMainMenu();
            int choice = UtilService.getIntInput("Enter your choice: ");
            userChoice(choice);

        }
    }

    /**
     * Denna metod skriver ut Main Menyn
     */
    private static void printMainMenu() {
        String [] menuItems = {"1. Login", "2. Register","3. View articles", "4. Exit"};
        for (String menuItem : menuItems) {
            System.out.println(menuItem);
        }
    }

    /**
     * Denna metod för en switch case som leder till andra metoder
     *
     * @param choice är en int variabel som tar emot användarens val
     */

    public static void userChoice(int choice) throws IOException, ParseException {
        switch (choice) {
            case 1 -> LoginMenu.loginUser();
            case 2 -> Register.register();
            case 3 -> ArticlesMenu.printArticlesMenu();
            case 4 -> {
                System.out.println("Exiting...");
                isRunning = false;
                System.exit(0);
            }
            default -> System.out.println("Invalid input. Please enter a number between 1 and 4.");
        }
    }
}
