package math.matrix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {

    @Test
    void createMatrix() {
        var elems = new double[][] {
                {1, 2, 3},
                {4, 5, 6}
        };
        var matrix = Matrix.of(elems);
        assertEquals(matrix.toString(), "Matrix{m=2, n=3, elems=[1.0, 2.0, 3.0], [4.0, 5.0, 6.0]}");
    }

    @Test
    void cannotCreateIllegalMatrix() {
        var elems = new double[][] {
                {1, 2, 3},
                {4, 5}
        };
        assertThrows(IllegalArgumentException.class, () -> Matrix.of(elems));
    }

    @Test
    void get() {
        var elems = new double[][] {
                {1, 2, 3},
                {4, 5, 6}
        };
        var matrix = Matrix.of(elems);

        assertEquals(matrix.get(0, 0), 1);
        assertEquals(matrix.get(0, 1), 2);
        assertEquals(matrix.get(1, 0), 4);
        assertEquals(matrix.get(1, 2), 6);
    }

    @Test
    void add() {
        var matrix1 = Matrix.of(new double[][] {
                {1, 2, 3, 4, 5},
                {10, 20, 30, 40, 50},
                {100, 200, 300, 400, 500},
        });
        var matrix2 = Matrix.of(new double[][] {
                {10, 20, 30, 40, 50},
                {100, 200, 300, 400, 500},
                {1000, 2000, 3000, 4000, 5000},
        });
        var result = matrix1.add(matrix2);
        var expected = Matrix.of(new double[][] {
                {11, 22, 33, 44, 55},
                {110, 220, 330, 440, 550},
                {1100, 2200, 3300, 4400, 5500},
        });
        assertEquals(result, expected);
    }

    @Test
    void multiply() {
        var matrix1 = Matrix.of(new double[][] {
                {2, 0, 3},
                {5, 1, 4},
        });
        var matrix2 = Matrix.of(new double[][] {
                {7, 6, 4},
                {1, 2, 2},
                {5, 3, 1},
        });
        var result = matrix1.multiply(matrix2);
        var expected = Matrix.of(new double[][] {
                {29, 21, 11},
                {56, 44, 26},
        });
        assertEquals(result, expected);
    }

}