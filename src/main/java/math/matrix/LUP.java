package math.matrix;

import java.util.Optional;

/**
 * @param l 下三角行列。対角成分は全て1とする
 * @param u 上三角行列。対角成分は全て0ではない(正則)
 * @param p 行の入れ替えを表す行列
 * @param pSign pの行列式
 * */
public record LUP<T>(Matrix<T> l, Matrix<T> u, Matrix<T> p, T pSign) {

    public static <T> T calcDeterminant(Matrix<T> matrix) {
        return lupDecomposition2(matrix).map(LUP::determinant).orElse(matrix.field.addZero());
    }

    public static <T> Optional<LUP<T>> lupDecomposition2(Matrix<T> matrix) {
        var field = matrix.field;
        var distance = matrix.distance;
        var comparator = matrix.comparator;
        if (matrix.m != matrix.n) {
            throw new IllegalArgumentException("正方行列でない場合LUP分解はできません");
        }
        try {
            if (matrix.n == 1) {
                if (matrix.elems[0][0] != field.addZero()) {
                    var l = (T[][]) new Object[1][1];
                    l[0][0] = field.mulZero();
                    var p = (T[][]) new Object[1][1];
                    p[0][0] = field.mulZero();
                    var u = (T[][]) new Object[1][1];
                    u[0][0] = matrix.elems[0][0];

                    return Optional.of(new LUP(
                            new Matrix(l, field, distance, comparator),
                            new Matrix(u, field, distance, comparator),
                            new Matrix(p, field, distance, comparator),
                            field.mulZero()));
                } else {
                    throw new SingularMatrixException();
                }
            } else {
                // 2次以上をここでかく

                // matrixの1列目で要素が0でないものを探す。複数ある時は絶対値が大きいもので最初に見つけたものを選ぶ。
                var p = field.addZero(); // 最大値を探すために、i行1列目を見ながら更新していくやつ
                var _k = 0; // どの列と入れ替えるか？
                for (var i = 0; i < matrix.n; i++) {
                    var abs = distance.calc(field.addZero(), matrix.elems[i][0]);
                    if (comparator.compare(abs, p) > 0) {
                        p = abs;
                        _k = i;
                    }
                }
                if (p == field.addZero()) {
                    throw new SingularMatrixException();
                }

                // 置換行列 P'
                var p_dash = (T[][]) new Object[matrix.n][matrix.n];
                for (var i = 0; i < matrix.n; i++) {
                    for (var j = 0; j < matrix.n; j++) {
                        p_dash[i][j] = field.addZero(); // 初期値nullが入っているので0で埋める
                    }
                }
                for (var i = 0; i < matrix.n; i++) {
                    p_dash[i][i] = field.mulZero(); // 対角成分を1にする
                }
                swap(p_dash, 0, _k);



                // 行列A'を作る
                var a_dash = matrix.clone().elems; // 元の行列Aをクローンして
                swap(a_dash, 0, _k); // 行を入れ替える

                // 計算の効率のため、P'の行列式を別に持つ。(P'いらないのでは…？)
                // 行を入れ替えない場合は行列式1で、入れ替えたら行列式-1.
                T p_dash_sign = field.mulZero();
                if (0 != _k) {
                    p_dash_sign = field.addInverse(p_dash_sign);
                }

                // L'を作る
                var l_dash = (T[][]) new Object[matrix.n][matrix.n];
                for (var i = 0; i < matrix.n; i++) {
                    for (var j = 0; j < matrix.n; j++) {
                        l_dash[i][j] = field.addZero(); // 初期値nullが入っているので0で埋める
                    }
                }
                l_dash[0][0] = field.mulZero();
                for (var i = 1; i < matrix.n; i++) {
                    l_dash[i][0] = field.div(a_dash[i][0], a_dash[0][0]); // v / a'_11の部分
                    l_dash[i][i] = field.mulZero(); // 対角成分
                }

                // U'を作る
                var u_dash = (T[][]) new Object[matrix.n][matrix.n];
                for (var i = 0; i < matrix.n; i++) {
                    for (var j = 0; j < matrix.n; j++) {
                        u_dash[i][j] = field.addZero(); // 初期値nullが入っているので0で埋める
                    }
                }
                u_dash[0][0] = a_dash[0][0];
                for (var i = 1; i < matrix.n; i++) {
                    u_dash[0][i] = a_dash[0][i]; // w^tの部分
                }

                // vw^t
                var v_matrix_elems = (T[][]) new Object[matrix.n - 1][1];
                for (var i = 0; i < matrix.n - 1; i++) {
                    v_matrix_elems[i][0] = a_dash[i+1][0];
                }
                var v_matrix = new Matrix<T>(v_matrix_elems, field, distance, comparator);
                var w_matrix_elems = (T[][]) new Object[1][matrix.n - 1];
                for (var i = 0; i < matrix.n - 1; i++) {
                    w_matrix_elems[0][i] = a_dash[0][i+1];
                }
                var w_matrix = new Matrix<T>(w_matrix_elems, field, distance, comparator);

                var vw = v_matrix.multiply(w_matrix); // n-1 * n-1 の行列

                // A_k- (vw^t / a'_11) の部分
                for (var i = 1; i < matrix.n; i++) { // 1から始めているので注意
                    for (var j = 1; j < matrix.n; j++) {
                        u_dash[i][j] = field.minus(a_dash[i][j], field.div(vw.elems[i-1][j-1], a_dash[0][0]));
                    }
                }

                // ここでU'の右下の部分が正則。これを次に使いたいので、新たな行列とする
                var u_migishita = (T[][]) new Object[matrix.n - 1][matrix.n - 1];
                for (var i = 0; i < matrix.n - 1; i++) {
                    for (var j = 0; j < matrix.n - 1; j++) {
                        u_migishita[i][j] = u_dash[i+1][j+1];
                    }
                }
                var u_migishita_matrix = new Matrix<T>(u_migishita, field, distance, comparator);

                var maybe_n_minus_1_lup = lupDecomposition2(u_migishita_matrix);
                if (maybe_n_minus_1_lup.isEmpty()) {
                    throw new SingularMatrixException();
                }
                var n_minux_1_lup = maybe_n_minus_1_lup.get(); // Pk, Lk, Ukがある。n-1次正方行列
                System.out.println(n_minux_1_lup);


                var pk_hidariue_1 = (T[][]) new Object[matrix.n][matrix.n];
                for (var i = 0; i < matrix.n; i++) {
                    for (var j = 0; j < matrix.n; j++) {
                        pk_hidariue_1[i][j] = field.addZero();
                    }
                }
                pk_hidariue_1[0][0] = field.mulZero();
                for (var i = 1; i < matrix.n; i++) {
                    for (var j = 1; j < matrix.n; j++) {
                        pk_hidariue_1[i][j] = n_minux_1_lup.p().elems[i-1][j-1];
                    }
                }

                var l_elems = (T[][]) new Object[matrix.n][matrix.n];
                for (var i = 0; i < matrix.n; i++) {
                    for (var j = 0; j < matrix.n; j++) {
                        l_elems[i][j] = field.addZero();
                    }
                }
                l_elems[0][0] = field.mulZero();
                var pkv_div_a11 = n_minux_1_lup.p().multiply(v_matrix)  // n-1 * 1行列
                        .multiply(field.mulInverse(a_dash[0][0]));
                for (var i = 1; i < matrix.n; i++) {
                    l_elems[i][0] = pkv_div_a11.elems[i-1][0];
                }
                for (var i = 1; i < matrix.n; i++) {
                    for (var j = 1; j < matrix.n; j++) {
                        l_elems[i][j] = n_minux_1_lup.l().elems[i-1][j-1];
                    }
                }

                var u_elems = (T[][]) new Object[matrix.n][matrix.n];
                for (var i = 0; i < matrix.n; i++) {
                    for (var j = 0; j < matrix.n; j++) {
                        u_elems[i][j] = field.addZero();
                    }
                }
                u_elems[0][0] = a_dash[0][0];
                for (var i = 1; i < matrix.n; i++) {
                    u_elems[0][i] = a_dash[0][i];
                }
                for (var i = 1; i < matrix.n; i++) {
                    for (var j = 1; j < matrix.n; j++) {
                        u_elems[i][j] = n_minux_1_lup.u().elems[i-1][j-1];
                    }
                }

                var pk_hidariue_1_matrix = new Matrix<T>(pk_hidariue_1, field, distance, comparator);
                var p_dash_matrix = new Matrix<T>(p_dash, field, distance, comparator);

                var p_matrix = pk_hidariue_1_matrix.multiply(p_dash_matrix);
                var l_matrix = new Matrix<T>(l_elems, field, distance, comparator);
                var u_matrix = new Matrix<T>(u_elems, field, distance, comparator);
                var pSign = field.multiply(p_dash_sign, n_minux_1_lup.pSign());

                return Optional.of(new LUP<T>(p_matrix, l_matrix, u_matrix, pSign));
            }
        } catch (SingularMatrixException e) {
            return Optional.empty();
        }
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
                        elems[i][j] = field.minus(
                                elems[i][j],
                                field.multiply(elems[i][k], elems[k][j])
                        );
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
