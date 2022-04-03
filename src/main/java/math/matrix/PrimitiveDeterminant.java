package math.matrix;

import math.numbers.Field;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class PrimitiveDeterminant {

    /** 符号と置換の組
     * @param sgn 符号 1または-1
     * @param list 置換 リストが [0, 1, 2] の場合恒等置換、 [0, 2, 1] の場合1要素目と2要素目を入れ替えることを意味する
     */
    record Permutation<T>(T sgn, LinkedList<Integer> list) {
        int map(int i) {
            return list.indexOf(i);
        }
    }

    private static <T> List<Permutation<T>> recGeneratePermutations(Permutation<T> perm, Field<T> field) {
        if (perm.list.size() <= 1) {
            return List.of(perm);
        } else {
            var acc = new LinkedList<Permutation<T>>();
            var sgn = perm.sgn;
            for (var i = 0; i < perm.list.size(); i++) {
                var cloneList = new LinkedList<>(perm.list);
                var target = cloneList.remove(i);

                var perms = recGeneratePermutations(new Permutation<>(sgn, cloneList), field);
                perms.forEach(p -> p.list.addFirst(target));
                perms.forEach(acc::addLast);
                sgn = field.addInverse(sgn);
            }
            return acc;
        }
    }

    // n文字の置換全体を返す。符号も含まれる。
    static <T> List<Permutation<T>> generatePermutations(int n, Field<T> field) {
        var sgn = field.mulZero();
        var list = new LinkedList<>(IntStream.range(0, n).boxed().toList());
        return recGeneratePermutations(new Permutation<>(sgn, list), field);
    }

    public static <T> T calcDeterminant(Matrix<T> matrix) {
        var field = matrix.field;
        var permutations = generatePermutations(matrix.n, field);

        return permutations.stream().map(perm -> {
            var product = field.mulZero();
            for (var i = 0; i < matrix.n; i++) {
                product = field.multiply(product, matrix.get(i, perm.map(i)));
            }
            return field.multiply(perm.sgn, product);
        }).reduce(field.addZero(), field::add);
    }

}
