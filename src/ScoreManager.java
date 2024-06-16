import java.util.HashMap;
import java.util.Map;

public class ScoreManager {
    private static final Map<String, Integer> bestScores = new HashMap<>();


    static {
        bestScores.put("Indian Summer Level", 0);
        bestScores.put("Underworld Level", 0);
        bestScores.put("Water World Level", 0);
        bestScores.put("Champagne Factory Level", 0);
    }


    public static void updateScore(String levelName, int score) {
        if (bestScores.get(levelName) == null || score > bestScores.get(levelName)) {
            bestScores.put(levelName, score);
        }
    }


    public static Map<String, Integer> getBestScores() {
        return new HashMap<>(bestScores);
    }
}
