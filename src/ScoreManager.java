import java.util.HashMap;
import java.util.Map;

public class ScoreManager {
    private static final Map<String, Integer> bestScores = new HashMap<>();

    // Инициализация начальных значений
    static {
        bestScores.put("Indian Summer Level", 0);
        bestScores.put("Underworld Level", 0);
        bestScores.put("Water World Level", 0);
        bestScores.put("Champagne Factory Level", 0);
    }

    // Обновление счета только если он лучше предыдущего
    public static void updateScore(String levelName, int score) {
        if (bestScores.get(levelName) == null || score > bestScores.get(levelName)) {
            bestScores.put(levelName, score);
        }
    }

    // Получение карты лучших результатов
    public static Map<String, Integer> getBestScores() {
        return new HashMap<>(bestScores);
    }
}
