package com.example.newClientWebservice.Menu;

import com.example.newClientWebservice.Models.Article;
import com.example.newClientWebservice.Models.Cart;
import com.example.newClientWebservice.Models.History;
import com.example.newClientWebservice.Models.LoginResponse;
import com.example.newClientWebservice.Service.ArticleService;
import com.example.newClientWebservice.Service.CartService;
import com.example.newClientWebservice.Service.HistoryService;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.example.newClientWebservice.Menu.ArticlesMenu.printArticlesMenu;
import static com.example.newClientWebservice.Service.CartService.getOneCartById;
import static com.example.newClientWebservice.Service.HistoryService.getCurrentUserHistory;
import static com.example.newClientWebservice.Service.UtilService.getIntInput;

public class UserMenu {

    public static void userMenu(String jwt) throws IOException, ParseException {

        while (true) {
            System.out.println("Welcome to Fruit Haven!");
            System.out.println("1. View all fruits");
            System.out.println("2. Add a fruit to the basket");
            System.out.println("3. View basket");
            System.out.println("4. Remove a fruit from the basket");
            System.out.println("5. Want more fruits? Update the quantity of a fruit in the basket");
            System.out.println("6. History of purchases");
            System.out.println("7. Ready to checkout? Proceed to checkout");
            System.out.println("8. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    printArticlesMenu();
                    break;
                case 2:
                    addFruitToCart(jwt);
                    break;
                case 3:
                    viewCart(jwt);
                    break;
                case 4:
                    deleteFruitFromCart(jwt);
                    break;
                case 5:
                    updateFruitQuantity(jwt);
                    break;
                case 6:
                    getHistory(jwt);
                    break;
                case 7:
                    purchaseCart(jwt);
                    break;
                case 8:
                    MainMenu.runMeny();
                    return;
                default:
                    System.out.println("Invalid input. Please enter a number between 1 and 7.");
                    userMenu(jwt);
                    break;
            }
        }
    }
    private static void addFruitToCart(String jwt) throws IOException, ParseException {
        printArticlesMenu();
        int articleNumber = getIntInput("Enter the article number of a fruit to add to the basket: ");

        // Hämtar artiklar från databasen
        List<Article> articles = ArticleService.getAllArticles();
        if (articleNumber > 0 && articleNumber <= articles.size()) {
            Article selectedArticle = articles.get(articleNumber - 1);
            int cartId = getIntInput("Enter the cart ID: ");

            // Lägger till artiklar i Cart
            CartService.addArticleToCart(cartId, Math.toIntExact(selectedArticle.getId()), jwt);
        } else {
            System.out.println("Invalid article number. Please try again.");
        }
    }

    private static void viewCart(String jwt) throws IOException, ParseException {
        int cartId = getIntInput("Enter the cart ID: ");
        getOneCartById(cartId, jwt);
    }
    private static void deleteFruitFromCart(String jwt) throws IOException, ParseException {
        int cartId = getIntInput("Enter the cart ID: ");
        int articleId = getIntInput("Enter the article ID: ");
        CartService.deleteArticleFromCart(cartId, articleId, jwt);
    }

    private static void updateFruitQuantity(String jwt) throws IOException, ParseException {
        int cartId = getIntInput("Enter the cart ID: ");
        int articleId = getIntInput("Enter the article ID: ");
        int quantity = getIntInput("Enter the new quantity: ");
        CartService.updateArticleCount(cartId, articleId, quantity, jwt);
    }

    private static void getHistory(String jwt) throws IOException, ParseException {
        List<History> histories = getCurrentUserHistory(jwt);
        System.out.println("\nPurchased Articles:\n");
        for (History history : histories) {
            for (Article article : history.getPurchasedArticles()) {
                System.out.println(String.format(
                        "id: %d \n  User: %s \n  name: %s \n  cost: %d \n  description: %s \n  quantity: %d \n  Total cost: %d",
                        history.getId(), history.getUser().getUsername(), article.getName(), article.getCost(), article.getDescription(), article.getQuantity(), history.getTotalCost()
                ));
            }
        }
    }

    private static void purchaseCart(String jwt) throws IOException, ParseException {
        CartService.purchaseArticles(jwt);
    }




}


