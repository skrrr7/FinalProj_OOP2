package com.example.pacman.LeaderBoard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Truncate {
    private static final String SQL_DELETE = "TRUNCATE leaderboard";

    public void clearAll(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.executeUpdate();
        }
    }
}