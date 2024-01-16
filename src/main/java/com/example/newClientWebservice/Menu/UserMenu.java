package com.example.newClientWebservice.Menu;

import com.example.newClientWebservice.DTO.*;
import com.example.newClientWebservice.Service.ArticleService;
import com.example.newClientWebservice.Service.CartService;
import org.apache.hc.core5.http.ParseException;
import java.io.IOException;
import java.util.List;

import static com.example.newClientWebservice.Menu.ArticlesMenu.printArticlesMenu;
import static com.example.newClientWebservice.Service.CartService.getOneCartById;
import static com.example.newClientWebservice.Service.HistoryService.getCurrentUserHistory;
import static com.example.newClientWebservice.Service.UtilService.getIntInput;
import static com.example.newClientWebservice.Service.UtilService.getLongInput;

/**
 * Denna klass används för att skapa en meny för användare.
 * Här kan användaren välja att lägga till, ta bort och uppdatera artiklar i kundkorgen.
 * Den innefattar metoder som anropar metoder från olika Service-klasser.
 *
 * @author Clara Brorson
 */
public class UserMenu {

    public static void userMenu(String jwt, Long cartId) {

        while (true) {
            System.out.println("\nWelcome to Fruit Haven!");
            System.out.println("1. View all fruits");
            System.out.println("2. Add a fruit to the basket");
            System.out.println("3. View basket");
            System.out.println("4. Remove a fruit from the basket");
            System.out.println("5. Want more fruits? Update the quantity of a fruit in the basket");
            System.out.println("6. History of purchases");
            System.out.println("7. Ready to checkout? Proceed to checkout");
            System.out.println("8. Log out\n");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> printArticlesMenu();
                case 2 -> addFruitToCart(jwt, cartId);
                case 3 -> viewCart(jwt, cartId);
                case 4 -> deleteFruitFromCart(jwt, cartId);
                case 5 -> updateFruitQuantity(jwt, cartId);
                case 6 -> getHistory(jwt);
                case 7 -> purchaseCart(jwt);
                case 8 -> {
                    MainMenu.runMeny();
                    return;
                }
                default -> {
                    System.out.println("Invalid input. Please enter a number between 1 and 7.");
                    userMenu(jwt, cartId);
                }
            }
        }
    }

    /**
     * Samtliga metoder nedanför anropas i userMenu-metoden.
     *
     * @param jwt är en sträng som används för att autentisera användaren.
     * @param cartId är id:t för den kundkorg som användaren har.
     */

    private static void addFruitToCart(String jwt, Long cartId) {
        printArticlesMenu();
        int articleId = getIntInput("\nEnter the article ID-number of a fruit to add to the basket: ");
        int quantity = getIntInput("Enter the quantity of the fruit to add to the basket: ");

        List<Article> articles = ArticleService.getAllArticles();
        if (articles == null) {
            System.out.println("Error: No articles found.");
            return;
        }
        Article selectedArticle = articles.stream()
                .filter(article -> article.getId() == articleId)
                .findFirst()
                .orElse(null);

        if (selectedArticle != null) {
            CartService.addArticleToCart(cartId, selectedArticle.getId(), quantity, jwt);
        } else {
            System.out.println("Invalid article number. Please try again.");
        }
    }

    private static void viewCart(String jwt, Long cartId) {

        Cart cart = getOneCartById(cartId, jwt);

        if (cart != null) {
            System.out.printf("\nCart %d belongs to %s and contains:\n", cart.getId(), cart.getUsername());
            for (CartItem cartItem : cart.getCartItems()) {
                Article article = cartItem.getArticle();
                System.out.printf(" Article id: %d\n Article: %s \n Price: %d \n Description: %s \n Quantity: %d\n%n",
                        article.getId(), article.getName(), article.getCost(), article.getDescription(), cartItem.getQuantity());
            }
            System.out.printf("Total cost: %d%n", cart.getTotalCost());
        } else {
            System.out.println("Cart not found or an error occurred.");
        }
    }

    private static void deleteFruitFromCart(String jwt, Long cartId) {

        Long articleId = getLongInput("Enter the article ID: ");

        if (CartService.controlIfArticleExistsInCart(cartId, articleId, jwt)) {

            CartService.deleteArticleFromCart(cartId, articleId, jwt);
        } else {

            System.out.println("Error: Article does not exist in the cart.");
        }
    }

    private static void updateFruitQuantity(String jwt, Long cartId) {

        Long articleId = getLongInput("Enter the article ID: ");
        int quantity = getIntInput("Enter the new quantity: ");

        if (CartService.controlIfArticleExistsInCart(cartId, articleId, jwt)) {
            CartService.updateArticleCount(cartId, quantity, articleId, jwt);
            System.out.println("Article quantity updated successfully in the cart.");
        } else {
            System.out.println("Error: Article does not exist in the cart.");
        }
    }

    private static void getHistory(String jwt) {
        List<History> histories = getCurrentUserHistory(jwt);
        if (histories == null || histories.isEmpty()) {
            System.out.println("No purchase history found.");
            return;
        }
        System.out.println("\nPurchased articles:");
        for (History history : histories) {
            for (Article article : history.getPurchasedArticles()) {
                System.out.printf(
                        "\nHistory id: %d\n User: %s\n Article name: %s\n Cost: %d\n Description: %s\n",
                        history.getId(), history.getUser().getUsername(), article.getName(), article.getCost(), article.getDescription()
                );
            }
            System.out.printf("Total cost: %d\n", history.getTotalCost());
        }
    }

    private static void purchaseCart(String jwt) {
        CartService.purchaseArticles(jwt);
    }
}