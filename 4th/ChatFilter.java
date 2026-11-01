import java.util.Scanner;
import java.util.regex.Pattern;

class ChatlogFilter {
    public static String[] splitLine(String line){
        String[] parts = line.split("\\s+", 3);
        if(parts.length <=2){
            return null;
        }
        return parts;
    }

    public static boolean hasKeyword(String message, String keyword){
        return message.toLowerCase().contains(keyword.toLowerCase());
    }

    public static String maskKeyword(String message, String keyword) {
        return message.replaceAll("(?i)" + Pattern.quote(keyword),
                "*".repeat(keyword.length()));
    }
}

public class ChatFilter {
    public static void main(String[] args) {
        String[] messages = {
            "10:05 hardik how are you?",
            "10:06 jay thanks! How about you?",
            "10:07 kirtan, Just wanted to check in.",
            "10:08 krupal, Let's catch up soon.",
            "10:09 ansh, let's plan for this stupid weekend.",
        };
        Scanner sc= new Scanner(System.in);
        System.out.println("Enter word to filter:");
        String input = sc.nextLine();
        
        int match =0;
        StringBuilder report = new StringBuilder();

        for (String message : messages) {
            String[] parts = ChatlogFilter.splitLine(message);
            if(parts == null){
                continue;
            }
            if(ChatlogFilter.hasKeyword(parts[2], input)){
                match++;
            String filtered = ChatlogFilter.maskKeyword(parts[2], input);
            report.append(parts[0]).append(" ").append(parts[1]).append(" ")
                    .append(filtered).append("\n");
        }
    }
    System.out.println("Matches Found: " + match);
    System.out.println(report);
    sc.close();
}
}
