import java.util.Objects;

public class Fraction {
    private int num;
    private int den;

    public Fraction(int num, int den) {
        if (den == 0) {
            throw new IllegalArgumentException("Denominator cannot be zero");
        }

        int gcd = gcd(Math.abs(num), Math.abs(den));
        this.num = num / gcd;
        this.den = den / gcd;

        if (this.den < 0) {
            this.num = -this.num;
            this.den = -this.den;
        }
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a == 0 ? 1 : a;
    }

    @Override
    public String toString() {
        return num + "/" + den;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Fraction)) {
            return false;
        }
        Fraction other = (Fraction) obj;
        return num == other.num && den == other.den;
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, den);
    }
}
