package math.matrix;

import java.util.Optional;

/**
 * @param l 下三角行列。対角成分は全て1とする
 * @param u 上三角行列。
 * @param p 行の入れ替えを表す行列
 * @param pSign pの行列式
 * */
public record LUP(Matrix l, Matrix u, Matrix p, double pSign) {

    public static double calcDeterminant(Matrix matrix) {
        return lupDecomposition(matrix).map(LUP::determinant).orElse(0.0);
    }

    public static Optional<LUP> lupDecomposition(Matrix matrix) {
        if (matrix.m != matrix.n) {
            throw new IllegalArgumentException("正方行列でない場合LUP分解はできません");
        }
        var permutate = new int[matrix.n];
        for (var i = 0; i < matrix.n; i++) {
            permutate[i] = i;
        }

        try {
            double[][] elems = matrix.clone().elems;
            double pSign = 1.0;
            for (var k = 0; k < matrix.n; k++) {
                var p = 0.0;
                var _k = 0;
                for (var i = k; i < matrix.n; i++) {
                    var abs = Math.abs(elems[i][k]);
                    if (abs > p) {
                        p = abs;
                        _k = i;
                    }
                }
                if (p == 0.0) {
                    throw new SingularMatrixException();
                }
                swap(permutate, k, _k);
                swap(elems, k, _k);
                if (k != _k) {
                    pSign *= -1;
                }
                for (var i = k + 1; i < matrix.n; i++) {
                    elems[i][k] = elems[i][k] / elems[k][k];
                    for (var j = k + 1; j < matrix.n; j++) {
                        elems[i][j] = elems[i][j] - elems[i][k] * elems[k][j];
                    }
                }
            }

            var l = new double[matrix.n][matrix.n];
            var u = new double[matrix.n][matrix.n];
            var p = new double[matrix.n][matrix.n];
            for (var i = 0; i < matrix.n; i++) {
                for (var j = 0; j < matrix.n; j++) {
                    if (i > j) {
                        l[i][j] = elems[i][j];
                    } else if (i == j) {
                        l[i][j] = 1.0;
                        u[i][j] = elems[i][j];
                    } else {
                        u[i][j] = elems[i][j];
                    }
                }
                p[i][permutate[i]] = 1.0;
            }
            return Optional.of(new LUP(new Matrix(l), new Matrix(u), new Matrix(p), pSign));
        } catch (SingularMatrixException e) {
            return Optional.empty();
        }
    }

    private static class SingularMatrixException extends RuntimeException {}

    private static void swap(int[] array, int i, int j) {
        var t = array[i];
        array[i] = array[j];
        array[j] = t;
    }

    private static void swap(double[][] array, int i, int j) {
        var t = array[i];
        array[i] = array[j];
        array[j] = t;
    }

    public double determinant() {
        var uDet = 1.0;
        for (var i = 0; i < u.n; i++) {
            uDet *= u.get(i, i);
        }
        return pSign * uDet;
    }

}
