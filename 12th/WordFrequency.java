import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordFrequency {
    public static void main(String[] args) {
        String paragraph = "Java is simple and Java is useful for learning Java";
        Map<String, Integer> counts = new HashMap<>();

        for (String word : paragraph.toLowerCase().split("[^a-z]+")) {
            if (!word.isEmpty()) {
                counts.put(word, counts.getOrDefault(word, 0) + 1);
            }
        }

        List<Map.Entry<String, Integer>> words =
                new ArrayList<>(counts.entrySet());
        words.sort(Comparator.comparingInt(
                (Map.Entry<String, Integer> entry) -> entry.getValue())
                .reversed().thenComparing(Map.Entry::getKey));

        for (Map.Entry<String, Integer> entry : words) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
