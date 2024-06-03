package com.example.pacman.LeaderBoard;

import java.sql.*;

public class Create {
    private static final String SQL_INSERT = "INSERT INTO leaderboard (playerName, score, level) VALUES (?, ?, ?)";

    public int insertLeaderboard(Connection connection, String playerName, int score, int level) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, playerName);
            statement.setInt(2, score);
            statement.setInt(3, level);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected");
            }

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}