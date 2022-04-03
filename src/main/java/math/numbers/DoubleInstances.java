package math.numbers;

import java.util.Comparator;

public class DoubleInstances {

    public static Field<Double> field = new Field<Double>() {
        @Override
        public Double addZero() {
            return 0.0;
        }

        @Override
        public Double mulZero() {
            return 1.0;
        }

        @Override
        public Double add(Double a, Double b) {
            return a + b;
        }

        @Override
        public Double multiply(Double a, Double b) {
            return a * b;
        }

        @Override
        public Double addInverse(Double a) {
            return -a;
        }

        @Override
        public Double mulInverse(Double a) throws ArithmeticException {
            return 1.0 / a;
        }
    };

    public static Distance<Double> distance = new Distance<Double>() {
        @Override
        public Double calc(Double a, Double b) {
            return Math.abs(a - b);
        }
    };

    public static Comparator<Double> comparator = new Comparator<Double>() {
        @Override
        public int compare(Double o1, Double o2) {
            return o1.compareTo(o2);
        }
    };

}
