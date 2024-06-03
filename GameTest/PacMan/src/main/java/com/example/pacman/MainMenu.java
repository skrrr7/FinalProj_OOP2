package com.example.pacman;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainMenu extends JFrame {
    private Leaderboards leaderboards;
    private Clip musicClip;
    private boolean isMusicMuted;

    public MainMenu() {
        // Initialize the leaderboards
        leaderboards = new Leaderboards();

        // Set up the frame
        setTitle("Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(614, 397); // Updated screen size to 600x360
        setLocationRelativeTo(null); // Center the frame on the screen
        setLayout(new BorderLayout());

        // Initialize music
        initializeMusic();

        // Add key listener for music toggle
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_M) {
                    toggleMusic();
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();

        // Create a JLayeredPane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600, 360));

        // Add the GIF as the background
        JLabel background = new JLabel(new ImageIcon("Game/PacMan/src/images/main_menu/backgroundPic.gif")); // Change the path to your GIF file
        background.setBounds(0, 0, 600, 360); // Set bounds for the background label
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER); // Add background to the default layer

        // Add "LEADERBOARD" text
        JLabel leaderboardLabel = new JLabel("LEADERBOARD");
        leaderboardLabel.setBounds(22, 303, 100, 60); // Position and size of the label
        leaderboardLabel.setFont(new Font("Inter", Font.BOLD, 10)); // Set font
        leaderboardLabel.setForeground(new Color(0xFFD233)); // Set text color
        layeredPane.add(leaderboardLabel, JLayeredPane.PALETTE_LAYER); // Add leaderboard label to the palette layer

        // Load and resize the trophy image
        ImageIcon trophyIcon = new ImageIcon("Game/PacMan/src/images/main_menu/trophy.png"); // Path to the trophy image
        Image trophyImage = trophyIcon.getImage().getScaledInstance(43, 41, Image.SCALE_SMOOTH); // Resize image
        trophyIcon = new ImageIcon(trophyImage); // Create new ImageIcon with resized image
        JLabel trophyLabel = new JLabel(trophyIcon);
        trophyLabel.setBounds(36, 281, 43, 41); // Position and size of the trophy image
        layeredPane.add(trophyLabel, JLayeredPane.PALETTE_LAYER); // Add trophy image to the palette layer

        // Add "PLAY" text
        JLabel playLabel = new JLabel("PLAY");
        playLabel.setBounds(31, 65, 100, 20); // Position and size of the label
        playLabel.setFont(new Font("Inter", Font.BOLD, 12)); // Set font
        playLabel.setForeground(new Color(0x0FA958)); // Set text color
        layeredPane.add(playLabel, JLayeredPane.PALETTE_LAYER); // Add play label to the palette layer

        // Load and resize the play image
        ImageIcon playIcon = new ImageIcon("Game/PacMan/src/images/main_menu/play.png"); // Path to the play image
        Image playImage = playIcon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH); // Resize image
        playIcon = new ImageIcon(playImage); // Create new ImageIcon with resized image
        JLabel playImageLabel = new JLabel(playIcon);
        playImageLabel.setBounds(23, 17, 48, 48); // Position and size of the play image
        layeredPane.add(playImageLabel, JLayeredPane.PALETTE_LAYER); // Add play image to the palette layer

        // Add "SETTINGS" text
        JLabel settingsLabel = new JLabel("SETTINGS");
        settingsLabel.setBounds(510, 49, 100, 20); // Position and size of the label
        settingsLabel.setFont(new Font("Inter", Font.BOLD, 10)); // Set font
        settingsLabel.setForeground(Color.WHITE); // Set text color
        layeredPane.add(settingsLabel, JLayeredPane.PALETTE_LAYER); // Add settings label to the palette layer

        // Load and resize the gear image
        ImageIcon gearIcon = new ImageIcon("Game/PacMan/src/images/main_menu/gear.png"); // Path to the gear image
        Image gearImage = gearIcon.getImage().getScaledInstance(22, 23, Image.SCALE_SMOOTH); // Resize image
        gearIcon = new ImageIcon(gearImage); // Create new ImageIcon with resized image
        JLabel gearLabel = new JLabel(gearIcon);
        gearLabel.setBounds(524, 23, 22, 23); // Position and size of the gear image
        layeredPane.add(gearLabel, JLayeredPane.PALETTE_LAYER); // Add gear image to the palette layer

        // Add "QUIT" text
        JLabel quitLabel = new JLabel("QUIT");
        quitLabel.setBounds(524, 321, 100, 20); // Position and size of the label
        quitLabel.setFont(new Font("Inter", Font.BOLD, 12)); // Set font
        quitLabel.setForeground(new Color(0xF24E1E)); // Set text color
        layeredPane.add(quitLabel, JLayeredPane.PALETTE_LAYER); // Add quit label to the palette layer

        // Load and resize the quit image
        ImageIcon quitIcon = new ImageIcon("Game/PacMan/src/images/main_menu/quit.png"); // Path to the quit image
        Image quitImage = quitIcon.getImage().getScaledInstance(42, 42, Image.SCALE_SMOOTH); // Resize image
        quitIcon = new ImageIcon(quitImage); // Create new ImageIcon with resized image
        JLabel quitImageLabel = new JLabel(quitIcon);
        quitImageLabel.setBounds(518, 279, 42, 42); // Position and size of the quit image
        layeredPane.add(quitImageLabel, JLayeredPane.PALETTE_LAYER); // Add quit image to the palette layer

        // Create the settings panel
        int settingsPanelWidth = 330;
        int settingsPanelHeight = 290;
        int settingsPanelX = (600 - settingsPanelWidth) / 2;
        int settingsPanelY = (360 - settingsPanelHeight) / 2;
        JPanel settingsPanel = new JPanel();
        settingsPanel.setBounds(settingsPanelX, settingsPanelY, settingsPanelWidth, settingsPanelHeight);
        settingsPanel.setBackground(new Color(217, 217, 217, 204)); // Background color with opacity
        settingsPanel.setBorder(new RoundedBorder(16)); // Set rounded border
        settingsPanel.setVisible(false); // Initially hidden
        layeredPane.add(settingsPanel, JLayeredPane.MODAL_LAYER); // Add settings panel to the modal layer

        // Create the leaderboard panel
        int leaderboardPanelWidth = 330;
        int leaderboardPanelHeight = 290;
        int leaderboardPanelX = (600 - leaderboardPanelWidth) / 2;
        int leaderboardPanelY = (360 - leaderboardPanelHeight) / 2;
        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setBounds(leaderboardPanelX, leaderboardPanelY, leaderboardPanelWidth, leaderboardPanelHeight);
        leaderboardPanel.setBackground(new Color(217, 217, 217, 204)); // Background color with opacity
        leaderboardPanel.setBorder(new RoundedBorder(16)); // Set rounded border
        leaderboardPanel.setVisible(false); // Initially hidden
        layeredPane.add(leaderboardPanel, JLayeredPane.MODAL_LAYER); // Add leaderboard panel to the modal layer

        // Add leaderboard data to the panel
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));
        List<String> topScores = leaderboards.getTopScores(10);
        for (String score : topScores) {
            JLabel scoreLabel = new JLabel(score);
            leaderboardPanel.add(scoreLabel);
        }

        // Add mouse listener to gear icon to show/hide settings panel
        gearLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                settingsPanel.setVisible(!settingsPanel.isVisible());
            }
        });

        // Add mouse listener to settings label to show/hide settings panel
        settingsLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                settingsPanel.setVisible(!settingsPanel.isVisible());
            }
        });

        // Add mouse listener to trophy icon to show/hide leaderboard panel
        trophyLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                leaderboardPanel.setVisible(!leaderboardPanel.isVisible());
            }
        });

        // Add mouse listener to leaderboard label to show/hide leaderboard panel
        leaderboardLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                leaderboardPanel.setVisible(!leaderboardPanel.isVisible());
            }
        });

        // Add mouse listener to play image to load Pacman.java
        playImageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Pacman.main(null); // Load Pacman.java
            }
        });

        // Add mouse listener to play label to load Pacman.java
        playLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Pacman.main(null); // Load Pacman.java
            }
        });

        // Add mouse listener to quit image to exit the program
        quitImageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0); // Exit the program
            }
        });

        // Add mouse listener to quit label to exit the program
        quitLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0); // Exit the program
            }
        });

        add(layeredPane, BorderLayout.CENTER); // Add layeredPane to the frame
        setVisible(true); // Make the frame visible
    }

    private void initializeMusic() {
        try {
            File musicFile = new File("Game/PacMan/src/sounds/Skillet - The Resistance (Slowed Version).mp3"); // Path to your music file
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            musicClip = AudioSystem.getClip();
            musicClip.open(audioStream);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void toggleMusic() {
        if (isMusicMuted) {
            musicClip.start();
        } else {
            musicClip.stop();
        }
        isMusicMuted = !isMusicMuted;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu());
    }
}
