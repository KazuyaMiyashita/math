package math.matrix;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PrimitiveDeterminantTest {

    @Test
    void generatePermutations() {
        var result = PrimitiveDeterminant.generatePermutations(5);

        var expected = List.of(
                new PrimitiveDeterminant.Permutation(1,  new LinkedList<>(List.of(0, 1, 2, 3, 4))),
                new PrimitiveDeterminant.Permutation(-1, new LinkedList<>(List.of(0, 1, 2, 4, 3))), // (3 4)
                new PrimitiveDeterminant.Permutation(-1, new LinkedList<>(List.of(0, 1, 3, 2, 4))), // (2 3)
                new PrimitiveDeterminant.Permutation(1,  new LinkedList<>(List.of(0, 1, 3, 4, 2))), // (2 3)(3 4)
                new PrimitiveDeterminant.Permutation(1,  new LinkedList<>(List.of(0, 1, 4, 2, 3))), // (2 4)(3 4)
                new PrimitiveDeterminant.Permutation(-1, new LinkedList<>(List.of(0, 1, 4, 3, 2))), // (2 4)
                new PrimitiveDeterminant.Permutation(-1, new LinkedList<>(List.of(0, 2, 1, 3, 4))),
                new PrimitiveDeterminant.Permutation(1,  new LinkedList<>(List.of(0, 2, 1, 4, 3))),
                new PrimitiveDeterminant.Permutation(1,  new LinkedList<>(List.of(0, 2, 3, 1, 4))),
                new PrimitiveDeterminant.Permutation(-1, new LinkedList<>(List.of(0, 2, 3, 4, 1))),
                new PrimitiveDeterminant.Permutation(-1, new LinkedList<>(List.of(0, 2, 4, 1, 3))),
                new PrimitiveDeterminant.Permutation(1,  new LinkedList<>(List.of(0, 2, 4, 3, 1))),
                new PrimitiveDeterminant.Permutation(1,  new LinkedList<>(List.of(0, 3, 1, 2, 4))),
                new PrimitiveDeterminant.Permutation(-1, new LinkedList<>(List.of(0, 3, 1, 4, 2))),
                new PrimitiveDeterminant.Permutation(-1, new LinkedList<>(List.of(0, 3, 2, 1, 4))),
                new PrimitiveDeterminant.Permutation(1,  new LinkedList<>(List.of(0, 3, 2, 4, 1))),
                new PrimitiveDeterminant.Permutation(1,  new LinkedList<>(List.of(0, 3, 4, 1, 2))),
                new PrimitiveDeterminant.Permutation(-1, new LinkedList<>(List.of(0, 3, 4, 2, 1))),
                new PrimitiveDeterminant.Permutation(-1, new LinkedList<>(List.of(0, 4, 1, 2, 3))),
                new PrimitiveDeterminant.Permutation(1,  new LinkedList<>(List.of(0, 4, 1, 3, 2))),
                new PrimitiveDeterminant.Permutation(1,  new LinkedList<>(List.of(0, 4, 2, 1, 3))),
                new PrimitiveDeterminant.Permutation(-1, new LinkedList<>(List.of(0, 4, 2, 3, 1))),
                new PrimitiveDeterminant.Permutation(-1, new LinkedList<>(List.of(0, 4, 3, 1, 2))),
                new PrimitiveDeterminant.Permutation(1,  new LinkedList<>(List.of(0, 4, 3, 2, 1))),
                new PrimitiveDeterminant.Permutation(-1, new LinkedList<>(List.of(1, 0, 2, 3, 4)))
                // ...
        );

        assertEquals(result.size(), 120);
        assertEquals(result.subList(0, 25), expected);
    }

    @Test
    void calcDeterminant() {
        var matrix = new Matrix(new double[][] {
                {1, 1, 1, 1, 1},
                {1, 2, 2, 2, 2},
                {1, 2, 3, 3, 3},
                {1, 2, 3, 4, 4},
                {1, 2, 3, 4, 5}
        });
        var determinant = PrimitiveDeterminant.calcDeterminant(matrix);

        assertEquals(1.0, determinant);
    }

    @Test
    void calcDeterminant2() {
        var matrix = new Matrix(new double[][] {
                {3, 1},
                {2, 5},
        });
        var determinant = PrimitiveDeterminant.calcDeterminant(matrix);

        assertEquals(13.0, determinant);
    }

    @Test
    void calcDeterminant3() {
        var matrix = new Matrix(new double[][] {
                {1, 0, 3},
                {2, 1, 5},
                {7, 6, 4},
        });
        var determinant = PrimitiveDeterminant.calcDeterminant(matrix);

        assertEquals(-11.0, determinant);
    }

    @Test
    void calcDeterminant4() {
        var matrix = new Matrix(new double[][] {
                {2, 3, 0, 1},
                {0, 7, 1 ,0},
                {0, 0, 3, 1},
                {0, 1, 5, 2},
        });
        var determinant = PrimitiveDeterminant.calcDeterminant(matrix);

        assertEquals(16.0, determinant);
    }

    @Test
    void calcDeterminant_large() {
        var matrix = new Matrix(new double[][] {
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
        var determinant = PrimitiveDeterminant.calcDeterminant(matrix);

        assertEquals(1.0, determinant);
    }

    @Test
    void calcDeterminant_extra() {
        var matrix = new Matrix(new double[][] {
                {0, 1},
                {-1, 0}
        });
        var determinant = PrimitiveDeterminant.calcDeterminant(matrix);
        assertEquals(1.0, determinant);
    }

}