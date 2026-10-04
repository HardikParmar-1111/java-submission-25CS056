import java.util.Scanner;

record Vehicle(String number, String type) {}

public class TollBooth {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int totalToll = 0;
        int bikeCount = 0;
        int carCount = 0;
        int truckCount = 0;

        while (true) {
            System.out.println("Enter vehicle number or done:");
            String number = sc.nextLine().trim();

            if (number.equalsIgnoreCase("done")) {
                break;
            }

            System.out.println("Enter vehicle type:");
            String type = sc.nextLine().trim().toLowerCase();
            Vehicle vehicle = new Vehicle(number, type);

            int toll = switch (vehicle.type()) {
                case "bike" -> 20;
                case "car" -> 50;
                case "truck" -> 150;
                default -> {
                    System.out.println("Invalid vehicle type.");
                    yield 0;
                }
            };

            if (toll == 0) {
                continue;
            }
            totalToll += toll;

            switch (vehicle.type()) {
                case "bike" -> bikeCount++;
                case "car" -> carCount++;
                case "truck" -> truckCount++;
            }
        }

        sc.close();
        System.out.println("Total toll: " + totalToll);
        if (bikeCount == 0 && carCount == 0 && truckCount == 0) {
            System.out.println("Most frequent: none");
        } else if (bikeCount >= carCount && bikeCount >= truckCount) {
            System.out.println("Most frequent: bike");
        } else if (carCount >= truckCount) {
            System.out.println("Most frequent: car");
        } else {
            System.out.println("Most frequent: truck");
        }
    }
}
