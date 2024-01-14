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

/**
 * Denna klass används för att skapa en meny för användare.
 * Här kan användaren välja att lägga till, ta bort och uppdatera artiklar i kundkorgen.
 * Den innefattar metoder som anropar metoder från olika Service-klasser.
 * @author Clara Brorson
 */
public class UserMenu {

    public static void userMenu(String jwt, Long cartId) throws IOException, ParseException {

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
     * @throws IOException om det blir fel med inläsning av data.
     * @throws ParseException om det blir fel med parsning av data.
     */
    private static void addFruitToCart(String jwt, Long cartId) throws IOException, ParseException {
        printArticlesMenu();
        int articleId = getIntInput("\nEnter the article ID-number of a fruit to add to the basket: ");
        int quantity = getIntInput("Enter the quantity of the fruit to add to the basket: ");

        List<Article> articles = ArticleService.getAllArticles();
        assert articles != null;
        Article selectedArticle = articles.stream()
                .filter(article -> article.getId() == articleId)
                .findFirst()
                .orElse(null);

        if (selectedArticle != null) {
            CartService.addArticleToCart(Math.toIntExact(cartId), Math.toIntExact(selectedArticle.getId()), quantity, jwt);
        } else {
            System.out.println("Invalid article number. Please try again.");
        }
    }

    private static void viewCart(String jwt, Long cartId) throws IOException, ParseException {

        Cart cart = getOneCartById(Math.toIntExact(cartId), jwt);

        if (cart != null) {
            System.out.printf("\nCart %d belongs to %s and contains:%n", cart.getId(), cart.getUsername());
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

    private static void deleteFruitFromCart(String jwt, Long cartId) throws IOException, ParseException {
        int articleId = getIntInput("Enter the article ID: ");

        if (CartService.controlIfArticleExistsInCart(Math.toIntExact(cartId), articleId, jwt)) {

            CartService.deleteArticleFromCart(Math.toIntExact(cartId), articleId, jwt);
        } else {

            System.out.println("Error: Article does not exist in the cart.");
        }
    }

    private static void updateFruitQuantity(String jwt, Long cartId) throws IOException, ParseException {
        int articleId = getIntInput("Enter the article ID: ");
        int quantity = getIntInput("Enter the new quantity: ");

        if (CartService.controlIfArticleExistsInCart(Math.toIntExact(cartId), articleId, jwt)) {
            CartService.updateArticleCount(Math.toIntExact(cartId), quantity, articleId, jwt);
            System.out.println("Article quantity updated successfully in the cart.");
        } else {
            System.out.println("Error: Article does not exist in the cart.");
        }
    }

    private static void getHistory(String jwt) {
        List<History> histories = getCurrentUserHistory(jwt);
        System.out.println("\nPurchased Articles:\n");
        assert histories != null;
        for (History history : histories) {
            for (Article article : history.getPurchasedArticles()) {
                System.out.printf(
                        "History id: %d\n User: %s\n article id: %d\n name: %s\n cost: %d\n description: %s\n%n",
                        history.getId(), history.getUser().getUsername(), article.getId(), article.getName(), article.getCost(), article.getDescription()
                );
            }
            System.out.printf("Total cost: %d%n", history.getTotalCost());
        }
    }

    private static void purchaseCart(String jwt) {
        CartService.purchaseArticles(jwt);
    }
}