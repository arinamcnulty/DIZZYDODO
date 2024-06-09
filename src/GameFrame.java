import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);

    public GameFrame() {
        setTitle("Flappy Bird Game");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        MainMenuPanel mainMenu = new MainMenuPanel(this, "/fonmainmenu.jpg");
        cardPanel.add(mainMenu, "MainMenu");
        // Создание уровней игры
        cardPanel.add(new IndianSummerLevel(this), "IndianSummerLevel");
        cardPanel.add(new UnderworldLevel(this), "UnderworldLevel");
        cardPanel.add(new WaterWorldLevel(this), "WaterWorldLevel");
        cardPanel.add(new ChampagneFactoryLevel(this), "ChampagneFactoryLevel");
        cardPanel.add(new ScoresPanel(this), "Scores");
        getContentPane().add(cardPanel);
        setVisible(true);
    }
    public void switchTo(String cardName) {
        cardLayout.show(cardPanel, cardName);
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}
