package com.example.newClientWebservice.Service;

import com.example.newClientWebservice.DTO.History;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 *  Denna klass hämtar användarens köphistorik från databasen
 *
 * @author jafar
 */
public class HistoryService {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    /**
     * Denna metod hämtar all köphistorik från databasen.
     *
     * @param jwt är en string som är en token som används för att autentisera användaren
     * @return histories är en arraylist av History objekt
     * */
    public static ArrayList<History> getAllHistory(String jwt) {

        HttpGet request = new HttpGet("http://localhost:8081/webshop/history");

        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getCode() != 200) {
                System.out.println("Error");
                return null;
            }

            HttpEntity entity = response.getEntity();

            ObjectMapper mapper = new ObjectMapper();
           return mapper.readValue(EntityUtils.toString(entity), new TypeReference<ArrayList<History>>() {
            });

//            System.out.println("Histories:");
//            // skriv ut alla köphistorik
//            for (History history : histories) {
//                System.out.println(String.format(
//                        "  History id: %d \n totalCost: %d \n article: %s \n user: %s",
//                        history.getId(), history.getTotalCost(), history.getPurchasedArticles(), history.getUser().getUsername()
//                ));
//            }

//            return histories;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Denna metod hämtar köphistorik från databasen från den aktuella användaren
     *
     * @param jwt är en string som är en token som används för att autentisera användaren
     * @return articles är en arraylist av Article objekt, det är alla artiklar som köpts av den aktuella användaren
     * */
    public static ArrayList<History> getCurrentUserHistory(String jwt) {
        HttpGet request = new HttpGet("http://localhost:8081/webshop/history/currentUserHistory");

        // inkludera en authorization metod till request
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        // Exekvera request
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getCode() != 200) {
                System.out.println("Error");
                return null;
            }

            // visar upp response payload i console
            HttpEntity entity = response.getEntity();

            // konvertera response payload till ett användbart objekt
            ObjectMapper mapper = new ObjectMapper();
           return mapper.readValue(EntityUtils.toString(entity), new TypeReference<ArrayList<History>>() {
            });

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
