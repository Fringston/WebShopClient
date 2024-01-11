package com.example.newClientWebservice.Menu;

import com.example.newClientWebservice.Service.UserService;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

/**
 * Den här klassen innehåller en metod för att registrera en ny användare.
 *
 * @author Fredrik
 */
public class Register {

    /**
     * Den här metoden används för att registrera en ny användare.
     * @throws IOException kastar ett undantag om det blir problem med inläsning från användaren.
     * @throws ParseException kastar ett undantag om det blir problem med parsning av JSON.
     */
    public static void register() throws IOException, ParseException {
        UserService.register();
    }
}
