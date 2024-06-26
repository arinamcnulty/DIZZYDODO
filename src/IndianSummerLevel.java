/*
Рівень ІНДІАН САМЕР
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class IndianSummerLevel extends GameLevelPanel {

    private String levelName = "IndianSummer";
    private BufferedImage backgroundImage;
    private BufferedImage birdImage;
    private BufferedImage obstacleImage;
    private BufferedImage flippedObstacleImage;
    private BufferedImage grapeImage;
    private int birdY;
    private int birdX;
    private float vy = 0;
    private final float GRAVITY = 1.0f;
    private final int JUMP_STRENGTH = -8;
    private boolean gameStarted = false;
    private boolean gameOver = false;
    private JButton restartButton;
    private Timer gameTimer;
    private List<Rectangle> obstacles;
    private List<Rectangle> grapes;
    private int grapesCollected = 0;
    private Random random;


    public IndianSummerLevel(GameFrame gameFrame) {
        super(gameFrame);
        loadBackgroundImage();
        loadBirdImage();
        loadObstacleImage();
        loadGrapeImage();
        setFocusable(true);
        requestFocusInWindow();
        random = new Random();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!gameStarted && !gameOver) {
                    gameStarted = true;
                }
                if (!gameOver) {
                    jump();
                }
            }
        });
        initRestartButton();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        resetGame();
        startMovement();
    }

    private void loadBackgroundImage() {  //фон
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/indiansummerFON.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            backgroundImage = null;
        }
    }

    private void loadBirdImage() { //герой
        try {
            birdImage = ImageIO.read(getClass().getResource("/jerry.png"));
            birdImage = scaleImage(birdImage, 0.15);
        } catch (IOException e) {
            e.printStackTrace();
            birdImage = null;
        }
    }

    private void loadObstacleImage() {  //перешкоди
        try {
            obstacleImage = ImageIO.read(getClass().getResource("/бочка.png"));
            obstacleImage = scaleImage(obstacleImage, 0.35);
            flippedObstacleImage = flipImageVertically(obstacleImage);
        } catch (IOException e) {
            e.printStackTrace();
            obstacleImage = null;
        }
    }

    private void loadGrapeImage() { //бонуси
        try {
            grapeImage = ImageIO.read(getClass().getResource("/виноград.png"));
            grapeImage = scaleImage(grapeImage, 0.15);
        } catch (IOException e) {
            e.printStackTrace();
            grapeImage = null;
        }
    }

    private BufferedImage scaleImage(BufferedImage srcImg, double factor) { //відтворення зображення
        int scaledWidth = (int) (srcImg.getWidth() * factor);
        int scaledHeight = (int) (srcImg.getHeight() * factor);
        BufferedImage scaledImg = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaledImg.createGraphics();
        g2.drawImage(srcImg, 0, 0, scaledWidth, scaledHeight, null);
        g2.dispose();
        return scaledImg;
    }

    private BufferedImage flipImageVertically(BufferedImage srcImg) { //перегортаємо дзеркально перешкоду
        int width = srcImg.getWidth();
        int height = srcImg.getHeight();
        BufferedImage flippedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = flippedImg.createGraphics();
        g2.drawImage(srcImg, 0, height, width, -height, null);
        g2.dispose();
        return flippedImg;
    }

    private void jump() { //стрибок вгору
        vy = JUMP_STRENGTH;
    }

    private void initRestartButton() { //рестарт
        restartButton = new JButton("ПЕРЕЗАПУСТИТИ");
        restartButton.setFont(new Font("Arial", Font.BOLD, 14));
        restartButton.setVisible(false);
        restartButton.setEnabled(false);
        restartButton.setPreferredSize(new Dimension(200, 50));
        restartButton.addActionListener(e -> {
            resetGame();
            startMovement();
        });
        JPanel restartButtonPanel = new JPanel();
        restartButtonPanel.setOpaque(false);
        restartButtonPanel.add(restartButton);
        this.add(restartButtonPanel, BorderLayout.CENTER);
    }

    private void showRestartButton() { //повідомлення про закінчення гри
        if (restartButton != null) {
            restartButton.setVisible(true);
            restartButton.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Ви зібрали " + grapesCollected + " штук!");
            updateScore(levelName, grapesCollected); // Оновлення скорсу після завершення гри
        }
    }

    public void resetGame() { //перезапуск
        birdX = 800 / 2 - birdImage.getWidth() / 2;
        birdY = 600 / 2 - birdImage.getHeight() / 2;
        vy = 0;
        gameStarted = false;
        gameOver = false;
        obstacles = new ArrayList<>();
        grapes = new ArrayList<>();
        grapesCollected = 0;
        if (restartButton != null) {
            restartButton.setVisible(false);
            restartButton.setEnabled(false);
        }
    }

    @Override
    protected void paintComponent(Graphics g) { //вимальовування
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        if (birdImage != null) {
            g.drawImage(birdImage, birdX, birdY, this);
        }
        if (obstacleImage != null) {
            for (Rectangle obstacle : obstacles) {
                if (obstacle.y == 0) {
                    g.drawImage(obstacleImage, obstacle.x, obstacle.y, obstacle.width, obstacle.height, this);
                } else {
                    g.drawImage(flippedObstacleImage, obstacle.x, obstacle.y, obstacle.width, obstacle.height, this);
                }
            }
        }
        if (grapeImage != null) {
            for (Rectangle grape : grapes) {
                g.drawImage(grapeImage, grape.x, grape.y, grape.width, grape.height, this);
            }
        }
    }

    private void updateBirdPosition() { //оновлення позиції героя
        if (gameStarted && !gameOver) {
            vy += GRAVITY;
            birdY += vy;
            if (birdY + birdImage.getHeight() > 600) {
                birdY = 600 - birdImage.getHeight();
                vy = 0;
                gameOver = true;
                showRestartButton();
            } else if (birdY < 0) {
                birdY = 0;
                vy = 0;
            }


            List<Rectangle> newObstacles = new ArrayList<>();
            for (Rectangle obstacle : obstacles) {
                obstacle.x -= 5;
                if (obstacle.x + obstacle.width > 0) {
                    newObstacles.add(obstacle);
                }
                if (obstacle.intersects(new Rectangle(birdX, birdY, birdImage.getWidth(), birdImage.getHeight()))) {
                    gameOver = true;
                    showRestartButton();
                }
            }
            obstacles = newObstacles;


            List<Rectangle> newGrapes = new ArrayList<>();
            for (Rectangle grape : grapes) {
                grape.x -= 2;
                if (grape.x + grape.width > 0) {
                    newGrapes.add(grape);
                }
                if (grape.intersects(new Rectangle(birdX, birdY, birdImage.getWidth(), birdImage.getHeight()))) {
                    grapesCollected++;
                    newGrapes.remove(grape);
                }
            }
            grapes = newGrapes;


            if (obstacles.isEmpty() || obstacles.get(obstacles.size() - 1).x < 600) {
                int gapHeight = 300;
                int minObstacleHeight = 50;
                int maxObstacleHeight = 300;

                int obstacleHeight = minObstacleHeight + (int) (Math.random() * (maxObstacleHeight - minObstacleHeight));
                int upperObstacleHeight = obstacleHeight;
                int lowerObstacleHeight = 600 - upperObstacleHeight - gapHeight;

                if (lowerObstacleHeight < minObstacleHeight) {
                    lowerObstacleHeight = minObstacleHeight;
                    upperObstacleHeight = 600 - lowerObstacleHeight - gapHeight;
                }

                int obstacleWidth = obstacleImage.getWidth();

                obstacles.add(new Rectangle(800, 0, obstacleWidth, upperObstacleHeight));
                obstacles.add(new Rectangle(800, 600 - lowerObstacleHeight, obstacleWidth, lowerObstacleHeight));


                if (grapeImage != null && random.nextInt(5) == 0) {
                    int grapeWidth = grapeImage.getWidth();
                    int grapeHeight = grapeImage.getHeight();
                    int grapeX = 800 + obstacleWidth / 2 - grapeWidth / 2;
                    int grapeY = upperObstacleHeight + random.nextInt(gapHeight - grapeHeight);

                    grapes.add(new Rectangle(grapeX, grapeY, grapeWidth, grapeHeight));
                }
            }
        }
        repaint();
    }

    private void startMovement() { //початок
        if (gameTimer != null) {
            gameTimer.stop();
        }
        gameTimer = new Timer(30, e -> updateBirdPosition());
        gameTimer.start();
    }

    @Override
    public void removeNotify() { //умова старту-кінця
        super.removeNotify();
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }
}

