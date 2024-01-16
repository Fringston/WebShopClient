package com.example.newClientWebservice.Menu;

import com.example.newClientWebservice.DTO.*;
import com.example.newClientWebservice.Service.CartService;
import com.example.newClientWebservice.Service.UtilService;
import org.apache.hc.core5.http.ParseException;


import java.io.IOException;
import java.util.List;

import static com.example.newClientWebservice.Menu.UserMenu.userMenu;
import static com.example.newClientWebservice.Service.ArticleService.*;
import static com.example.newClientWebservice.Service.HistoryService.getAllHistory;
import static com.example.newClientWebservice.Service.UserService.getUsers;
import static com.example.newClientWebservice.Service.UtilService.*;

/**
 * Den här klassen innehåller metoder för att visa menyer för en administratör.
 *
 * @author Fredrik
 */
public class AdminMenu {

    /**
     * Den här metoden visar en meny för en administratör.
     *
     * @param jwt är en String som innehåller en JWT-token.
     */
    public static void adminChoice (String jwt, Long cartId) {
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Got to user menu");
            System.out.println("2. Go to admin menu");
            System.out.println("3. Log out");

            int choice = getIntInput("\nEnter your choice: ");

            switch (choice) {
                case 1 -> userMenu(jwt, cartId);
                case 2 -> adminMenu(jwt, cartId);
                case 3 -> MainMenu.runMeny();
                default -> {
                    System.out.println("Invalid input. Please enter a number between 1 and 3.");
                    adminChoice(jwt, cartId);
                }
            }
        }
    }


    /**
     * Den här metoden visar en meny för en administratör.
     *
     * @param jwt är en String som innehåller en JWT-token.
     */

    public static void adminMenu(String jwt, Long cartId) {
        while (true) {
            System.out.println("\nAdmin menu:\n");
            System.out.println("1. View all current carts");
            System.out.println("2. View all purchase-histories");
            System.out.println("3. View all users");
            System.out.println("4. Add article");
            System.out.println("5. Update article");
            System.out.println("6. Delete article");
            System.out.println("7. Go back");

           int choice = UtilService.getIntInput("\nEnter your choice: ");

            switch (choice) {
                case 1 -> getAllCarts(jwt);
                case 2 -> getAllHistories(jwt);
                case 3 -> getAllUsers(jwt);
                case 4 -> addNewArticle(jwt);
                case 5 -> patchArticle(jwt);
                case 6 -> removeArticleFromDB(jwt, cartId);
                case 7 -> adminChoice(jwt, cartId);
                default -> {
                    System.out.println("Invalid input. Please enter a number between 1 and 7.");
                    adminMenu(jwt, cartId);
                }
            }
       }
   }

    /**
     * Den här metoden visar alla varukorgar som finns för tillfället.
     * @param jwt är en String som innehåller en JWT-token.
     */
    public static void getAllCarts(String jwt) {
        List<Cart> carts = CartService.getAllCarts(jwt);
        if (carts == null) {
            System.out.println("Error: No carts found.");
            return;
        }
        System.out.println("\nAll current carts:\n");
        for (Cart cart : carts) {
            if (cart != null) {
                System.out.println("\u001B[4m" + "Cart ID: " + cart.getId() + "\u001B[0m" + "\nUser: " + cart.getUsername());
                if (cart.getCartItems().isEmpty()) {
                    System.out.println("Empty cart.\n");
                } else {
                    for (CartItem cartItem : cart.getCartItems()) {
                        Article article = cartItem.getArticle();
                        System.out.printf(
                                "Article ID: %d \n Article name: %s \n  Cost: %d \n  Description: %s \n Quantity: %d\n%n",
                                article.getId(), article.getName(), article.getCost(), article.getDescription(), cartItem.getQuantity()
                        );
                    }
                }
            } else {
                System.out.println("No cart found.");
            }
        }
    }

   /**
    * Den här metoden visar alla varukorgar som någonsin funnits historiskt.
    * @param jwt är en String som innehåller en JWT-token.
    */
   public static void getAllHistories(String jwt) {
       List<History> histories = getAllHistory(jwt);
       if (histories == null) {
           System.out.println("Error: No purchase histories found.");
           return;
       }
       System.out.println("\nPurchase histories:\n");
       for (History history : histories) {
           for (Article article : history.getPurchasedArticles()) {
               System.out.printf(
                       "\nHistory id: %d \n  User: %s \n  Article name: %s \n  Cost: %d \n  Description: %s \n",
                       history.getId(), history.getUser().getUsername(), article.getName(), article.getCost(), article.getDescription()
               );
           }
       }
   }

   /**
    * Denna metod visar alla användare.
    *
    * @param jwt är en String som innehåller en JWT-token.
    */
   public static void getAllUsers(String jwt) {
       List<User> users = getUsers(jwt);
       if (users == null) {
           System.out.println("Error: No users found.");
           return;
       }
       for (User user : users) {
            System.out.printf("Id: %d\n Username: %s%n",user.getId(), user.getUsername());
        }
    }

    /**
     * Denna metod lägger till en artikel.
     * @param jwt är en String som innehåller en JWT-token.
     */
    public static void addNewArticle(String jwt){
            addArticle(jwt);
    }

    /**
     * Den här metoden uppdaterar en artikel.
     *
     * @param jwt är en String som innehåller en JWT-token.
     */
   public static Void patchArticle(String jwt) {

        int id = getIntInput("Enter the id of the article you want to update: ");

        Article existingArticle = getOneArticle(id);

        if (existingArticle == null) {
            System.out.println("No article with that id exists.");
            patchArticle(jwt);

        } else {

            Article article = new Article();

            String newName = getStringInputForHttpPatch("If you want to change the name of the article. Enter the new name. Otherwise press enter:");
            if (!newName.isEmpty()) {
                article.setName(newName);
            }

            int newCost = getIntInputForHttpPatch("If you want to change the price of the article. Enter the new price. Otherwise press enter:");
            if (newCost != 0) {
                article.setCost(newCost);
            }

            String newDescription = getStringInputForHttpPatch("If you want to change the description of the article. Enter the new description. Otherwise press enter:");
            if (!newDescription.isEmpty()) {
                article.setDescription(newDescription);
            }

            return updateArticle(id, existingArticle, article, jwt);
        }
        return null;
    }

    /**
     * Den här metoden tar bort en artikel.
     *
     * @param jwt är en String som innehåller en JWT-token.
     */
    public static void removeArticleFromDB(String jwt, Long cartId) {

        int id = getIntInput("Enter the id of the article you want to delete: ");

        Article articleAboutToBeDeleted = getOneArticle(id);

        if (articleAboutToBeDeleted == null) {
            System.out.println("No article with that id exists.");
        } else {
            System.out.println("Are you sure you want to delete " + articleAboutToBeDeleted.getName() + "?");
            System.out.println("1. Yes");
            System.out.println("2. No");

            if (getIntInput("Enter your choice: ") == 1) {
            deleteArticle(id, jwt);
        } else if (getIntInput("Enter your choice: ") == 2) {
                System.out.println("The article was not deleted.");
            adminMenu(jwt, cartId);
        } else {
            System.out.println("Invalid input. Please enter a number between 1 and 2.");
            removeArticleFromDB(jwt, cartId);
        }
    }
}
}
