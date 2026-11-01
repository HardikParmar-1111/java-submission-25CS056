public class ParkingLot {
    private int twoWheelers;
    private int fourWheelers;
    private final int twoCap;
    private final int fourCap;
    private static long revenue = 0;

    public ParkingLot(int twoCap, int fourCap) {
        this.twoCap = twoCap;
        this.fourCap = fourCap;
        this.twoWheelers = 0;
        this.fourWheelers = 0;
    }

    public void park(String type) {
        if (type.equalsIgnoreCase("two")) {
            if (twoWheelers < twoCap) {
                twoWheelers++;
                revenue += 20;
                System.out.println("Two Wheeler Parked");
            } else {
                System.out.println("Full");
            }
        } else if (type.equalsIgnoreCase("four")) {
            if (fourWheelers < fourCap) {
                fourWheelers++;
                revenue += 40;
                System.out.println("Four Wheeler Parked");
            } else {
                System.out.println("Full");
            }
        } else {
            System.out.println("Invalid vehicle type");
        }
    }

    public void leave(String type) {
        if (type.equalsIgnoreCase("two")) {
            if (twoWheelers > 0) {
                twoWheelers--;
                System.out.println("Two Wheeler left");
            } else {
                System.out.println("No two-wheelers parked");
            }
        } else if (type.equalsIgnoreCase("four")) {
            if (fourWheelers > 0) {
                fourWheelers--;
                System.out.println("Four Wheeler left");
            } else {
                System.out.println("No four-wheelers parked");
            }
        } else {
            System.out.println("Invalid vehicle type");
        }
    }

    public static long getRevenue() {
        return revenue;
    }

    public static void main(String[] args) {
        ParkingLot p = new ParkingLot(2, 1);

        p.park("two");
        p.park("two");
        p.park("two");

        p.park("four");
        p.park("four");

        p.leave("two");
        p.leave("four");

        System.out.println("Two Wheelers: " + p.twoWheelers);
        System.out.println("Four Wheelers: " + p.fourWheelers);
        System.out.println("Revenue: " + ParkingLot.getRevenue());
    }
}