package com.example.pacman.LeaderBoard;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Read {
    private static final String SQL_SELECT_ALL = "SELECT * FROM leaderboard ORDER BY score DESC LIMIT 10";

    public List<Leaderboard> getTopLeaderboards(Connection connection) throws SQLException {
        List<Leaderboard> leaderboards = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);
            while (resultSet.next()) {
                String playerName = resultSet.getString("playerName");
                int score = resultSet.getInt("score");
                int level = resultSet.getInt("level");
                leaderboards.add(new Leaderboard(playerName, score,level));
            }
        }
        return leaderboards;
    }
}