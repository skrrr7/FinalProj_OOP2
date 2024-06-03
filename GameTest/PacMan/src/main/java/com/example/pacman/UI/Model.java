package com.example.pacman.UI;

import com.example.pacman.Config;
import com.example.pacman.CountdownTimer;
import com.example.pacman.LeaderBoard.Create;
import com.example.pacman.LeaderBoard.Update;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Model extends JPanel implements ActionListener {
    private Connection connection;
    private String username;
    private String userId;

    private Dimension d;
    private final Font smallFont = new Font("Arial", Font.BOLD, 14);
    private boolean inGame = false;
    private Clip collisionSound;
    private boolean dying = false;

    private final int BLOCK_SIZE = 48;
    private final int N_BLOCKS = 15;
    private int remainingTimeInSeconds = 30;
    private final Timer gameTimer;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    private final int MAX_GHOSTS = 17;
    private final int PACMAN_SPEED = 6;
    private final int INITIAL_GHOST_SPEED = 1; // Initial ghost speed for level 1
    private final int INITIAL_GHOST_COUNT = 3; // Initial number of ghosts for level 1

    private int N_GHOSTS = INITIAL_GHOST_COUNT;
    private int countdown = 3;
    private int lives;
    private int[] dx, dy;
    private int[] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed;
    private int level = 0;
    private int score = 0;
    private boolean gameOver = false;


    private Image heart, pacman, ghost;
    private Image up, down, left, right;

    private int pacman_x, pacman_y, pacmand_x, pacmand_y;
    private int req_dx, req_dy;
    private boolean[] ghostsAlive;

    private final short[] levelData = {
            19, 18, 18, 18, 26, 26, 26, 26, 26, 26, 26, 18, 18, 18, 22,
            17, 24, 16, 20,  0,  0,  0,  0,  0,  0,  0, 17, 16, 24, 20,
            29,  0, 17, 16, 18, 18, 18, 18, 18, 18, 18, 16, 20,  0, 13,
            1,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0,  4,
            23,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0, 23,
            17, 18, 16, 24, 16, 16, 16, 16, 16, 16, 16, 24, 16, 18, 20,
            17, 16, 20,  0, 17, 16, 16, 24, 16, 16, 20,  0, 17, 16, 20,
            17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,
            17, 16, 20,  0, 17, 16, 16, 18, 16, 16, 20,  0, 17, 16, 20,
            17, 24, 16, 18, 16, 16, 16, 16, 16, 16, 16, 18, 16, 24, 20,
            29,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0, 13,
            1,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0,  4,
            23,  0, 17, 16, 24, 24, 24, 24, 24, 24, 24, 16, 20,  0, 23,
            17, 18, 16, 20,  0,  0,  0,  0,  0,  0,  0, 17, 16, 18, 20,
            25, 24, 24, 24, 26, 26, 26, 26, 26, 26, 26, 24, 24, 24, 28,
    };

    private final int validSpeeds[] = {1, 2, 3, 4, 6, 8};
    private final int maxSpeed = 6;

    private int currentSpeed = 4;
    private short[] screenData;
    private Timer timer;

    public Model(String username, String userId) {
        this.username = username;
        this.userId = userId;
        loadImages();
        initVariables();
        initCollisionSound();
        addKeyListener(new TAdapter());
        setFocusable(true);
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remainingTimeInSeconds--;
                if (remainingTimeInSeconds <= 0) {
                    endGame(); // Implement this method to end the game
                }
            }
        });
        initGame();
    }

    public String getUsername() {
        return this.username;
    }

    public String getUserId() {
        return this.userId;
    }

    private void initCollisionSound() {
        try {
            // Create a File object with the absolute path
            File soundFile = new File("Game/PacMan/src/sounds/slash.wav");

            // Check if the file exists
            if (!soundFile.exists()) {
                throw new FileNotFoundException("Sound file not found: " + soundFile);
            }

            // Continue loading the sound file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            collisionSound = AudioSystem.getClip();
            collisionSound.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playCollisionSound() {
        if (collisionSound != null && !collisionSound.isRunning()) {
            collisionSound.setFramePosition(0);
            collisionSound.start();
        }
    }
    public int getCountdown() {
        return countdown;
    }
    public void decrementCountdown() {
        countdown--;
    }

    public void startCountdownTimer() {
        countdown = 3; // Reset the countdown
        CountdownTimer countdownTimer = new CountdownTimer(this);
        countdownTimer.start();
    }


    private void endGame() {
        inGame = false;
        gameOver = true;
        stopTimer(); // Stop the timer when the game ends
        // Implement any other actions to end the game
    }


    public void startTimer() {
        resetTimer(); // Reset and start the timer
        gameTimer.start();
    }

    private void stopTimer() {
        gameTimer.stop();
    }



    private void loadImages() {
        down = new ImageIcon("Game/PacMan/src/images/down_man.gif").getImage();
        up = new ImageIcon("Game/PacMan/src/images/up_man.gif").getImage();
        left = new ImageIcon("Game/PacMan/src/images/left_man.gif").getImage();
        right = new ImageIcon("Game/PacMan/src/images/right_man.gif").getImage();
        pacman = new ImageIcon("Game/PacMan/src/images/pacman.gif").getImage();
        ghost = new ImageIcon("Game/PacMan/src/images/ghost.gif").getImage();
        heart = new ImageIcon("Game/PacMan/src/images/heart.png").getImage();

    }
    private void initVariables() {
        screenData = new short[N_BLOCKS * N_BLOCKS];
        d = new Dimension(400, 400);
        ghost_x = new int[MAX_GHOSTS];
        ghost_dx = new int[MAX_GHOSTS];
        ghost_y = new int[MAX_GHOSTS];
        ghost_dy = new int[MAX_GHOSTS];
        ghostSpeed = new int[MAX_GHOSTS];
        ghostsAlive = new boolean[MAX_GHOSTS];
        dx = new int[4];
        dy = new int[4];

        timer = new Timer(40, this);
        timer.start();
    }

    private void playGame(Graphics2D g2d) {
        if (dying) {
            death();
        } else {
            movePacman();
            drawPacman(g2d);
            moveGhosts(g2d);
            checkMaze();
            checkAllGhostsEaten(); // New method to check if all ghosts are eaten
        }
    }


    private void checkAllGhostsEaten() {
        boolean allEaten = true;
        for (int i = 0; i < N_GHOSTS; i++) {
            if (ghostsAlive[i]) {
                allEaten = false;
                break;
            }
        }
        if (allEaten) {
            initLevel(); // Initialize next level
        }
    }


    private void showIntroScreen(Graphics2D g2d) {
        if (!inGame) {
            String start = "Press SPACE to start ";
            Font largeFont = new Font("Helvetica", Font.BOLD, 24);
            g2d.setFont(largeFont);

            // Calculate the width and height of the text
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(start);
            int usernameTextWidth = fm.stringWidth(start);
            int textHeight = fm.getHeight();

            // Set the position for the text
            int x = (SCREEN_SIZE - textWidth) / 2;
            int xUsername = (SCREEN_SIZE - usernameTextWidth) / 2;
            int y = SCREEN_SIZE / 2;

            // Set the background color and draw the rectangle
            g2d.setColor(Color.BLACK);
            g2d.fillRect(x - 10, y - textHeight + fm.getDescent() - 10, textWidth + 20, textHeight + 20);

            // Set the text color and draw the string
            g2d.setColor(Color.YELLOW);
            if (username != null) {
                g2d.drawString(username, xUsername, y - 50); // TODO: center username
            }
            g2d.drawString(start, x, y);
        } else {
            // Countdown when the game has started
            if (countdown > 0) {
                String countdownMsg = "Starting in " + countdown;
                Font smallFont = new Font("Helvetica", Font.BOLD, 18);
                g2d.setFont(smallFont);

                // Calculate the width and height of the text
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(countdownMsg);
                int textHeight = fm.getHeight();

                // Set the position for the text
                int x = (SCREEN_SIZE - textWidth) / 2;
                int y = SCREEN_SIZE / 2 + textHeight + 20;

                // Set the background color and draw the rectangle
                g2d.setColor(Color.BLACK);
                g2d.fillRect(x - 10, y - textHeight + fm.getDescent() - 10, textWidth + 20, textHeight + 20);

                // Set the text color and draw the string
                g2d.setColor(Color.YELLOW);
                g2d.drawString(countdownMsg, x, y);
            }
        }
    }

    private void resetTimer() {
        remainingTimeInSeconds = 30; // Reset timer to 30 seconds
        gameTimer.restart(); // Restart the timer
    }





    private void drawTimer(Graphics2D g) {
        g.setFont(smallFont);
        g.setColor(new Color(5, 181, 79));
        String s = "Time: " + remainingTimeInSeconds; // Display remaining time
        g.drawString(s, SCREEN_SIZE / 2 + 96, SCREEN_SIZE + 16);

        for (int i = 0; i < lives; i++) {
            g.drawImage(heart, i * 28 + 8, SCREEN_SIZE + 1, this);
        }
    }



    private void checkMaze() {

        int i = 0;
        boolean finished = true;

        while (i < N_BLOCKS * N_BLOCKS && finished) {

            if ((screenData[i]) != 0) {
                finished = false;
            }

            i++;
        }

        if (finished) {

            if (N_GHOSTS < MAX_GHOSTS) {
                N_GHOSTS++;
            }

            if (currentSpeed < maxSpeed) {
                currentSpeed++;
            }

            initLevel();
        }
    }

    private void death() {

        lives--;

        if (lives == 0) {
            inGame = false;
        }

        continueLevel();
    }

    private void moveGhosts(Graphics2D g2d) {
        int pos;
        int count;

        for (int i = 0; i < N_GHOSTS; i++) {
            if (!ghostsAlive[i]) {
                continue; // Skip the ghost if it is not alive
            }

            if (ghost_x[i] % BLOCK_SIZE == 0 && ghost_y[i] % BLOCK_SIZE == 0) {
                pos = ghost_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (ghost_y[i] / BLOCK_SIZE);

                count = 0;

                if ((screenData[pos] & 1) == 0 && ghost_dx[i] != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 2) == 0 && ghost_dy[i] != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }

                if ((screenData[pos] & 4) == 0 && ghost_dx[i] != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 8) == 0 && ghost_dy[i] != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }

                if (count == 0) {
                    if ((screenData[pos] & 15) == 15) {
                        ghost_dx[i] = 0;
                        ghost_dy[i] = 0;
                    } else {
                        ghost_dx[i] = -ghost_dx[i];
                        ghost_dy[i] = -ghost_dy[i];
                    }
                } else {
                    count = (int) (Math.random() * count);
                    if (count > 3) {
                        count = 3;
                    }
                    ghost_dx[i] = dx[count];
                    ghost_dy[i] = dy[count];
                }
            }

            ghost_x[i] = ghost_x[i] + (ghost_dx[i] * ghostSpeed[i]);
            ghost_y[i] = ghost_y[i] + (ghost_dy[i] * ghostSpeed[i]);
            drawGhost(g2d, ghost_x[i] + 1, ghost_y[i] + 1);

            if (pacman_x > (ghost_x[i] - 12) && pacman_x < (ghost_x[i] + 12)
                    && pacman_y > (ghost_y[i] - 12) && pacman_y < (ghost_y[i] + 12)
                    && inGame) {
                // Pacman eats the ghost
                ghostsAlive[i] = false; // Mark the ghost as not alive
                playCollisionSound(); // Play the collision sound effect
                score += 20;
                try {
                    // Assuming you have a database connection named 'connection'
                    // Establish database connection
                    connection = DriverManager.getConnection(Config.getInstance().DB_URL, Config.getInstance().DB_USER, Config.getInstance().DB_PASSWORD);
                    Update update = new Update();
                    update.updateLeaderboard(connection, Integer.parseInt(getUserId()), getUsername(), score,level);
                    //int userId = create.insertLeaderboard(connection, username, 0, 0);
                    //this.userId = userId;
                    //this.username = username;
                    //System.out.println("userId " + userId);
                    connection.close(); // Close the connection after use
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    // Handle SQLException as per your application's requirements
                }
            }
        }
    }

    private void drawGhost(Graphics2D g2d, int x, int y) {
        if (g2d != null) {
            g2d.drawImage(ghost, x, y, this);
        }
    }


    private void movePacman() {

        int pos;
        short ch;

        if (pacman_x % BLOCK_SIZE == 0 && pacman_y % BLOCK_SIZE == 0) {
            pos = pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE);
            ch = screenData[pos];

            if ((ch & 16) != 0) {
                screenData[pos] = (short) (ch & 15);
            }

            if (req_dx != 0 || req_dy != 0) {
                if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
                        || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
                        || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
                        || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
                    pacmand_x = req_dx;
                    pacmand_y = req_dy;
                }
            }

            if ((pacmand_x == -1 && pacmand_y == 0 && (ch & 1) != 0)
                    || (pacmand_x == 1 && pacmand_y == 0 && (ch & 4) != 0)
                    || (pacmand_x == 0 && pacmand_y == -1 && (ch & 2) != 0)
                    || (pacmand_x == 0 && pacmand_y == 1 && (ch & 8) != 0)) {
                pacmand_x = 0;
                pacmand_y = 0;
            }
        }
        pacman_x = pacman_x + PACMAN_SPEED * pacmand_x;
        pacman_y = pacman_y + PACMAN_SPEED * pacmand_y;
    }

    private void drawPacman(Graphics2D g2d) {

        if (req_dx == -1) {
            g2d.drawImage(left, pacman_x + 1, pacman_y + 1, this);
        } else if (req_dx == 1) {
            g2d.drawImage(right, pacman_x + 1, pacman_y + 1, this);
        } else if (req_dy == -1) {
            g2d.drawImage(up, pacman_x + 1, pacman_y + 1, this);
        } else {
            g2d.drawImage(down, pacman_x + 1, pacman_y + 1, this);
        }
    }

    private void drawMaze(Graphics2D g2d) {
        short i = 0;
        int x, y;
        int wallThickness = 2;  // Set the desired wall thickness

        for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {

                g2d.setColor(Color.black);  // Background color
                g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);

                g2d.setColor(new Color(255, 0, 0));  // Wall color
                g2d.setStroke(new BasicStroke(wallThickness));

                if ((screenData[i] & 1) != 0) { // Draw left wall
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 2) != 0) { // Draw top wall
                    g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
                }

                if ((screenData[i] & 4) != 0) { // Draw right wall
                    g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 8) != 0) { // Draw bottom wall
                    g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1);
                }

                // Remove the following code block that draws the pellets
            /*
            if ((screenData[i] & 16) != 0) { // Draw pellet
                g2d.setColor(new Color(255, 255, 255));
                g2d.fillOval(x + 10, y + 10, 6, 6);
            }
            */

                i++;
            }
        }
    }



    private void initGame() {
        lives = 3;
        level = 0;
        N_GHOSTS = 6;
        currentSpeed = 3;
        inGame = false; // Set inGame to false initially

        // Start the countdown timer when the space key is pressed
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (!inGame && key == KeyEvent.VK_SPACE) {
                    inGame = true;
                    startCountdownTimer(); // Start the countdown timer when the game starts
                    startTimer(); // Start the game timer after the countdown
                }
            }
        });

        // Display the "Press Start" message
        repaint();
    }




    private void initLevel() {
        for (int i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            screenData[i] = levelData[i];
        }

        // Increment the level variable
        level++;



        try {
            // Assuming you have a database connection named 'connection'
            // Establish database connection
            connection = DriverManager.getConnection(Config.getInstance().DB_URL, Config.getInstance().DB_USER, Config.getInstance().DB_PASSWORD);
            Update update = new Update();
            update.updateLeaderboard(connection, Integer.parseInt(getUserId()), getUsername(), score,level);
            //int userId = create.insertLeaderboard(connection, username, 0, 0);
            //this.userId = userId;
            //this.username = username;
            //System.out.println("userId " + userId);
            connection.close(); // Close the connection after use
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle SQLException as per your application's requirements
        }

        // Adjust the number of ghosts based on the current level
        N_GHOSTS = INITIAL_GHOST_COUNT + (level - 1) + 2; // You can adjust this formula as needed

        // Initialize ghosts and other game elements
        for (int i = 0; i < N_GHOSTS; i++) {
            if (i % 2 == 0) {
                ghostSpeed[i] = INITIAL_GHOST_SPEED + level - 1; // Double the speed
            }
        }

        for (int i = 0; i < N_GHOSTS; i++) {
            ghostsAlive[i] = true;
        }

        continueLevel();

        // Reset and start the timer for the new level
        resetTimer();
    }





    private void continueLevel() {
        int dx = 1;
        int random;

        // Calculate the center of the maze
        int centerX = N_BLOCKS / 2 * BLOCK_SIZE;
        int centerY = N_BLOCKS / 2 * BLOCK_SIZE;

        // Generate random respawn coordinates within the maze boundaries
        int randomX = (int) (Math.random() * (N_BLOCKS - 2) + 1) * BLOCK_SIZE;
        int randomY = (int) (Math.random() * (N_BLOCKS - 2) + 1) * BLOCK_SIZE;

        pacman_x = randomX;
        pacman_y = randomY;

        for (int i = 0; i < N_GHOSTS; i++) {
            // Start each ghost at the center of the maze
            ghost_x[i] = centerX;
            ghost_y[i] = centerY;

            ghost_dy[i] = 0;
            ghost_dx[i] = dx;
            dx = -dx;
            random = (int) (Math.random() * (currentSpeed + 1));

            if (random > currentSpeed) {
                random = currentSpeed;
            }

            ghostSpeed[i] = validSpeeds[random] * 2; // Multiply the speed by 2 for faster movement
        }

        // Reset pacman direction
        pacmand_x = 0;
        pacmand_y = 0;
        req_dx = 0;
        req_dy = 0;
        dying = false;
    }







    private void drawLevel(Graphics2D g2d) {
        String levelString = "Level " + level;
        g2d.setColor(Color.white);
        g2d.setFont(smallFont);

        // Calculate the x-coordinate to center the text horizontally
        int stringWidth = g2d.getFontMetrics().stringWidth(levelString);
        int x = (SCREEN_SIZE - stringWidth) / 2;

        // Place the text at a fixed y-coordinate
        int y = SCREEN_SIZE + 16;

        g2d.drawString(levelString, x, y);
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Draw gradient background
        GradientPaint gradient = new GradientPaint(0, 0, Color.DARK_GRAY, getWidth(), getHeight(), Color.BLACK);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        drawMaze(g2d);
        drawTimer(g2d);

        if (gameOver) {
            showGameOverScreen(g2d);
        } else if (inGame) {
            playGame(g2d);
        } else {
            showIntroScreen(g2d);
        }
        drawLevel(g2d);

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }
    private void showGameOverScreen(Graphics2D g2d) {
        String gameOverMsg = "Game Over";
        Font largeFont = new Font("Helvetica", Font.BOLD, 24);
        g2d.setFont(largeFont);

        // Calculate the width and height of the text
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(gameOverMsg);
        int textHeight = fm.getHeight();

        // Set the position for the text
        int x = (SCREEN_SIZE - textWidth) / 2;
        int y = SCREEN_SIZE / 2;

        // Set the background color and draw the rectangle
        g2d.setColor(Color.BLACK);
        g2d.fillRect(x - 10, y - textHeight + fm.getDescent() - 10, textWidth + 20, textHeight + 20);

        // Set the text color and draw the string
        g2d.setColor(Color.RED);
        g2d.drawString(gameOverMsg, x, y);

        if (gameOver) {
            // Draw restart button
            String restartMsg = "Press R to restart";
            Font smallFont = new Font("Arial", Font.PLAIN, 14);
            g2d.setFont(smallFont);

            // Calculate the width and height of the text
            fm = g2d.getFontMetrics();
            textWidth = fm.stringWidth(restartMsg);
            textHeight = fm.getHeight();

            // Set the position for the text
            x = (SCREEN_SIZE - textWidth) / 2;
            y += textHeight + 20;

            // Draw the restart message
            g2d.drawString(restartMsg, x, y);
        }
    }

    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (!inGame && key == KeyEvent.VK_SPACE && !gameOver) {
                inGame = true;
                initGame();
                startTimer(); // Start the game timer
                startCountdownTimer(); // Start the countdown when the game starts
            } else if (!timer.isRunning() && key == KeyEvent.VK_ESCAPE) {
                inGame = false;
                stopTimer(); // Stop the timer if the game is paused
            }

            if (inGame) {
                if (key == KeyEvent.VK_LEFT) {
                    req_dx = -1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    req_dx = 1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_UP) {
                    req_dx = 0;
                    req_dy = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    req_dx = 0;
                    req_dy = 1;
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    inGame = false;
                }
            } else {
                if (gameOver && key == KeyEvent.VK_R) {
                    gameOver = false; // Reset game over status
                    inGame = true;
                    initGame();
                    startTimer(); // Start the timer when the game starts
                }   
            }
        }

    }



    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}