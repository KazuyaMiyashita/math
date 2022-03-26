package math.matrix;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Matrix {

    private final int m;
    private final int n;
    private final double[][] elems;

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

    @Override
    public String toString() {
        var a = Stream.of(elems).map(Arrays::toString).collect(Collectors.joining(", "));
        return "Matrix{" +
                "m=" + m +
                ", n=" + n +
                ", elems=" + a +
                '}';
    }
}
