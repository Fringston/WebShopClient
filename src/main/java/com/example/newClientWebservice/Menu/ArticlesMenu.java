package com.example.newClientWebservice.Menu;

import com.example.newClientWebservice.DTO.Article;
import com.example.newClientWebservice.Service.ArticleService;

import java.util.List;
/**
 * @author Jafar Hussein
 * Denna klass skriver ut alla artiklar från databasen
 *
 */
public class ArticlesMenu {

    /**
     * Skriver ut alla artiklar från databasen med hjälp av getAllArticles() från ArticleService
     * använde mig av en for loop för att skriva ut alla artiklar från databasen
     * skapar en int articleNumber för att kunna skriva ut artikel nummer för varje artikel
     * varje loop ökar articleNumber med 1
     */
    public static void printArticlesMenu() {
        System.out.println("\nArticles Menu \n");
        List<Article> articles = ArticleService.getAllArticles();
        if (articles == null) {
            System.out.println("Error: No articles found.");
            return;
        }
        for (Article article : articles) {
            System.out.printf("Article id: %d\n Article: %s \n Price: %d \n Description: %s \n%n",
                     article.getId(), article.getName(), article.getCost(), article.getDescription());
        }
    }


}
