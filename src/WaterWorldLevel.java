import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class WaterWorldLevel extends GameLevelPanel {
    private BufferedImage backgroundImage;

    public WaterWorldLevel(GameFrame gameFrame) {
        super(gameFrame);
        loadBackgroundImage();
    }

    private void loadBackgroundImage() {
        try {
            // Загрузка фонового изображения
            backgroundImage = ImageIO.read(getClass().getResource("/waterworldlevelFON.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            backgroundImage = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Отрисовка изображения на весь компонент
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
