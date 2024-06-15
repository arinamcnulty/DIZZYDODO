import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;


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
        audioPlayer.playMusic("Angry-Birds-Theme-Song.wav"); // Specify the correct path to your audio file

        MainMenuPanel mainMenu = new MainMenuPanel(this, "/fonmainmenu.jpg");
        cardPanel.add(mainMenu, "MainMenu");
        // Создание уровней игры
        cardPanel.add(new IndianSummerLevel(this), "IndianSummerLevel");
        cardPanel.add(new UnderworldLevel(this), "UnderworldLevel");
        cardPanel.add(new WaterWorldLevel(this), "WaterWorldLevel");
        cardPanel.add(new ChampagneFactoryLevel(this), "ChampagneFactoryLevel");
        cardPanel.add(new CryptoLevel(this), "CryptoLevel");
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
