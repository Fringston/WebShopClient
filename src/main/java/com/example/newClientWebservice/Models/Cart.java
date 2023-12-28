package com.example.newClientWebservice.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

        private Long id;
        private User user;
        private Set<Article> articles;
        private String username;
        public Cart(Long id) {
                this.id = id;
        }

        public static Long getCartIdFromUser(LoginResponse loginResponse) {
                if (loginResponse != null && loginResponse.getUser() != null) {
                        return loginResponse.getUser().getCart().getId();
                }
                return null;
        }

}
