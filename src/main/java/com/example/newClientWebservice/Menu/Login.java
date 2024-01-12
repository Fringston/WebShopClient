package com.example.newClientWebservice.Menu;

import com.example.newClientWebservice.DTO.LoginResponse;
import com.example.newClientWebservice.DTO.Role;
import com.example.newClientWebservice.DTO.User;
import org.apache.hc.core5.http.ParseException;
import java.io.IOException;
import static com.example.newClientWebservice.DTO.Cart.getCartIdFromUser;
import static com.example.newClientWebservice.Service.UserService.login;

/**
 * Denna klass används för att skapa en meny för inloggning.
 *
 * @author Clara Brorson
 */

public class Login {

    /**
     * Denna metod används för att skapa en meny för inloggning.
     * Den anropar metoden login i UserService-klassen för att logga in användaren.
     * Användaren får tillbaka ett LoginResponse-objekt som innehåller ett JWT och en User.
     * LoginResponse-objektet skickas in i metoden getCartIdFromUser i Cart-klassen.
     * En if sats kontrollerar om användaren har admin-roll genom att anropa metoden isAdmin.
     * Beroende på om användaren har admin-roll eller inte, visas antingen admin-menyn eller användarmenyn.
     */
    public static void loginMenu() throws IOException, ParseException {
        LoginResponse loginResponse = login();
        Long cartId = getCartIdFromUser(loginResponse);

        if (cartId != null) {
            System.out.println("Ready to go shopping? Don't forget your Cart ID: " + cartId);

            if (isAdmin(loginResponse.getUser())) {
                AdminMenu.adminChoice(loginResponse.getJwt());
            } else {
                UserMenu.userMenu(loginResponse.getJwt());
            }
        } else {
            System.out.println("Something went wrong. Please try again.");
        }
    }

    /**
     * Denna metod används för att kontrollera om en användare har admin-rollen.
     *
     * @param user är en User som ska kontrolleras.
     * @return true om användaren har admin-rollen, annars false.
     */
    private static boolean isAdmin(User user) {
        if (user != null && user.getAuthorities() != null) {
            for (Role role : user.getAuthorities()) {
                if ("admin".equalsIgnoreCase(role.getAuthority())) {
                    return true;
                }
            }
        }
        return false;
    }
}
