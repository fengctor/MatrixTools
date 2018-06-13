package projects.feng.gary.matrixtools;

import java.util.Arrays;

public class Matrix {
    private int[] matrix;
    private int numRows;
    private int numCols;

    public Matrix(int[] matrix, int numRows, int numCols) {
        this.matrix = matrix;
        this.numRows = numRows;
        this.numCols = numCols;
    }

    public int[] RREF() {
        int[] result = Arrays.copyOf(matrix, numRows * numCols);


        return result;
    }
}
