package math.numbers;

import java.util.Comparator;
import java.util.Objects;

public class Rational {

    public final long numer;
    public final long denom;

    public Rational(long n, long d) {
        if (d == 0) {
            throw new ArithmeticException("denom %d cannot be zero. (%d/%d)".formatted(d, n, d));
        }
        var g = gcd(Math.abs(n), Math.abs(d));
        numer = n / g;
        denom = d / g;
    }

    private static Long gcd(long a, long b) {
        if (b == 0) {
            return a;
        } else {
            return gcd(b, a % b);
        }
    }

    public static Field<Rational> field = new Field<>() {
        @Override
        public Rational addZero() {
            return new Rational(0, 1);
        }

        @Override
        public Rational mulZero() {
            return new Rational(1, 1);
        }

        @Override
        public Rational add(Rational a, Rational b) {
            return new Rational((a.numer * b.denom) + (b.numer * a.denom), a.denom * b.denom);
        }

        @Override
        public Rational multiply(Rational a, Rational b) {
            return new Rational(a.numer * b.numer, a.denom * b.denom);
        }

        @Override
        public Rational addInverse(Rational a) {
            return new Rational(-a.numer, a.denom);
        }

        @Override
        public Rational mulInverse(Rational a) throws ArithmeticException {
            return new Rational(a.denom, a.numer);
        }
    };

    public static Distance<Rational> distance = new Distance<>() {
        @Override
        public Rational calc(Rational a, Rational b) {
            var d = field.minus(a, b);
            return new Rational(Math.abs(d.numer), d.denom);
        }
    };

    public static Comparator<Rational> comparator = new Comparator<Rational>() {
        @Override
        public int compare(Rational o1, Rational o2) {
            return Long.compare(o1.numer * o2.denom, o2.numer * o1.denom);
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rational rational = (Rational) o;
        return numer == rational.numer && denom == rational.denom;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numer, denom);
    }

    @Override
    public String toString() {
        return "Rational{" +
                "numer=" + numer +
                ", denom=" + denom +
                '}';
    }
}
