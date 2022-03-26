package math.matrix;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

final class Matrix implements Cloneable {

    final int m; // 行数
    final int n; // 列数
    final double[][] elems;

    /** 行ベクトル(配列)の配列から行列を作成する */
    Matrix(double[][] elems) {
        this.m = elems.length;
        this.n = elems[0].length;
        this.elems = elems;
        for (int i = 0; i < m; i++) {
            if (elems[i].length != n) {
                throw new IllegalArgumentException("%d行目の要素数%dが先頭の要素数%dと異なります".formatted(i, elems[i].length, n));
            }
        }
    }

    /** n*n次の単位行列 */
    public static Matrix identity(int n) {
        var elems = new double[n][n];
        for (var i = 0; i < n; i++) {
            elems[i][i] = 1;
        }
        return new Matrix(elems);
    }

    private Matrix(LinkedList<LinkedList<Double>> elems) {
        this.m = elems.size();
        this.n = elems.getFirst().size();
        this.elems = new double[m][n];
        var i = 0;
        for (var row: elems) {
            if (row.size() != n) {
                throw new IllegalArgumentException("%d行目の要素数%dが先頭の要素数%dと異なります".formatted(i, row.size(), n));
            }
            var j = 0;
            for (var e: row) {
                this.elems[i][j] = e;
                j++;
            }
            i++;
        }
    }

    private LinkedList<LinkedList<Double>> toLinkedList() {
        var list = new LinkedList<LinkedList<Double>>();
        for (var row: elems) {
            var rowList = new LinkedList<Double>();
            for (var e: row) {
                rowList.addLast(e);
            }
            list.addLast(rowList);
        }
        return list;
    }

    /** 添字を0始まりとしてi行j列の要素を取得する */
    public double get(int i, int j) {
        return elems[i][j];
    }

    void update(int i, int j, double value) {
        elems[i][j] = value;
    }
    void update(int i, int j, Matrix mat) {
        for (var _i = 0; _i < mat.m; _i++) {
            for (var _j = 0; _j < mat.n; _j++) {
                elems[i + _i][j + _j] = mat.get(_i, _j);
            }
        }
    }

    public Matrix add(Matrix that) {
        if (m != that.m || n != that.n) {
            throw new IllegalArgumentException("行列のサイズが一致しません。this: %d*%d, that: %d*%d".formatted(m, n, that.m, that.n));
        }
        var newElems = new double[m][n];
        for (var i = 0; i < m; i++) {
            for (var j = 0; j < n; j++) {
                newElems[i][j] = get(i, j) + that.get(i, j);
            }
        }
        return new Matrix(newElems);
    }

    /** 自身の右に引数の行列を掛ける。自身の列の個数と引数の列の行数が一致する必要がある */
    public Matrix multiply(Matrix that) {
        if (n != that.m) {
            throw new IllegalArgumentException("thisの列の個数とthatの行の個数が一致しません。this: %d*%d, that: %d*%d".formatted(m, n, that.m, that.n));
        }
        var newElems = new double[m][that.n];
        for (var i = 0; i < m; i++) {
            for (var j = 0; j < that.n; j++) {
                var acc = 0.0;
                for (var k = 0; k < n; k++) {
                    acc += get(i, k) * that.get(k, j);
                }
                newElems[i][j] = acc;
            }
        }
        return new Matrix(newElems);
    }

    public Matrix multiply(double a) {
        var newElems = new double[m][n];
        for (var i = 0; i < m; i++) {
            for (var j = 0; j < n; j++) {
                newElems[i][j] = get(i, j) * a;
            }
        }
        return new Matrix(newElems);
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
        Matrix matrix = (Matrix) o;
        return Arrays.deepEquals(elems, matrix.elems);
    }


    @Override
    protected Matrix clone() {
        // Matrixはfinalであるためsuper.clone()の呼び出しは不要
        var newElem = new double[m][n];
        for (var i = 0; i < m; i++) {
            for (var j = 0; j < n; j++) {
                newElem[i][j] = get(i, j);
            }
        }
        return new Matrix(newElem);
    }
}
