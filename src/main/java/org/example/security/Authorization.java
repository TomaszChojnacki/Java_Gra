package org.example.security;

import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.sql.SqlService.getConnection;

@Log4j2
public class Authorization {
    public static boolean isValidLogin(String username, String password) {
        try (Connection connection = getConnection()) {
            String query = "SELECT COUNT(*) FROM player WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            e.printStackTrace();

        }
        return false;
    }


}

