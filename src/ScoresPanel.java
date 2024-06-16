import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Map;

public class ScoresPanel extends JPanel {
    private BufferedImage backgroundImage;
    private GameFrame gameFrame;

    public ScoresPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setLayout(new BorderLayout());
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/FONscores.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            backgroundImage = null;
        }
        addBackButton();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        int y = 100;
        Map<String, Integer> scores = GameScores.getAllScores();
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            g.drawString(entry.getKey() + ": " + entry.getValue(), 50, y);
            y += 30;
        }
    }

    private void addBackButton() {
        JButton backButton = new JButton("Назад");
        backButton.addActionListener(e -> gameFrame.switchTo("MainMenu"));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);
    }
}
