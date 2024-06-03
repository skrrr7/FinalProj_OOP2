package Game.Main.com.example.pacman;

import javax.swing.JFrame;

public class Pacman extends JFrame{

    public Pacman() {
        add(new com.example.pacman.Model());
    }


    public static void main(String[] args) {
        com.example.pacman.Pacman pac = new com.example.pacman.Pacman();
        pac.setVisible(true);
        pac.setTitle("Pacman");
        pac.setSize(734,800);
        pac.setDefaultCloseOperation(EXIT_ON_CLOSE);
        pac.setLocationRelativeTo(null);

    }

}
