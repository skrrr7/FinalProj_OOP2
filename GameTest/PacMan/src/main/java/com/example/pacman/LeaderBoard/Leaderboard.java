package com.example.pacman.LeaderBoard;

public class Leaderboard {
    public String playerName;
    public int score;
    public int level;

    public Leaderboard(String playerName, int score, int level) {
        this.playerName = playerName;
        this.score = score;
        this.level = level;
    }

    // Getters and setters

    @Override
    public String toString() {
        return String.format("%s - %d - %d", playerName, score,level);
    }
}