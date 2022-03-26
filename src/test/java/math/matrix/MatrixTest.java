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
        var matrix = new Matrix(elems);
        assertEquals(matrix.toString(), "Matrix{m=2, n=3, elems=[1.0, 2.0, 3.0], [4.0, 5.0, 6.0]}");
    }

    @Test
    void cannotCreateIllegalMatrix() {
        var elems = new double[][] {
                {1, 2, 3},
                {4, 5}
        };
        assertThrows(IllegalArgumentException.class, () -> new Matrix(elems));
    }

}