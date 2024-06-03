package com.example.pacman.LeaderBoard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Update {
    private static final String SQL_UPDATE = "UPDATE leaderboard SET playerName = ?, score = ?, level = ? WHERE id = ?";

    public void updateLeaderboard(Connection connection, int id, String name, int score, int level) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setString(1, name);
            statement.setInt(2, score);
            statement.setInt(3, level);
            statement.setInt(4, id);

            statement.executeUpdate();
        }
    }
}
