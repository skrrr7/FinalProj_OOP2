package com.example.pacman;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    private static Config instance = null;

    public String DB_URL;
    public String DB_USER;
    public String DB_PASSWORD;

    private Config()
    {
        Dotenv dotenv = Dotenv.load();
        String host = dotenv.get("host");
        String dbname = dotenv.get("dbname");
        String dbUsername = dotenv.get("dbUsername");
        String dbPassword = dotenv.get("dbPassword");
        DB_URL = "jdbc:mysql://"+host+":3306/"+dbname;
        DB_USER = dbUsername;
        DB_PASSWORD = dbPassword;
    }

    public static synchronized Config getInstance()
    {
        if (instance == null)
            instance = new Config();

        return instance;
    }
}
