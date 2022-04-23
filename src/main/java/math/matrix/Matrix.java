package math.matrix;

import math.numbers.Distance;
import math.numbers.DoubleInstances;
import math.numbers.Field;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Matrix<T> implements Cloneable {

    final int m; // 行数
    final int n; // 列数
    final T[][] elems;
    final Field<T> field;
    final Distance<T> distance;
    final Comparator<T> comparator;

    /** 行ベクトル(配列)の配列から行列を作成する */
    Matrix(T[][] elems, Field<T> field, Distance<T> distance, Comparator<T> comparator) {
        this.m = elems.length;
        this.n = elems[0].length;
        this.elems = elems;
        this.field = field;
        this.distance = distance;
        this.comparator = comparator;
        for (int i = 0; i < m; i++) {
            if (elems[i].length != n) {
                throw new IllegalArgumentException("%d行目の要素数%dが先頭の要素数%dと異なります".formatted(i, elems[i].length, n));
            }
        }
    }

    public static Matrix<Double> of(double[][] elems) {
        var n = elems[0].length;
        for (int i = 0; i < elems.length; i++) {
            if (elems[i].length != n) {
                throw new IllegalArgumentException("%d行目の要素数%dが先頭の要素数%dと異なります".formatted(i, elems[i].length, n));
            }
        }

        Double[][] _elems = new Double[elems.length][elems[0].length];
        for (var i = 0; i < elems.length; i++) {
            for (var j = 0; j < elems[0].length; j++) {
                _elems[i][j] = elems[i][j];
            }
        }
        return new Matrix<>(_elems, DoubleInstances.field, DoubleInstances.distance, DoubleInstances.comparator);
    }

    /** 添字を0始まりとしてi行j列の要素を取得する */
    public T get(int i, int j) {
        return elems[i][j];
    }

    public Matrix<T> add(Matrix<T> that) {
        if (m != that.m || n != that.n) {
            throw new IllegalArgumentException("行列のサイズが一致しません。this: %d*%d, that: %d*%d".formatted(m, n, that.m, that.n));
        }
        var newElems = (T[][]) new Object[m][n];
        for (var i = 0; i < m; i++) {
            for (var j = 0; j < n; j++) {
                newElems[i][j] = field.add(get(i, j), that.get(i, j));
            }
        }
        return new Matrix<>(newElems, field, distance, comparator);
    }

    /** 自身の右に引数の行列を掛ける。自身の列の個数と引数の列の行数が一致する必要がある */
    public Matrix<T> multiply(Matrix<T> that) {
        if (n != that.m) {
            throw new IllegalArgumentException("thisの列の個数とthatの行の個数が一致しません。this: %d*%d, that: %d*%d".formatted(m, n, that.m, that.n));
        }
        var newElems = (T[][]) new Object[m][n];
        for (var i = 0; i < m; i++) {
            for (var j = 0; j < that.n; j++) {
                var acc = field.addZero();
                for (var k = 0; k < n; k++) {
                    acc = field.add(acc, field.multiply(get(i, k) ,that.get(k, j)));
                }
                newElems[i][j] = acc;
            }
        }
        return new Matrix<>(newElems, field, distance, comparator);
    }

    public Matrix<T> multiply(T a) {
        var newElems = (T[][]) new Object[m][n];
        for (var i = 0; i < m; i++) {
            for (var j = 0; j < n; j++) {
                newElems[i][j] = field.multiply(get(i, j), a);
            }
        }
        return new Matrix<>(newElems, field, distance, comparator);
    }

    @Override
    public String toString() {
        var a = Stream.of(elems).map(Arrays::toString).collect(Collectors.joining(", "));
        return "Matrix{" +
                "m=" + m +
                ", n=" + n +
                ", elems=" + a +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix<T> matrix = (Matrix<T>) o;
        return Arrays.deepEquals(elems, matrix.elems);
    }


    @Override
    protected Matrix<T> clone() {
        // Matrixはfinalであるためsuper.clone()の呼び出しは不要
        var newElems = (T[][]) new Object[m][n];
        for (var i = 0; i < m; i++) {
            for (var j = 0; j < n; j++) {
                newElems[i][j] = get(i, j);
            }
        }
        return new Matrix<>(newElems, field, distance, comparator);
    }
}
