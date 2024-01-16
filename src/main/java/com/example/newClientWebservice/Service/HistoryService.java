package com.example.newClientWebservice.Service;

import com.example.newClientWebservice.DTO.History;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
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
 * Denna klass hämtar användarens köphistorik från databasen
 *
 * @author jafar
 */
public class HistoryService {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    /**
     * Denna metod hämtar all köphistorik från databasen.
     *
     * @param jwt är en string som är en token som används för att autentisera användaren
     * @return en arraylist av History objekt
     */
    public static ArrayList<History> getAllHistory(String jwt) {

        try {
            HttpGet request = new HttpGet("http://localhost:8081/webshop/history");
            request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

            try (CloseableHttpResponse response = httpClient.execute(request)) {

                if (response.getCode() != 200) {
                    System.out.println("Error occurred. HTTP response code: " + response.getCode());
                    return null;
                }

                HttpEntity entity = response.getEntity();

                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(EntityUtils.toString(entity), new TypeReference<ArrayList<History>>() {
                });
            } catch (JsonMappingException e) {
                System.out.println("Mapping Error: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO Error: " + e.getMessage());
            } catch (ParseException e) {
                System.out.println("Parse Error: " + e.getMessage());
            }
            return null;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }


    /**
     * Denna metod hämtar köphistorik från databasen från den aktuella användaren
     *
     * @param jwt är en string som är en token som används för att autentisera användaren
     * @return en arraylist av Article objekt, det är alla artiklar som köpts av den aktuella användaren
     */
    public static ArrayList<History> getCurrentUserHistory(String jwt) {

        try {
            HttpGet request = new HttpGet("http://localhost:8081/webshop/history/currentUserHistory");

            request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getCode() != 200) {
                    System.out.println("Error");
                    return null;
                }

                HttpEntity entity = response.getEntity();

                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(EntityUtils.toString(entity), new TypeReference<ArrayList<History>>() {});

            } catch (JsonMappingException e) {
                System.out.println("Mapping Error: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO Error: " + e.getMessage());
            } catch (ParseException e) {
                System.out.println("Parse Error: " + e.getMessage());
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}
