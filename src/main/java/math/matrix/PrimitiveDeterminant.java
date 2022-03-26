package math.matrix;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class PrimitiveDeterminant {

    /** 符号と置換の組
     * @param sgn 符号 1または-1
     * @param list 置換 リストが [0, 1, 2] の場合恒等置換、 [0, 2, 1] の場合1要素目と2要素目を入れ替えることを意味する
     */
    record Permutation(int sgn, LinkedList<Integer> list) {
        int map(int i) {
            return list.indexOf(i);
        }
    }

    private static List<Permutation> recGeneratePermutations(Permutation perm) {
        if (perm.list.size() <= 1) {
            return List.of(perm);
        } else {
            var acc = new LinkedList<Permutation>();
            var sgn = perm.sgn;
            for (var i = 0; i < perm.list.size(); i++) {
                var cloneList = new LinkedList<>(perm.list);
                var target = cloneList.remove(i);

                var perms = recGeneratePermutations(new Permutation(sgn, cloneList));
                perms.forEach(p -> p.list.addFirst(target));
                perms.forEach(acc::addLast);
                sgn *= -1;
            }
            return acc;
        }
    }

    // n文字の置換全体を返す。符号も含まれる。
    static List<Permutation> generatePermutations(int n) {
        var sgn = 1;
        var list = new LinkedList<>(IntStream.range(0, n).boxed().toList());
        return recGeneratePermutations(new Permutation(sgn, list));
    }

    public static double calcDeterminant(Matrix matrix) {
        var permutations = generatePermutations(matrix.n);

        return permutations.stream().map(perm -> {
            var product = IntStream.range(0, matrix.n)
                    .mapToDouble(i -> matrix.get(i, perm.map(i)))
                    .reduce(1, (a, b) -> a * b);
            return perm.sgn * product;
        }).reduce(0.0, Double::sum);
    }

}
