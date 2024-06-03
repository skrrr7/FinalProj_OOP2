package com.example.pacman.UI;

import javax.swing.JFrame;
import java.util.Arrays;

public class Pacman extends JFrame{
    public Pacman(String username, String userId) {
        add(new Model(username, userId));
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));
        Pacman pac = new Pacman(args[0], args[1]);
        pac.setVisible(true);
        pac.setTitle("Pacman ");
        pac.setSize(734,800);
        pac.setDefaultCloseOperation(EXIT_ON_CLOSE);
        pac.setLocationRelativeTo(null);

    }

}