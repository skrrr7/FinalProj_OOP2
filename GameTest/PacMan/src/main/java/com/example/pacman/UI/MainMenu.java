package com.example.pacman.UI;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import com.example.pacman.Config;
import com.example.pacman.LeaderBoard.Create;
import com.example.pacman.LeaderBoard.Leaderboard;
import com.example.pacman.LeaderBoard.Leaderboards;
import com.example.pacman.LeaderBoard.Truncate;

public class MainMenu extends JFrame {
    private Connection connection;
    private Leaderboards leaderboards;
    private Clip backgroundMusicClip;
    private FloatControl volumeControl;

    private String username;
    private int userId;

    public MainMenu() {
        // Initialize the leaderboards
        leaderboards = new Leaderboards();

        // Set up the frame
        setTitle("Attack on PacMan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(614, 397); // Updated screen size to 600x360
        setLocationRelativeTo(null); // Center the frame on the screen
        setLayout(new BorderLayout());

        // Create a JLayeredPane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600, 360));

        // Add the GIF as the background
        JLabel background = new JLabel(new ImageIcon("Game/PacMan/src/images/main_menu/main_menu_background.gif")); // Change the path to your GIF file
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

        UIManager.put("TabbedPane.contentOpaque", false);
        UIManager.getDefaults().put("TabbedPane.contentBorderInsets", new Insets(0,10,0,10));
        UIManager.getDefaults().put("TabbedPane.tabsOverlapBorder", true);

        // Create the settings panel
        int settingsPanelWidth = 330;
        int settingsPanelHeight = 290;
        int settingsPanelX = (600 - settingsPanelWidth) / 2;
        int settingsPanelY = (360 - settingsPanelHeight) / 2;
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayout(4, 4, 10, 10));
        settingsPanel.setBounds(settingsPanelX, settingsPanelY, settingsPanelWidth, settingsPanelHeight);
        settingsPanel.setBackground(new Color(217, 217, 217, 204)); // Background color with opacity
        //settingsPanel.setBorder(new RoundedBorder(16)); // Set rounded border
        settingsPanel.setVisible(false); // Initially hidden
        //layeredPane.add(settingsPanel, JLayeredPane.MODAL_LAYER); // Add settings panel to the modal layer

        JPanel settings2Panel = new JPanel();
        settings2Panel.setLayout(new GridLayout(2, 4, 10, 10));
        settings2Panel.setBounds(settingsPanelX, settingsPanelY, settingsPanelWidth, settingsPanelHeight);
        settings2Panel.setBackground(new Color(217, 217, 217, 204)); // Background color with opacity
        //settings2Panel.setBorder(new RoundedBorder(16)); // Set rounded border
        settings2Panel.setVisible(true); // Initially hidden

        JLabel accounts = new JLabel("ACCOUNTS", SwingConstants.CENTER);
        accounts.setPreferredSize(new Dimension(140, 50));
        accounts.setBackground(new Color(217, 217, 217, 204));

        JLabel audio = new JLabel("AUDIO", SwingConstants.CENTER);
        audio.setPreferredSize(new Dimension(140, 50));
        audio.setBackground(new Color(217, 217, 217, 204));

        // Create and add the volume control slider to the settings panel
        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50); // Default volume at 50%
        volumeSlider.addChangeListener(e -> adjustVolume(volumeSlider.getValue()));
        volumeSlider.setBackground(new Color(217, 217, 217, 0));
        settingsPanel.add(new JLabel("Volume:", SwingConstants.CENTER));
        settingsPanel.add(volumeSlider);

        JTabbedPane settingstp=new JTabbedPane();
        JPanel accountsPanel = new JPanel();

        accountsPanel.setBackground(new Color(217, 217, 217, 204)); // Background color with opacity
        accountsPanel.setBorder(new RoundedBorder(16)); // Set rounded border
        accountsPanel.setBounds(settingsPanelX, settingsPanelY, settingsPanelWidth, settingsPanelHeight);

        List<Leaderboard> topScoreUsernames = leaderboards.getTopScores(20);

        Object[] columnUsernames = {"Username",};
        Object[][] dataUsername = new Object[20][1];

        int iU=0;
        for (Leaderboard score : topScoreUsernames) {
            dataUsername[iU][0] = score.playerName;
            iU++;
        }

        UIManager.put("TabbedPane.contentOpaque", false);
        UIManager.getDefaults().put("TabbedPane.contentBorderInsets", new Insets(0,10,0,10));
        UIManager.getDefaults().put("TabbedPane.tabsOverlapBorder", true);

        JTable tableUsername = new JTable(dataUsername, columnUsernames);
        JTableHeader headerUsername = tableUsername.getTableHeader();
        headerUsername.setDefaultRenderer(new HeaderRenderer());
        headerUsername.setBackground(new Color(217, 217, 217, 204));
        tableUsername.setBackground(new Color(217, 217, 217, 204));
        tableUsername.setBounds(settingsPanelX, settingsPanelY, settingsPanelWidth, settingsPanelHeight);
        tableUsername.setShowGrid(false);

        JScrollPane scrollPaneUsername = new JScrollPane(tableUsername);
        scrollPaneUsername.setBounds(settingsPanelX, settingsPanelY, settingsPanelWidth, settingsPanelHeight);
        scrollPaneUsername.setOpaque(false);
        scrollPaneUsername.getViewport().setOpaque(false);


        settings2Panel.add(scrollPaneUsername);



        settingstp.setBounds(settingsPanelX, settingsPanelY, settingsPanelWidth, settingsPanelHeight);
        settingstp.add("Accounts",settings2Panel);
        settingstp.add("Audio",settingsPanel);
        settingstp.setTabComponentAt(0, accounts);
        settingstp.setTabComponentAt(1, audio);
        settingstp.setBackground(new Color(217, 217, 217, 204));
        settingstp.setVisible(false);
        settingstp.setUI(new BasicTabbedPaneUI() {
            private final Insets borderInsets = new Insets(0, 0, 0, 0);
            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
            }
            @Override
            protected Insets getContentBorderInsets(int tabPlacement) {
                return borderInsets;
            }
        });





        layeredPane.add(settingstp, JLayeredPane.MODAL_LAYER);



        // Create the leaderboard panel
        int leaderboardPanelWidth = 330;
        int leaderboardPanelHeight = 290;
        int leaderboardPanelX = (600 - leaderboardPanelWidth) / 2;
        int leaderboardPanelY = (360 - leaderboardPanelHeight) / 2;
        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setBounds(leaderboardPanelX, leaderboardPanelY, leaderboardPanelWidth, leaderboardPanelHeight);
        leaderboardPanel.setBackground(new Color(217, 217, 217, 204)); // Background color with opacity
        //leaderboardPanel.setBorder(new RoundedBorder(16)); // Set rounded border
        leaderboardPanel.setVisible(false); // Initially hidden
        //layeredPane.add(leaderboardPanel, JLayeredPane.MODAL_LAYER); // Add leaderboard panel to the modal layer



        JLabel lab = new JLabel("LEADERBOARD", SwingConstants.CENTER);
        lab.setPreferredSize(new Dimension(leaderboardPanelWidth-25, 50));
        lab.setBackground(new Color(217, 217, 217, 204));


        JTabbedPane tp=new JTabbedPane();
        tp.setBounds(leaderboardPanelX, leaderboardPanelY, leaderboardPanelWidth, leaderboardPanelHeight);
        tp.add("Leaderboard",leaderboardPanel);
        tp.setBackground(new Color(217, 217, 217, 204));
        tp.setVisible(false);
        tp.setEnabledAt(0, false);
        tp.setTabComponentAt(0, lab);
        tp.setUI(new BasicTabbedPaneUI() {
            private final Insets borderInsets = new Insets(0, 0, 0, 0);
            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
            }
            @Override
            protected Insets getContentBorderInsets(int tabPlacement) {
                return borderInsets;
            }
        });

        layeredPane.add(tp, JLayeredPane.MODAL_LAYER);


        // Add leaderboard data to the panel
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));
        List<Leaderboard> topScores = leaderboards.getTopScores(10);
//        for (Leaderboard score : topScores) {
//            JLabel scoreLabel = new JLabel(String.valueOf(score));
//            leaderboardPanel.add(scoreLabel);
//        }

        //DefaultListModel<Leaderboard> listModel = new DefaultListModel<Leaderboard>();
        Object[] column = {"Player Name", "Scores", "Level"};
        Object[][] data = new Object[10][4];
        int i=0;
        for (Leaderboard score : topScores) {
            int j=0;
            //listModel.addElement(score);
            data[i][j++] = score.playerName;
            data[i][j++] = score.score;
            data[i][j++] = score.level;
            i++;
        }

        //JList<Leaderboard> jList = new JList<>(listModel);

        //Object[][] data = {{1, 2, 3}, {3, 4, 5}, {5, 6, 7}};

        JTable table = new JTable(data, column);
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer());
        header.setBackground(new Color(217, 217, 217, 204));
        table.setBackground(new Color(217, 217, 217, 204));
        table.setShowGrid(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        leaderboardPanel.add(scrollPane);
        JButton clearLeaderButton = new JButton("Clear ALL");
        leaderboardPanel.add(clearLeaderButton);

        // Create the container for username input
        JPanel usernameContainer = new JPanel();
        JLabel usernameLabel = new JLabel("Enter username:");
        JTextField usernameField = new JTextField(15);
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        // Add components to the username container
        usernameContainer.add(usernameLabel);
        usernameContainer.add(usernameField);
        usernameContainer.add(okButton);
        usernameContainer.add(cancelButton);

        // Set up action listeners for buttons
        clearLeaderButton.addActionListener(e -> {
            try {
                // Assuming you have a database connection named 'connection'
                // Establish database connection
                connection = DriverManager.getConnection(Config.getInstance().DB_URL, Config.getInstance().DB_USER, Config.getInstance().DB_PASSWORD);
                Truncate truncate = new Truncate();
                truncate.clearAll(connection);
                this.setVisible(true);
                connection.close(); // Close the connection after use
            } catch (SQLException ex) {
                ex.printStackTrace();
                // Handle SQLException as per your application's requirements
            }
        });

        okButton.addActionListener(e -> {
            String username = usernameField.getText();
            if (!username.isEmpty()) {
                // Do something with the username
                System.out.println("Username: " + username);

                // For now, let's just hide the username container
                usernameContainer.setVisible(false);

                // Insert the username into the database
                try {
                    // Assuming you have a database connection named 'connection'
                    // Establish database connection
                    connection = DriverManager.getConnection(Config.getInstance().DB_URL, Config.getInstance().DB_USER, Config.getInstance().DB_PASSWORD);
                    Create create = new Create();
                    int userId = create.insertLeaderboard(connection, username, 0, 0);
                    this.userId = userId;
                    this.username = username;
                    System.out.println("userId " + userId);
                    connection.close(); // Close the connection after use
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    // Handle SQLException as per your application's requirements
                }
            }
        });

        cancelButton.addActionListener(e -> {
            // Clear the username field and hide the container
            usernameField.setText("");
            usernameContainer.setVisible(false);
        });

        // Set the position of the username container
        int usernameContainerX = (600 - 200) / 2; // Center horizontally
        int usernameContainerY = 280; // Below the leaderboard panel
        usernameContainer.setBounds(usernameContainerX, usernameContainerY, 200, 80);

        // Add the username container to the layered pane
        layeredPane.add(usernameContainer, JLayeredPane.MODAL_LAYER);

        // Add mouse listener to gear icon to show/hide settings panel
        gearLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //settingsPanel.setVisible(!settingsPanel.isVisible());
                settingstp.setVisible(!settingstp.isVisible());
                if (settingstp.isVisible()) {
                    tp.setVisible(false);
                }
            }
        });

        // Add mouse listener to settings label to show/hide settings panel
        settingsLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //settingsPanel.setVisible(!settingsPanel.isVisible());
                settingstp.setVisible(!settingstp.isVisible());
                if (settingstp.isVisible()) {
                    tp.setVisible(false);
                }
            }
        });

        // Add mouse listener to trophy icon to show/hide leaderboard panel
        trophyLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //leaderboardPanel.setVisible(!leaderboardPanel.isVisible());
                tp.setVisible(!tp.isVisible());
                if (tp.isVisible()) {
                    settingstp.setVisible(false);
                }
            }
        });

        // Add mouse listener to leaderboard label to show/hide leaderboard panel
        leaderboardLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //leaderboardPanel.setVisible(!leaderboardPanel.isVisible());
                tp.setVisible(!tp.isVisible());
                if (tp.isVisible()) {
                    settingstp.setVisible(false);
                }
            }
        });

        // Add mouse listener to play image to load Pacman.java
        playImageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Pacman.main(new String[]{username, String.valueOf(userId)}); // Load Pacman.java
            }
        });

        // Add mouse listener to play label to load Pacman.java
        playLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Pacman.main(new String[]{username, String.valueOf(userId)}); // Load Pacman.java
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

        // Load and play background music
        playBackgroundMusic("Game/PacMan/src/sounds/background_music.wav");

        setVisible(true); // Make the frame visible
    }

    private void playBackgroundMusic(String filePath) {
        try {
            File musicPath = new File(filePath);
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                backgroundMusicClip = AudioSystem.getClip();
                backgroundMusicClip.open(audioInput);
                volumeControl = (FloatControl) backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
                backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music
                backgroundMusicClip.start();
            } else {
                System.out.println("Can't find file: " + filePath);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void adjustVolume(int volume) {
        if (volumeControl != null) {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float newVolume = min + (volume / 100.0f) * (max - min);
            volumeControl.setValue(newVolume);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu());
    }
}