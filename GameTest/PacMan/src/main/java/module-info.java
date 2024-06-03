module com.example.pacman {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;


    opens com.example.pacman to javafx.fxml;
    exports com.example.pacman;
    exports com.example.pacman.LeaderBoard;
    opens com.example.pacman.LeaderBoard to javafx.fxml;
    exports com.example.pacman.UI;
    opens com.example.pacman.UI to javafx.fxml;
}