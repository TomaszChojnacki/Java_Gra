package org.example.sql;

import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
public class SqlService {

    // Metoda do wstawiania obiektu do bazy danych
    protected static void insert(Persistable persistableObject) {
        try (Connection conn = getConnection()) {

            // Tworzenie zapytania SQL na podstawie nazw pól i wartości obiektu
            String tableName = persistableObject.getClass().getSimpleName().toLowerCase();
            StringBuilder columnNames = new StringBuilder();
            StringBuilder placeholders = new StringBuilder();
            List<Object> values = new ArrayList<>();
            for (Field field : persistableObject.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                columnNames.append(field.getName()).append(", ");
                placeholders.append("?, ");
                values.add(field.get(persistableObject));

            }

            // Usunięcie ostatniego przecinka z listy nazw kolumn i symboli zastępczych
            columnNames.setLength(columnNames.length() - 2);
            placeholders.setLength(placeholders.length() - 2);

            String sql = "INSERT INTO " + tableName + " (" + columnNames + ") VALUES (" + placeholders + ")";
            log.info(sql);  // Logowanie zapytania SQL

            // Wykonanie zapytania SQL
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                for (int i = 0; i < values.size(); i++) {

                    // Obsługa różnych typów wartości
                    if (values.get(i) instanceof Object[]) {
                        statement.setArray(i + 1, conn.createArrayOf("VARCHAR", (Object[]) values.get(i)));
                    } else {
                        statement.setObject(i + 1, values.get(i));
                    }
                }
                statement.executeUpdate();
            }
        } catch (Exception e) {
            log.error(e.getMessage()); // Zapisanie do logu gdy wywali blad "napisac zeby do pliku"
            e.printStackTrace();
        }
    }


    public static void update(Persistable persistable) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            Class<?> clazz = persistable.getClass();
            String tableName = clazz.getSimpleName();
            Field[] fields = clazz.getDeclaredFields();
            StringBuilder updateQuery = new StringBuilder("UPDATE " + tableName + " SET ");
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldValue;
                try {
                    fieldValue = field.get(persistable);
                    updateQuery.append(fieldName).append(" = '").append(fieldValue).append("', ");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            updateQuery.deleteCharAt(updateQuery.length() - 2);
            updateQuery.append(" WHERE id = ").append(persistable.getId());
            statement.executeUpdate(updateQuery.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
    protected static void delete(Persistable persistable) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            Class<?> clazz =persistable.getClass();
            String tableName = clazz.getSimpleName();
            String deleteQuery = "DELETE FROM " + tableName + " WHERE id = " + persistable.getId();
            statement.executeUpdate(deleteQuery);
            log.info("Document deleted");
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    // Metoda do nawiązywania połączenia z bazą danych
    public static Connection getConnection() throws SQLException {
        String host = System.getenv("DB_HOST");
        String port = System.getenv("DB_PORT");
        String database = System.getenv("DB_DATABASE");
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");
        String connectionUrl = "jdbc:postgresql://" + host + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        return DriverManager.getConnection(connectionUrl, username, password);
    }

}
