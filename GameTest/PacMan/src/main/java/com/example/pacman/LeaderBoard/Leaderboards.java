package com.example.pacman.LeaderBoard;

import com.example.pacman.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Leaderboards {
    private static String DB_URL;
    public static String USERNAME;
    public static String PASSWORD;

    public Leaderboards() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DB_URL = Config.getInstance().DB_URL;
            USERNAME = Config.getInstance().DB_USER;
            PASSWORD = Config.getInstance().DB_PASSWORD;


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS leaderboard (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "playerName VARCHAR(255) NOT NULL," +
                        "score INT NOT NULL," +
                        "level INT NOT NULL," +
                        "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ")";
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void create(String playerName, int score, int rank) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            Create create = new Create();
            create.insertLeaderboard(connection, playerName, score, rank);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Leaderboard> getTopScores(int limit) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            Read read = new Read();
            return read.getTopLeaderboards(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void update(Connection connection1, int id, String playerName, int score,int level) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            Update update = new Update();
            update.updateLeaderboard(connection1, id, playerName, score, level);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            Delete delete = new Delete();
            delete.deleteLeaderboard(connection, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}