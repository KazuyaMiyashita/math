package math.matrix;

import java.util.Optional;

/**
 * @param l 下三角行列。対角成分は全て1とする
 * @param u 上三角行列。
 * @param p 行の入れ替えを表す行列
 * @param pSign pの行列式
 * */
public record LUP<T>(Matrix<T> l, Matrix<T> u, Matrix<T> p, T pSign) {

    public static <T> T calcDeterminant(Matrix<T> matrix) {
        return lupDecomposition(matrix).map(LUP::determinant).orElse(matrix.field.addZero());
    }

    public static <T> Optional<LUP<T>> lupDecomposition(Matrix<T> matrix) {
        var field = matrix.field;
        var distance = matrix.distance;
        var comparator = matrix.comparator;

        if (matrix.m != matrix.n) {
            throw new IllegalArgumentException("正方行列でない場合LUP分解はできません");
        }
        var permutate = new Integer[matrix.n];
        for (var i = 0; i < matrix.n; i++) {
            permutate[i] = i;
        }

        try {
            T[][] elems = matrix.clone().elems;
            T pSign = field.mulZero();
            for (var k = 0; k < matrix.n; k++) {
                var p = field.addZero();
                var _k = 0;
                for (var i = k; i < matrix.n; i++) {
                    var abs = distance.calc(field.addZero(), elems[i][k]);
                    if (comparator.compare(abs, p) > 0) {
                        p = abs;
                        _k = i;
                    }
                }
                if (p == field.addZero()) {
                    throw new SingularMatrixException();
                }
                swap(permutate, k, _k);
                swap(elems, k, _k);
                if (k != _k) {
                    pSign = field.addInverse(pSign);
                }
                for (var i = k + 1; i < matrix.n; i++) {
                    elems[i][k] = field.div(elems[i][k], elems[k][k]);
                    for (var j = k + 1; j < matrix.n; j++) {
                        elems[i][j] = field.minus(elems[i][j], field.multiply(elems[i][k], elems[k][j]));
                    }
                }
            }

            var l = (T[][]) new Object[matrix.n][matrix.n];
            var u = (T[][]) new Object[matrix.n][matrix.n];
            var p = (T[][]) new Object[matrix.n][matrix.n];
            for (var i = 0; i < matrix.n; i++) {
                for (var j = 0; j < matrix.n; j++) {
                    if (i > j) {
                        l[i][j] = elems[i][j];
                        u[i][j] = field.addZero();
                    } else if (i == j) {
                        l[i][j] = field.mulZero();
                        u[i][j] = elems[i][j];
                    } else {
                        l[i][j] = field.addZero();
                        u[i][j] = elems[i][j];
                    }
                    p[i][j] = field.addZero();
                }
                p[i][permutate[i]] = field.mulZero();
            }
            return Optional.of(new LUP<>(
                    new Matrix<>(l, field, distance, comparator),
                    new Matrix<>(u, field, distance, comparator),
                    new Matrix<>(p, field, distance, comparator),
                    pSign));
        } catch (SingularMatrixException e) {
            return Optional.empty();
        }
    }

    private static class SingularMatrixException extends RuntimeException {}

    private static <T> void swap(T[] array, int i, int j) {
        var t = array[i];
        array[i] = array[j];
        array[j] = t;
    }

    public T determinant() {
        var field = u.field;
        var uDet = field.mulZero();
        for (var i = 0; i < u.n; i++) {
            uDet = field.multiply(uDet, u.get(i, i));
        }
        return field.multiply(pSign, uDet);
    }

}
