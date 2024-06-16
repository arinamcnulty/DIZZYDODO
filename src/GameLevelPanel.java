import javax.swing.*;
import java.awt.*;

public abstract class GameLevelPanel extends JPanel {
    protected GameFrame gameFrame;

    protected void updateScore(String levelName, int score) {
        GameScores.updateScore(levelName, score);
    }

    public GameLevelPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setLayout(new BorderLayout());
        addBackButton();
    }

    private void addBackButton() {
        JButton backButton = new JButton("Назад");
        backButton.addActionListener(e -> gameFrame.switchTo("MainMenu"));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);
    }

    public abstract void resetGame();
}
