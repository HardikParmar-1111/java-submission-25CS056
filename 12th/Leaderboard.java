import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Leaderboard {
    public static void main(String[] args) {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Amit", 90);
        scores.put("Hardik", 95);
        scores.put("Jay", 95);
        scores.put("Kirtan", 82);
        int top = 3;

        List<Map.Entry<String, Integer>> players =
                new ArrayList<>(scores.entrySet());
        players.sort(Comparator.comparingInt(
                (Map.Entry<String, Integer> entry) -> entry.getValue())
                .reversed().thenComparing(Map.Entry::getKey));

        for (int i = 0; i < Math.min(top, players.size()); i++) {
            Map.Entry<String, Integer> player = players.get(i);
            System.out.println(player.getKey() + ": " + player.getValue());
        }
    }
}
