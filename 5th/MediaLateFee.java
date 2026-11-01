abstract class Media {
    private String title;

    Media(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    abstract double lateFee(int lateDays);
}

class Book extends Media {
    Book(String title) {
        super(title);
    }

    double lateFee(int lateDays) {
        return lateDays * 1.0;
    }
}

class DVD extends Media {
    DVD(String title) {
        super(title);
    }

    double lateFee(int lateDays) {
        return lateDays * 2.0;
    }
}

class Game extends Media {
    Game(String title) {
        super(title);
    }

    double lateFee(int lateDays) {
        return lateDays * 3.0;
    }
}

public class MediaLateFee {
    public static void main(String[] args) {
        Media[] returned = {
            new Book("Java Basics"),
            new DVD("Nature Film"),
            new Game("Racing Game")
        };
        int[] lateDays = {3, 2, 4};
        double total = 0;

        for (int i = 0; i < returned.length; i++) {
            double fee = returned[i].lateFee(lateDays[i]);
            total += fee;
            System.out.println(returned[i].getTitle() + ": " + fee);
        }
        System.out.println("Total late fee: " + total);
    }
}
