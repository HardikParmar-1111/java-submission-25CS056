import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LogAnalyzer {
    public static void main(String[] args) {
        Path file = Path.of(args.length == 0 ? "11th/sample.log" : args[0]);
        String keyword = args.length < 2 ? "error" : args[1];

        try {
            List<String> lines = Files.readAllLines(file);
            long matches = lines.stream()
                    .filter(line -> line.toLowerCase().contains(keyword.toLowerCase()))
                    .count();
            System.out.println("Lines: " + lines.size());
            System.out.println("Matches for " + keyword + ": " + matches);
        } catch (IOException e) {
            System.out.println("Could not read log file: " + e.getMessage());
        }
    }
}
