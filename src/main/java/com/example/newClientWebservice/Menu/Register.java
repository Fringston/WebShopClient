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
     */
    public static void register(){
        UserService.register();
    }
}
