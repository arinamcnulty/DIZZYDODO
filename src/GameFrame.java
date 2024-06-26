import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);
    private AudioPlayer audioPlayer;

    public GameFrame() {
        setTitle("Flappy Bird Game");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        audioPlayer = new AudioPlayer();
        audioPlayer.playMusic("src/Angry-Birds-Theme-Song.wav");

        MainMenuPanel mainMenu = new MainMenuPanel(this, "/fonmainmenu.jpg");
        cardPanel.add(mainMenu, "MainMenu");
        // Создание уровней игры
        cardPanel.add(new IndianSummerLevel(this), "IndianSummerLevel");
        cardPanel.add(new UnderworldLevel(this), "UnderworldLevel");
        cardPanel.add(new WaterWorldLevel(this), "WaterWorldLevel");
        cardPanel.add(new ChampagneFactoryLevel(this), "ChampagneFactoryLevel");
        cardPanel.add(new CryptoLevel(this), "CryptoLevel");
        cardPanel.add(new ScoresPanel(this), "Scores");
        cardPanel.add(new InfoPanel(this, "/infoBackground.jpg"), "InfoPanel");
        getContentPane().add(cardPanel);
        setVisible(true);
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public void switchTo(String cardName) {
        cardLayout.show(cardPanel, cardName);
        audioPlayer.playLevelMusic(cardName);  // Play the music associated with the level
        cardLayout.show(cardPanel, cardName);
        if (cardName.equals("Scores")) {
            ((ScoresPanel) cardPanel.getComponent(6)).repaint();
        }
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}
