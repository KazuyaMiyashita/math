package math.matrix;

import math.numbers.Rational;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LUPTest {

    @Test
    void calcDeterminant() {
        var matrix = Matrix.of(new double[][] {
                {1, 1, 1, 1, 1},
                {1, 2, 2, 2, 2},
                {1, 2, 3, 3, 3},
                {1, 2, 3, 4, 4},
                {1, 2, 3, 4, 5},
        });
        var lup = LUP.lupDecomposition(matrix);

        var expected = Optional.of(new LUP(
                Matrix.of(new double[][]{
                        {1.0, 0.0, 0.0, 0.0, 0.0},
                        {1.0, 1.0, 0.0, 0.0, 0.0},
                        {1.0, 1.0, 1.0, 0.0, 0.0},
                        {1.0, 1.0, 1.0, 1.0, 0.0},
                        {1.0, 1.0, 1.0, 1.0, 1.0},
                }),
                Matrix.of(new double[][] {
                        {1.0, 1.0, 1.0, 1.0, 1.0},
                        {0.0, 1.0, 1.0, 1.0, 1.0},
                        {0.0, 0.0, 1.0, 1.0, 1.0},
                        {0.0, 0.0, 0.0, 1.0, 1.0},
                        {0.0, 0.0, 0.0, 0.0, 1.0},
                }),
                Matrix.of(new double[][] {
                        {1.0, 0.0, 0.0, 0.0, 0.0},
                        {0.0, 1.0, 0.0, 0.0, 0.0},
                        {0.0, 0.0, 1.0, 0.0, 0.0},
                        {0.0, 0.0, 0.0, 1.0, 0.0},
                        {0.0, 0.0, 0.0, 0.0, 1.0},
                }),
                1.0
        ));
        assertEquals(expected, lup);
        assertEquals(1.0, lup.get().determinant());
        var l = lup.get().l();
        var u = lup.get().u();
        var p = lup.get().p();
        assertEquals(p.multiply(matrix), l.multiply(u));
    }

    @Test
    void calcDeterminant2() {
        var matrix = Matrix.of(new double[][] {
                {3, 1},
                {2, 5},
        });
        var lup = LUP.lupDecomposition(matrix);
        var expected = Optional.of(new LUP(
                Matrix.of(new double[][]{
                        {1.0, 0.0},
                        {0.6666666666666666, 1.0},
                }),
                Matrix.of(new double[][] {
                        {3.0, 1.0},
                        {0.0, 4.333333333333333},
                }),
                Matrix.of(new double[][] {
                        {1.0, 0.0},
                        {0.0, 1.0},
                }),
                1.0
        ));
        assertEquals(expected, lup);
        assertEquals(13.0, lup.get().determinant());
        var l = lup.get().l();
        var u = lup.get().u();
        var p = lup.get().p();
        assertEquals(p.multiply(matrix), l.multiply(u));
    }

    @Test
    void calcDeterminant3() {
        var matrix = Matrix.of(new double[][] {
                {1, 0, 3},
                {2, 1, 5},
                {7, 6, 4},
        });
        var lup = LUP.lupDecomposition(matrix);
        var expected = Optional.of(new LUP(
                Matrix.of(new double[][]{
                        {1.0, 0.0, 0.0},
                        {0.14285714285714285, 1.0, 0.0},
                        {0.2857142857142857, 0.8333333333333333, 1.0},
                }),
                Matrix.of(new double[][] {
                        {7.0, 6.0, 4.0},
                        {0.0, -0.8571428571428571, 2.428571428571429},
                        {0.0, 0.0, 1.8333333333333335},
                }),
                Matrix.of(new double[][] {
                        {0.0, 0.0, 1.0},
                        {1.0, 0.0, 0.0},
                        {0.0, 1.0, 0.0},
                }),
                1.0
        ));
        assertEquals(expected, lup);
        assertEquals(-11.0, lup.get().determinant());
        var l = lup.get().l();
        var u = lup.get().u();
        var p = lup.get().p();
        assertEquals(p.multiply(matrix), l.multiply(u));
    }

    @Test
    void calcDeterminant4() {
        var matrix = Matrix.of(new double[][] {
                {2, 3, 0, 1},
                {0, 7, 1 ,0},
                {0, 0, 3, 1},
                {0, 1, 5, 2},
        });
        var lup = LUP.lupDecomposition(matrix);
        var expected = Optional.of(new LUP<>(
                Matrix.of(new double[][]{
                        {1.0, 0.0, 0.0, 0.0},
                        {0.0, 1.0, 0.0, 0.0},
                        {0.0, 0.14285714285714285, 1.0, 0.0},
                        {0.0, 0.0, 0.6176470588235294, 1.0},
                }),
                Matrix.of(new double[][] {
                        {2.0, 3.0, 0.0, 1.0},
                        {0.0, 7.0, 1.0, 0.0},
                        {0.0, 0.0, 4.857142857142857, 2.0},
                        {0.0, 0.0, 0.0, -0.23529411764705888},
                }),
                Matrix.of(new double[][] {
                        {1.0, 0.0, 0.0, 0.0},
                        {0.0, 1.0, 0.0, 0.0},
                        {0.0, 0.0, 0.0, 1.0},
                        {0.0, 0.0, 1.0, 0.0},
                }),
                -1.0
        ));
        assertEquals(expected.get().pSign(), lup.get().pSign());

        assertEquals(expected, lup);
        assertEquals(16.000000000000004, lup.get().determinant()); // 誤差あり
        var l = lup.get().l();
        var u = lup.get().u();
        var p = lup.get().p();
        assertEquals(p.multiply(matrix), l.multiply(u));
    }

    @Test
    void calcDeterminant4_rational() {
        var matrix = new Matrix<>(new Rational[][] {
                {new Rational(2, 1), new Rational(3, 1), new Rational(0, 1), new Rational(1, 1)},
                {new Rational(0, 1), new Rational(7, 1), new Rational(1, 1) ,new Rational(0, 1)},
                {new Rational(0, 1), new Rational(0, 1), new Rational(3, 1), new Rational(1, 1)},
                {new Rational(0, 1), new Rational(1, 1), new Rational(5, 1), new Rational(2, 1)},
        }, Rational.field, Rational.distance, Rational.comparator);
        var lup = LUP.lupDecomposition(matrix);
        var expected = Optional.of(new LUP<Rational>(
                new Matrix<>(new Rational[][]{
                        {new Rational(1, 1), new Rational(0, 1), new Rational(0, 1), new Rational(0, 1)},
                        {new Rational(0, 1), new Rational(1, 1), new Rational(0, 1), new Rational(0, 1)},
                        {new Rational(0, 1), new Rational(1, 7), new Rational(1, 1), new Rational(0, 1)},
                        {new Rational(0, 1), new Rational(0, 1), new Rational(21, 34), new Rational(1, 1)}
                }, Rational.field, Rational.distance, Rational.comparator),
                new Matrix<>(new Rational[][]{
                        {new Rational(2, 1), new Rational(3, 1), new Rational(0, 1), new Rational(1, 1)},
                        {new Rational(0, 1), new Rational(7, 1), new Rational(1, 1), new Rational(0, 1)},
                        {new Rational(0, 1), new Rational(0, 1), new Rational(34, 7), new Rational(2, 1)},
                        {new Rational(0, 1), new Rational(0, 1), new Rational(0, 1), new Rational(-4, 17)},
                }, Rational.field, Rational.distance, Rational.comparator),
                new Matrix<>(new Rational[][]{
                        {new Rational(1, 1), new Rational(0, 1), new Rational(0, 1), new Rational(0, 1)},
                        {new Rational(0, 1), new Rational(1, 1), new Rational(0, 1), new Rational(0, 1)},
                        {new Rational(0, 1), new Rational(0, 1), new Rational(0, 1), new Rational(1, 1)},
                        {new Rational(0,  1), new Rational(0, 1), new Rational(1, 1), new Rational(0, 1)},
                }, Rational.field, Rational.distance, Rational.comparator),
                new Rational(-1, 1)
        ));
        assertEquals(expected.get().l(), lup.get().l());
        assertEquals(expected.get().u(), lup.get().u());
        assertEquals(expected.get().p(), lup.get().p());
        assertEquals(expected.get().pSign(), lup.get().pSign());

        assertEquals(expected, lup);
        assertEquals(new Rational(16, 1), lup.get().determinant());
        var l = lup.get().l();
        var u = lup.get().u();
        var p = lup.get().p();
        assertEquals(p.multiply(matrix), l.multiply(u));
    }

    @Test
    void a() {
        var r = new Rational(1, 1);
        System.out.println(r);
        System.out.println(Rational.field.addInverse(r));
    }

    @Test
    void calcDeterminant_large() {
        var matrix = Matrix.of(new double[][] {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 2, 2, 2, 2, 2, 2, 2, 2, 2},
                {1, 2, 3, 3, 3, 3, 3, 3, 3, 3},
                {1, 2, 3, 4, 4, 4, 4, 4, 4, 4},
                {1, 2, 3, 4, 5, 5, 5, 5, 5, 5},
                {1, 2, 3, 4, 5, 6, 6, 6, 6, 6},
                {1, 2, 3, 4, 5, 6, 7, 7, 7, 7},
                {1, 2, 3, 4, 5, 6, 7, 8, 8, 8},
                {1, 2, 3, 4, 5, 6, 7, 8, 9, 9},
                {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}
        });
        var determinant = LUP.calcDeterminant(matrix);

        assertEquals(1.0, determinant);
    }

    @Test
    void calcDeterminant_extra() {
        var matrix = Matrix.of(new double[][] {
                {0, 1},
                {-1, 0}
        });
        var determinant = LUP.calcDeterminant(matrix);
        assertEquals(1.0, determinant);
    }

}