import java.util.Scanner;

class ChatlogFilter {
    public static String[] splitLine(String Line){
        String[] parts = Line.split(" ", 3);
        if(parts.length <=2){
            return null;
        }
        return parts;
    }
    public static boolean hasKeyword(String message, String keyword){
        return message.toLowerCase().contains(keyword.toLowerCase());
    }
}

public class ChatFilter {
    public static void main(String[] args) {
        String[] messages = {
            "10:05 hardik how are you?",
            "10:06 jay thanks! How about you?",
            "10:07 kirtan, Just wanted to check in.",
            "10:08 krupal, Let's catch up soon.",
            "10:09 ansh, let's plan for this weekend.",
        };
        java.util.Scanner sc= new java.util.Scanner(System.in);
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
                report.append(parts[0]).append(" ").append(parts[1]).append(" ; ").append(parts[2]).append("\n");
            }
            
            System.out.println(parts[2]);
}
    System.out.println("Matche Found: " + match + " " + report);
}
}
