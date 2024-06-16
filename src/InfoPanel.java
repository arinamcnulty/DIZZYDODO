import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private GameFrame gameFrame;
    private Image backgroundImage;

    public InfoPanel(GameFrame gameFrame, String fileName) {
        this.gameFrame = gameFrame;
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/INFO.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setLayout(new BorderLayout());
        addBackButton();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    private void addBackButton() {
        JButton backButton = new JButton("Назад");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.addActionListener(e -> gameFrame.switchTo("MainMenu"));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);
    }
}
