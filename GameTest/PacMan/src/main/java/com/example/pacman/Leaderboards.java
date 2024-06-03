package com.example.pacman;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Leaderboards {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/your_database_name";
    private static final String DB_USER = "your_username";
    private static final String DB_PASSWORD = "your_password";

    // Method to initialize the database
    public Leaderboards() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS leaderboard (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "player_name VARCHAR(255) NOT NULL," +
                        "score INT NOT NULL," +
                        "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ")";
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Method to add a new score
    public void addScore(String playerName, int score) {
        String sql = "INSERT INTO leaderboard (player_name, score) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            pstmt.setInt(2, score);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Method to get the top scores
    public List<String> getTopScores(int limit) {
        List<String> topScores = new ArrayList<>();
        String sql = "SELECT player_name, score, date FROM leaderboard ORDER BY score DESC LIMIT ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String entry = "Player: " + rs.getString("player_name") +
                        " - Score: " + rs.getInt("score") +
                        " - Date: " + rs.getTimestamp("date");
                topScores.add(entry);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return topScores;
    }
}