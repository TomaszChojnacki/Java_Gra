package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.sql.Persistable;

@Getter                 // Tworzy publiczne metody getter dla każdego pola w klasie
@Setter                 // Tworzy publiczne metody setter dla każdego pola w klasie
@AllArgsConstructor     // Tworzy konstruktor z wszystkimi argumentami dla każdego pola w klasie
public class Player implements Persistable {
    private Integer id;          // Unikalny identyfikator gracza
    private String username;     // Nazwa użytkownika gracza
    private String password;     // Hasło gracza
    private Integer gamePoints;  // Punkty zdobyte przez gracza w grze (nie działa)
}
