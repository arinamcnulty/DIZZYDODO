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

public class CryptoLevel extends GameLevelPanel {
    private String levelName = "Crypto";
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
    private boolean flashState = false;  // Для ефекту миготіння
    private JButton restartButton;
    private Timer gameTimer;
    private Timer flashTimer;  // Таймер для ефекту миготіння
    private List<Rectangle> obstacles;
    private List<Rectangle> grapes;
    private int grapesCollected = 0;
    private Random random;

    public CryptoLevel(GameFrame gameFrame) {
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
        initFlashTimer();
    }
    private void initFlashTimer() {
        // Визначаємо рандомний інтервал часу від 5 до 10 секунд
        int delay = new Random().nextInt(5001) + 5000;  // 5000 до 10000 мілісекунд (5 до 10 секунд)
        flashTimer = new Timer(delay, e -> {
            flashState = !flashState;  // Зміна стану миготіння
            repaint();

            // Запускаємо таймер, щоб вимкнути миготіння через 100 мілісекунд
            new Timer(100, event -> {
                flashState = false;
                repaint();
                ((Timer)event.getSource()).stop();  // Зупиняємо таймер вимкнення миготіння
            }).start();

            // Рестартуємо таймер миготіння з новим інтервалом
            ((Timer)e.getSource()).setInitialDelay(new Random().nextInt(5001) + 5000);
            ((Timer)e.getSource()).restart();
        });
        flashTimer.setRepeats(false);  // Забезпечуємо, що таймер спрацьовує лише один раз за інтервал
        flashTimer.start();
    }


    @Override
    public void addNotify() {
        super.addNotify();
        resetGame();
        startMovement();
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/CryptoFON.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            backgroundImage = null;
        }
    }

    private void loadBirdImage() {
        try {
            birdImage = ImageIO.read(getClass().getResource("/sticker,375x360.u2.png"));
            birdImage = scaleImage(birdImage, 0.15);
        } catch (IOException e) {
            e.printStackTrace();
            birdImage = null;
        }
    }

    private void loadObstacleImage() {
        try {
            obstacleImage = ImageIO.read(getClass().getResource("столб.jpg"));
            obstacleImage = scaleImage(obstacleImage, 0.50);
            flippedObstacleImage = flipImageVertically(obstacleImage);
        } catch (IOException e) {
            e.printStackTrace();
            obstacleImage = null;
        }
    }

    private void loadGrapeImage() {
        try {
            grapeImage = ImageIO.read(getClass().getResource("/cryptostick.jpg"));
            grapeImage = scaleImage(grapeImage, 0.15);
        } catch (IOException e) {
            e.printStackTrace();
            grapeImage = null;
        }
    }

    private BufferedImage scaleImage(BufferedImage srcImg, double factor) {
        int scaledWidth = (int) (srcImg.getWidth() * factor);
        int scaledHeight = (int) (srcImg.getHeight() * factor);
        BufferedImage scaledImg = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaledImg.createGraphics();
        g2.drawImage(srcImg, 0, 0, scaledWidth, scaledHeight, null);
        g2.dispose();
        return scaledImg;
    }

    private BufferedImage flipImageVertically(BufferedImage srcImg) {
        int width = srcImg.getWidth();
        int height = srcImg.getHeight();
        BufferedImage flippedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = flippedImg.createGraphics();
        g2.drawImage(srcImg, 0, height, width, -height, null);
        g2.dispose();
        return flippedImg;
    }

    private void jump() {
        vy = JUMP_STRENGTH;
    }

    private void initRestartButton() {
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

    private void showRestartButton() {
        if (restartButton != null) {
            restartButton.setVisible(true);
            restartButton.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Ви зібрали " + grapesCollected + " монет!");
            updateScore(levelName, grapesCollected); // Оновлення скорсу після завершення гри
        }
    }


    public void resetGame() {
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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (flashState) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
        } else {
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
            if (birdImage != null) {
                g.drawImage(birdImage, birdX, birdY, this);
            }
            if (obstacleImage != null) {
                for (Rectangle obstacle : obstacles) {
                    if (obstacle.y == 0) {
                        g.drawImage(obstacleImage, obstacle.x, obstacle.y, obstacle.width, obstacle.height, this); // Верхнее препятствие
                    } else {
                        g.drawImage(flippedObstacleImage, obstacle.x, obstacle.y, obstacle.width, obstacle.height, this); // Нижнее препятствие
                    }
                }
            }
            if (grapeImage != null) {
                for (Rectangle grape : grapes) {
                    g.drawImage(grapeImage, grape.x, grape.y, grape.width, grape.height, this);
                }
            }
        }
    }

    private void updateBirdPosition() {
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

    private void startMovement() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        gameTimer = new Timer(30, e -> updateBirdPosition());
        gameTimer.start();
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }
}
