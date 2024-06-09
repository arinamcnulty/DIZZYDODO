import java.util.HashMap;
import java.util.Map;

public class GameScores {
    private static Map<String, Integer> highScores = new HashMap<>();

    public static void updateScore(String level, int score) {
        if (score > highScores.getOrDefault(level, 0)) {
            highScores.put(level, score);
        }
    }

    public static int getHighScore(String level) {
        return highScores.getOrDefault(level, 0);
    }

    public static Map<String, Integer> getAllScores() {
        return new HashMap<>(highScores);
    }
}
