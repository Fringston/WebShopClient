package com.example.newClientWebservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Denna klass används för att skapa objekt av typen CartItem.
 *
 * @author Fredrik
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    private Long id;
    private Cart cart;
    private Article article;
    private int quantity;

    /**
     * Denna konstruktor används för att skapa ett objekt av typen CartItem.
     *
     * @param article  är artikeln som ska läggas till i varukorgen.
     * @param quantity är antalet av artikeln som ska läggas till i varukorgen.
     */
    public CartItem(Article article, int quantity) {
        this.article = article;
        this.quantity = quantity;
    }

    /**
     * Denna metod används för att hämta antalet av en artikel i varukorgen.
     * @return quantity är antalet artiklar.
     */
    public int getQuantity() {
        return quantity;
    }

}
