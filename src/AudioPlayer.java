import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AudioPlayer {
    private Clip clip;
    private Map<String, String> musicPaths;

    public AudioPlayer() {
        // Initialize the music paths for each level
        musicPaths = new HashMap<>();
        musicPaths.put("MainMenu", "src/Angry-Birds-Theme-Song.wav");
        musicPaths.put("IndianSummerLevel", "src/President-Coco-Jambo.wav");
        musicPaths.put("ChampagneFactoryLevel", "src/Riikka-Ievan_Polkka-world.wav");
        musicPaths.put("UnderworldLevel", "src/Guenta-K.-Das-Boot.wav");
        musicPaths.put("Scores", "src/Guenta-K.-Das-Boot.wav");
        musicPaths.put("CryptoLevel", "src/Los-Del-RioMacarena.wav");
        musicPaths.put("WaterWorldLevel", "src/Los-Del-RioMacarena.wav");
    }

    public void playLevelMusic(String levelName) {
        if (musicPaths.containsKey(levelName)) {
            playMusic(musicPaths.get(levelName));
        } else {
            System.out.println("No music found for: " + levelName);
        }
    }

    public void playMusic(String filePath) {
        try {
            if (clip != null && clip.isRunning()) {
                clip.stop();  // Stop the currently playing music
                clip.close(); // Close the clip to release resources
            }
            File musicPath = new File(filePath);
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                System.out.println("File does not exist: " + filePath);
            }
        } catch (Exception e) {
            System.out.println("Error playing file: " + filePath);
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}
