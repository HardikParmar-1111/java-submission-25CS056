import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class UniqueVisitors {
    public static void main(String[] args) {
        List<String> logins = Arrays.asList(
                "hardik", "jay", "hardik", "kirtan", "ansh", "jay", "krupal");
        Set<String> unique = new HashSet<>(logins);
        Set<String> inOrder = new LinkedHashSet<>(logins);

        System.out.println("Unique visitors: " + unique.size());
        int shown = 0;
        for (String name : inOrder) {
            if (shown == 3) {
                break;
            }
            System.out.println(name);
            shown++;
        }
    }
}
