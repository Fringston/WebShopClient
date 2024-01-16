package com.example.newClientWebservice.Service;

import com.example.newClientWebservice.DTO.Cart;
import com.example.newClientWebservice.DTO.CartItem;
import com.example.newClientWebservice.DTO.LoginResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;


/**
 * Denna klass används som en service för att utföra crud operationer på cart-tabellen i databasen.
 * Metoderna använder sig av HTTP-requests för att skicka förfrågningar till API:et.
 * Url:en specificeras i varje metod och överensstämmer med de endpoints som finns i API:et.
 * Metoderna returnerar ett svar från API:et i form av en String.
 *
 * @author Clara Brorson
 */
public class CartService {

    /**
     * CloseableHttpClient används för att skicka HTTP-requests till API:et.
     * httpClient är en instans av CloseableHttpClient.
     */
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    /**
     * Denna metod används för att hämta alla carts från API:et.
     * @param jwt är en String som innehåller en JWT-token.
     */
    public static List<Cart> getAllCarts(String jwt) {

        try {
        HttpGet request = new HttpGet("http://localhost:8081/webshop/cart");
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getCode() != 200) {
                System.out.println("Error occurred. HTTP response code: " + response.getCode());
                return null;
            }

            HttpEntity entity = response.getEntity();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(EntityUtils.toString(entity), new TypeReference<List<Cart>>() {});

        } catch (JsonMappingException e) {
            System.out.println("Json Mapping Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        } catch (ParseException e) {
            System.out.println("Parse Error: " + e.getMessage());
        } return null;

    } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } return null;
    }

    /**
     * Denna metod används för att hämta en cart med ett specifikt id från API:et.
     * @param id är id:t för den cart som ska hämtas.
     * @param jwt är en String som innehåller en JWT-token.
     */
    public static Cart getOneCartById(Long id, String jwt) {

        try {
        HttpGet request = new HttpGet(String.format("http://localhost:8081/webshop/cart/%s", id));
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getCode() != 200) {
                System.out.println("Error occurred. HTTP response code: " + response.getCode());
                return null;
            }

            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(responseBody, new TypeReference<Cart>() {});

        } catch (JsonMappingException e) {
            System.out.println("Json Mapping Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        } catch (ParseException e) {
            System.out.println("Parse Error: " + e.getMessage());
        } return null;

    } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } return null;
    }

    /**
     * Denna metod används för att lägga till en artikel i en cart.
     * @param cartId är id:t för den cart som artikeln ska läggas till i.
     * @param articleId är id:t för den artikel som ska läggas till.
     * @param jwt är en String som innehåller en JWT-token.
     */
    public static void addArticleToCart(Long cartId, Long articleId, int quantity, String jwt) {

        try {
        HttpPost request = new HttpPost(String.format("http://localhost:8081/webshop/cart/%s", articleId));
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        JSONObject json = new JSONObject();
        json.put("quantity", quantity);

        StringEntity entity = new StringEntity(json.toString());
        request.setEntity(entity);

        try (CloseableHttpResponse response = httpClient.execute(request)) {

        if (response.getCode() != 200) {
            System.out.println("Error occurred. HTTP response code: " + response.getCode());
            return;
        }

        HttpEntity responseEntity = response.getEntity();

        ObjectMapper mapper = new ObjectMapper();
        Cart cart = mapper.readValue(EntityUtils.toString(responseEntity), new TypeReference<Cart>() {});
        System.out.printf("Article %s added to cart %s\n", articleId, cartId);

    } catch (JsonMappingException e) {
        System.out.println("Json Mapping Error: " + e.getMessage());
    } catch (IOException e) {
        System.out.println("IO Error: " + e.getMessage());
    } catch (ParseException e) {
        System.out.println("Parse Error: " + e.getMessage());
        }
    } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Denna metod används för att uppdatera antalet artiklar i en cart.
     *
     */
    public static void updateArticleCount(Long cartId, int quantity, Long articleId, String jwt) {

        try {
        HttpPatch request = new HttpPatch(String.format("http://localhost:8081/webshop/cart/%s/articles/%s/quantity/%d", cartId, articleId, quantity));
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        try (CloseableHttpResponse response = httpClient.execute(request)) {

        if (response.getCode() != 200) {
            System.out.println("Error occurred. HTTP response code: " + response.getCode());
            return;
        }

        HttpEntity entity = response.getEntity();

        ObjectMapper mapper = new ObjectMapper();
        Cart cart = mapper.readValue(EntityUtils.toString(entity), new TypeReference<Cart>() {});

        System.out.printf("Quantity of article %s has been updated to %d in cart %s\n", articleId, quantity, cartId);

        } catch (JsonMappingException e) {
        System.out.println("Json Mapping Error: " + e.getMessage());
    } catch (IOException e) {
        System.out.println("IO Error: " + e.getMessage());
    } catch (ParseException e) {
        System.out.println("Parse Error: " + e.getMessage());
        }

    } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Denna metod används för att ta bort en artikel från en cart.
     * @param cartId är id:t för den cart som artikeln ska tas bort från.
     * @param articleId är id:t för den artikel som ska tas bort.
     * @param jwt är en String som innehåller en JWT-token.
     */
    public static void deleteArticleFromCart(Long cartId, Long articleId, String jwt) {

        try {
        HttpDelete request = new HttpDelete(String.format("http://localhost:8081/webshop/cart/%s/articles/%s", cartId, articleId));
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        try (CloseableHttpResponse response = httpClient.execute(request)) {

        if (response.getCode() != 200) {
            System.out.println("Error occurred. HTTP response code: " + response.getCode());
            return;
        }

        HttpEntity entity = response.getEntity();

        ObjectMapper mapper = new ObjectMapper();
        Cart cart = mapper.readValue(EntityUtils.toString(entity), new TypeReference<Cart>() {});

        System.out.printf("Article %s has been deleted from cart %s\n", articleId, cartId);

    } catch (JsonMappingException e) {
        System.out.println("Json Mapping Error: " + e.getMessage());
    } catch (IOException e) {
        System.out.println("IO Error: " + e.getMessage());
    } catch (ParseException e) {
        System.out.println("Parse Error: " + e.getMessage());
        }

    } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Denna metod används för att genomföra ett köp.
     * @param jwt är en String som innehåller en JWT-token.
     */
    public static void purchaseArticles(String jwt) {

        try {
            HttpPost request = new HttpPost("http://localhost:8081/webshop/history/purchase");
            request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getCode() != 200) {
                    System.out.println("Error occurred. HTTP response code: " + response.getCode());
                    return;
                }
                System.out.println("Purchase completed successfully.");
            }
            catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("IO Error: " + e.getMessage());
        }
    }


    /**
     * Denna metod används för att kontrollera om en artikel redan finns i en cart.
     * @param cartId är id:t för den cart som artikeln ska kontrolleras i.
     * @param articleId är id:t för den artikel som ska kontrolleras.
     * @param jwt är en String som innehåller en JWT-token.
     * @return true om artikeln finns i carten, annars false.
     */
    public static boolean controlIfArticleExistsInCart(Long cartId, Long articleId, String jwt) {

        try {
            Cart cart = getOneCartById(cartId, jwt);

            if (cart != null && cart.getCartItems() != null) {
                for (CartItem cartItem : cart.getCartItems()) {
                    if (cartItem.getArticle().getId().equals(articleId)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Den här metoden tar emot ett LoginResponse-objekt och returnerar CartId
     * @param loginResponse LoginResponse-objekt som innehåller ett User-objekt som i sin tur innehåller ett Cart-objekt
     * @return CartId som är en Long eller null om CartId inte finns
     */
    public static Long getCartIdFromUser(LoginResponse loginResponse) {
        if (loginResponse != null && loginResponse.getUser() != null) {
            return loginResponse.getUser().getCart().getId();
        }
        return null;
    }

}