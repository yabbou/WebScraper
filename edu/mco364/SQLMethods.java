package edu.mco364;

import java.sql.*;
import java.util.Set;

class SQLMethods {
    private String connectionUrl;
    private String database;
    private String table;

    SQLMethods(String connectionUrl) {
        this(connectionUrl, "lastName364", "tableName");
    }

    SQLMethods(String connectionUrl, String database, String table) {
        this.connectionUrl = connectionUrl;
        this.database = database;
        this.table = table;

        createDatabase(database);
        createTableAndColumns(table);
    }

    void uploadEmailsToDatabase(Set<String> totalEmails) {
        if (totalEmails != null) {
            String insertSql = repeatedInsert(totalEmails);

            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedInsert = connection.prepareStatement(insertSql)) {
                preparedInsert.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String repeatedInsert(Set<String> totalEmails) {
        StringBuilder statement = new StringBuilder(String.format("INSERT INTO %s.%s (%s) VALUES ", database, table, table.toLowerCase()));
        for (String email : totalEmails) {
            statement.append(String.format("(%s),", email));
        }
        return statement.deleteCharAt(statement.length() - 1).toString();
    }

    private void createDatabase(String database) {
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement()) {

            String createDatabaseSql = "CREATE DATABASE " + database;
            ResultSet resultSet = statement.executeQuery(createDatabaseSql);

            if (resultSet.next()) {
                System.out.println("Database exists.");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void createTableAndColumns(String table) {
        final int LOCAL_PART_MAX = 64;
        final int DOMAIN_MAX = 255;
        final int PUNCTUATION = 2;

        String createTableSql = String.format("CREATE TABLE %s.guest.%s \n" +
                "  %s varchar(%d) not null,\n", database, table, table.toLowerCase(), LOCAL_PART_MAX + DOMAIN_MAX + PUNCTUATION);

        try (Connection connection = DriverManager.getConnection(connectionUrl);
             PreparedStatement preparedInsert = connection.prepareStatement(createTableSql)) {
            preparedInsert.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
