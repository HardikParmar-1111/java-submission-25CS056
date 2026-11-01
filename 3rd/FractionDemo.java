public class FractionDemo {
    public static void main(String[] args) {
        Fraction first = new Fraction(1, 2);
        Fraction second = new Fraction(2, 4);
        Fraction third = new Fraction(3, 6);

        System.out.println(first);
        System.out.println(second);
        System.out.println(third);
        System.out.println(first.equals(second));
        System.out.println(second.equals(third));
    }
}
