package org.example.security;

import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.sql.SqlService.getConnection;

@Log4j2
public class Authorization {

    // Metoda sprawdzająca, czy dane logowania (nazwa użytkownika i hasło) są prawidłowe
    public static boolean isValidLogin(String username, String password) {
        try (Connection connection = getConnection()) {     // Ustanowienie połączenia z bazą danych

            // Zapytanie SQL sprawdzające, czy istnieje gracz z podanym użytkownikiem i hasłem
            String query = "SELECT COUNT(*) FROM player WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {

                // Ustawienie parametrów zapytania: nazwy użytkownika i hasła
                statement.setString(1, username);
                statement.setString(2, password);

                try (ResultSet resultSet = statement.executeQuery()) {      // Wykonanie zapytania
                    if (resultSet.next()) {                                 // Jeśli istnieje wynik zapytania
                        int count = resultSet.getInt(1);        // Pobranie liczby znalezionych rekordów
                        return count > 0;                             // Zwraca 0, jeśli znaleziono co najmniej jeden rekord
                    }
                }
            }
        } catch (SQLException e) {                  // Obsługa wyjątków związanych z bazą danych
            log.error(e.getMessage());              // Logowanie błędu
            e.printStackTrace();                    // Wyświetlenie śladu stosu

        }
        return false;                 // Zwraca false, jeśli logowanie jest nieprawidłowe
    }
}

