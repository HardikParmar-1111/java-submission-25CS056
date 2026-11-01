import java.util.Arrays;
import java.util.List;

public class BoundedStatsDemo {
    public static <T extends Number> double average(List<T> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("List cannot be empty");
        }

        double total = 0;
        for (T value : values) {
            total += value.doubleValue();
        }
        return total / values.size();
    }

    public static void main(String[] args) {
        List<Integer> marks = Arrays.asList(70, 80, 90);
        List<Double> prices = Arrays.asList(10.5, 15.0, 20.5);
        System.out.println("Average marks: " + average(marks));
        System.out.println("Average price: " + average(prices));
    }
}
