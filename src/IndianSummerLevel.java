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
    private BufferedImage backgroundImage;
    private BufferedImage birdImage;
    private BufferedImage obstacleImage;
    private BufferedImage flippedObstacleImage;
    private BufferedImage grapeImage;
    private int birdY;
    private int birdX;
    private float vy = 0; // Вертикальная скорость птички
    private final float GRAVITY = 1.0f; // Константа гравитации, увеличьте для более быстрого падения
    private final int JUMP_STRENGTH = -8; // Сила подъема, увеличьте для более быстрого подъема
    private boolean gameStarted = false; // Состояние игры
    private boolean gameOver = false; // Состояние завершения игры
    private JButton restartButton; // Кнопка перезапуска
    private Timer gameTimer;
    private List<Rectangle> obstacles; // Список препятствий
    private List<Rectangle> grapes; // Список виноградинок
    private int grapesCollected = 0; // Количество собранных виноградинок
    private Random random;

    public IndianSummerLevel(GameFrame gameFrame) {
        super(gameFrame);
        loadBackgroundImage();
        loadBirdImage();
        loadObstacleImage();
        loadGrapeImage();
        setFocusable(true);
        requestFocusInWindow(); // Запрос на фокус
        random = new Random();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!gameStarted && !gameOver) {
                    gameStarted = true;
                }
                if (!gameOver) {
                    jump(); // Подъем птички при нажатии на экран
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

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/indiansummerFON.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            backgroundImage = null;
        }
    }

    private void loadBirdImage() {
        try {
            birdImage = ImageIO.read(getClass().getResource("/sticker,375x360.u2.png"));
            birdImage = scaleImage(birdImage, 0.15); // Масштабирование изображения
        } catch (IOException e) {
            e.printStackTrace();
            birdImage = null;
        }
    }

    private void loadObstacleImage() {
        try {
            obstacleImage = ImageIO.read(getClass().getResource("/бочка.png"));
            obstacleImage = scaleImage(obstacleImage, 0.35); // Масштабирование изображения
            flippedObstacleImage = flipImageVertically(obstacleImage); // Переворачиваем изображение для нижних препятствий
        } catch (IOException e) {
            e.printStackTrace();
            obstacleImage = null;
        }
    }

    private void loadGrapeImage() {
        try {
            grapeImage = ImageIO.read(getClass().getResource("/виноград.png"));
            grapeImage = scaleImage(grapeImage, 0.15); // Масштабирование изображения виноградинки
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
        vy = JUMP_STRENGTH; // Поднимаем птичку вверх при нажатии
    }

    private void initRestartButton() {
        restartButton = new JButton("ПЕРЕЗАПУСТИТИ");
        restartButton.setFont(new Font("Arial", Font.BOLD, 14));
        restartButton.setVisible(false);
        restartButton.setEnabled(false);
        restartButton.setPreferredSize(new Dimension(200, 50)); // Задаем размер кнопки
        restartButton.addActionListener(e -> {
            resetGame(); // Перезапуск игры
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
            JOptionPane.showMessageDialog(this, "Ви зібрали " + grapesCollected + " виноградинок!");
        }
    }

    public void resetGame() {
        birdX = 800 / 2 - birdImage.getWidth() / 2; // Начальная позиция по X
        birdY = 600 / 2 - birdImage.getHeight() / 2; // Начальная позиция по Y
        vy = 0; // Сброс вертикальной скорости
        gameStarted = false; // Сброс состояния игры
        gameOver = false; // Сброс состояния завершения игры
        obstacles = new ArrayList<>(); // Очистка препятствий
        grapes = new ArrayList<>(); // Очистка виноградинок
        grapesCollected = 0; // Сброс количества собранных виноградинок
        if (restartButton != null) {
            restartButton.setVisible(false);
            restartButton.setEnabled(false);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
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

    private void updateBirdPosition() {
        if (gameStarted && !gameOver) {
            vy += GRAVITY; // Гравитация увеличивает скорость падения
            birdY += vy;
            if (birdY + birdImage.getHeight() > 600) { // Проверка касания нижней части экрана
                birdY = 600 - birdImage.getHeight(); // Предотвращаем выход за пределы экрана
                vy = 0; // Останавливаем птичку, если она достигла дна
                gameOver = true; // Останавливаем игру
                showRestartButton(); // Показываем кнопку перезапуска
            } else if (birdY < 0) {
                birdY = 0; // Предотвращаем выход за верхнюю границу
                vy = 0; // Останавливаем птичку, если она достигла верха
            }

            // Обновляем позиции препятствий
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

            // Обновляем позиции виноградинок
            List<Rectangle> newGrapes = new ArrayList<>();
            for (Rectangle grape : grapes) {
                grape.x -= 2; // Виноградинки движутся медленнее, чем препятствия
                if (grape.x + grape.width > 0) {
                    newGrapes.add(grape);
                }
                if (grape.intersects(new Rectangle(birdX, birdY, birdImage.getWidth(), birdImage.getHeight()))) {
                    grapesCollected++;
                    newGrapes.remove(grape); // Удаляем виноградинку, если птичка ее собрала
                }
            }
            grapes = newGrapes;

            // Добавляем новые препятствия и виноградинки
            if (obstacles.isEmpty() || obstacles.get(obstacles.size() - 1).x < 600) {
                int gapHeight = 300; // Высота промежутка между верхним и нижним препятствиями
                int minObstacleHeight = 50; // Минимальная высота препятствия
                int maxObstacleHeight = 300; // Максимальная высота препятствия

                int obstacleHeight = minObstacleHeight + (int) (Math.random() * (maxObstacleHeight - minObstacleHeight));
                int upperObstacleHeight = obstacleHeight;
                int lowerObstacleHeight = 600 - upperObstacleHeight - gapHeight;

                if (lowerObstacleHeight < minObstacleHeight) {
                    lowerObstacleHeight = minObstacleHeight;
                    upperObstacleHeight = 600 - lowerObstacleHeight - gapHeight;
                }

                int obstacleWidth = obstacleImage.getWidth();

                obstacles.add(new Rectangle(800, 0, obstacleWidth, upperObstacleHeight)); // Верхнее препятствие
                obstacles.add(new Rectangle(800, 600 - lowerObstacleHeight, obstacleWidth, lowerObstacleHeight)); // Нижнее препятствие

                // Добавляем виноградинки с меньшей частотой
                if (grapeImage != null && random.nextInt(5) == 0) { // Уменьшение частоты появления виноградинок
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

