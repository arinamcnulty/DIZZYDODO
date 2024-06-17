/*
Клас для взаємодії між рівнями та головним екраном-меню
 */
import javax.swing.*;
import java.awt.*;

public abstract class GameLevelPanel extends JPanel {
    protected GameFrame gameFrame;

    protected void updateScore(String levelName, int score) { //оновлення балів
        GameScores.updateScore(levelName, score);
    }

    public GameLevelPanel(GameFrame gameFrame) { //панель
        this.gameFrame = gameFrame;
        setLayout(new BorderLayout());
        addBackButton();
    }

    private void addBackButton() { //кнопка назад
        JButton backButton = new JButton("Назад");
        backButton.addActionListener(e -> gameFrame.switchTo("MainMenu"));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);
    }

    public abstract void resetGame();
}
