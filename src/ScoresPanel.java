import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ScoresPanel extends JPanel {
    private BufferedImage backgroundImage;
    private GameFrame gameFrame;

    public ScoresPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setLayout(new BorderLayout());
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/FONscoresFON.png"));
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

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));

        FontMetrics fm = g.getFontMetrics();
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        Map<String, Integer> scoresMap = GameScores.getAllScores();
        List<Map.Entry<String, Integer>> scoresList = scoresMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        int totalHeight = fm.getHeight() * scoresList.size();
        int y = (panelHeight - totalHeight) / 2; // Відцентрувати по вертикалі

        for (Map.Entry<String, Integer> entry : scoresList) {
            String scoreText = entry.getKey() + ": " + entry.getValue();
            int textWidth = fm.stringWidth(scoreText);
            int x = (panelWidth - textWidth) / 2; // Відцентрувати по горизонталі
            g.drawString(scoreText, x, y);
            y += fm.getHeight();
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
