package com.example.newClientWebservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Denna klass används för att skapa ett login-svar som innehåller en användare och en JWT.
 * User-objektet innehåller användarens id, användarnamn och roller.
 * JWT är en sträng som används för att autentisera användaren.
 *
 * @author Clara Brorson
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
        private User user;
        private String jwt;
}
