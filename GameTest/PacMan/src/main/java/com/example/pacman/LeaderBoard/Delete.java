package com.example.pacman.LeaderBoard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Delete {
    private static final String SQL_DELETE = "DELETE FROM leaderboard WHERE id = ?";
    private static final String SQL_DELETE_ALL = "DELETE FROM leaderboard";

    public void deleteLeaderboard(Connection connection, int id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public void deleteAll(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_ALL)) {
            statement.executeUpdate();
        }
    }
}
