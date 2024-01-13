package com.example.newClientWebservice.Service;

import com.example.newClientWebservice.DTO.LoginResponse;
import com.example.newClientWebservice.DTO.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import static com.example.newClientWebservice.Service.CartService.getCartIdFromUser;
import static com.example.newClientWebservice.Service.UtilService.*;
/**
 * Denna klass innehåller metoder för att utföra operationer på user-tabellen i databasen.
 * Metoderna är kopplade till olika endpoints i WebService-applikationen.
 *
 * @author Jafar Hussein
 */
public class UserService {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    /**
     * Denna metod hämtar alla användare från databasen, endast användare med admin roll kan använda den
     *
     * @param jwt är en string som är en token som används för att autentisera användaren
     * @return är en arraylist av User objekt
     * */
    public static List<User> getUsers(String jwt) throws IOException, ParseException { // för admin
    // skapa ett objekt av http get klassen
            HttpGet request = new HttpGet("http://localhost:8081/webshop/user");
    // inkludera en authorization metod till request
            request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
    // exekvera request
            CloseableHttpResponse response = httpClient.execute(request);
            // visa upp response payload i console
            if (response.getCode() != 200) {
                System.out.println("Something went wrong");
                return null;
            }

            // visa upp response payload i console
            HttpEntity entity = response.getEntity();
        // skapa ett objekt av ObjectMapper klassen
            ObjectMapper mapper = new ObjectMapper();
            // skapar en arraylist av User objekt för att kunna loopa igenom och skriva ut alla users
            return mapper.readValue(EntityUtils.toString(entity), new TypeReference<ArrayList<User>>() {});
    }

    /**
     * Denna metod skapar en ny användare.
     * Alla användare, även oregistrerade användare har tillgång till denna metod.
     */
    public static void register()throws IOException, ParseException{
        //Skapar ett username och password
        String username = getStringInput("Enter username: ");
        String password = getPasswordInput("Enter your password: ");
        //Skapar ett user objekt och sparar username och password i det
        User newUser = new User(0L, username, password);
        //Skapar en nytt request
        HttpPost request = new HttpPost("http://localhost:8081/webshop/auth/register");
        //Skapa en payload
        request.setEntity(createPayload(newUser));
        //Skicka request
        CloseableHttpResponse response = httpClient.execute(request);
        //Om response code inte är 200 så har något gått fel
        if (response.getCode() != 200){
            System.out.println("Something went wrong");
            return;
        }
        //Hämta payload från response
        HttpEntity payload = response.getEntity();
        //Skapa ett user objekt från payload
        ObjectMapper mapper = new ObjectMapper();
        //Skriv ut att användaren har skapats
        User responseUser = mapper.readValue(EntityUtils.toString(payload), new TypeReference<User>() {});

        System.out.println(String.format("User %s has been created with the user-id: %d",responseUser.getUsername(), responseUser.getId()));
    }

    /**
     * Denna metod loggar in en användare, alla registrerade användare kan använda den
     *
     *  @return ett objekt av LoginResponse klassen
     */
    public static LoginResponse login() throws IOException, ParseException{
        // skapa ett username och password
        String username = getStringInput("Enter username ");
        String password = getPasswordInput("Enter your password ");
        //Skapa user objekt
        User loginUser = new User(0L, username, password);

        //skapa ett nytt request
        HttpPost request = new HttpPost("http://localhost:8081/webshop/auth/login");
        //skapa en payload
        request.setEntity(createPayload(loginUser));

        //send request
        CloseableHttpResponse response = httpClient.execute(request);
        if (response.getCode() != 200){
            System.out.println("Something went wrong");
            return null;
        }

        //hämta Payload från response
        HttpEntity payload = response.getEntity();

        //skapa User objekt från payload
        ObjectMapper mapper = new ObjectMapper();
        LoginResponse loginResponse = mapper.readValue(EntityUtils.toString(payload), new TypeReference<LoginResponse>() {});
        if (loginResponse.getUser() == null) {
            System.out.println("Wrong username or password. Please try again.");
            return null;
        }
        System.out.println(String.format("\nUser: %s has logged in", loginResponse.getUser().getUsername()));

        return loginResponse;
    }
}
