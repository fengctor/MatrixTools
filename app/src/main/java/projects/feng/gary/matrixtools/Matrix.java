package projects.feng.gary.matrixtools;

import java.util.Arrays;

public class Matrix {
    private Fraction[] matrix;
    private int numRows;
    private int numCols;

    public Matrix(Fraction[] matrix, int numRows, int numCols) {
        this.matrix = matrix;
        this.numRows = numRows;
        this.numCols = numCols;
    }

    public Fraction[] RREF() {
        Fraction[] result = Arrays.copyOf(matrix, numRows * numCols);
        for (int i = 0; i < Math.max(numRows, numCols); ++i) {
            int nextRow = leftMostRow(result);

            if (nextRow == -1) {
                break;
            }

            swapRows(result, i, nextRow);

            multiplyRow(result, firstNonZero(result, i).reciprocal(), i);
        }

        return result;
    }

    private int getIndex(int row, int col) {
        return row * numCols + col;
    }

    // Row Operations
    public void swapRows(Fraction[] scrapMatrix, int rowX, int rowY) {
        for (int i = 0; i < numCols; ++i) {
            Fraction temp = scrapMatrix[getIndex(rowX, i)];
            scrapMatrix[getIndex(rowX, i)] = scrapMatrix[getIndex(rowY, i)];
            scrapMatrix[getIndex(rowY, i)] = temp;
        }
    }

    public void addMultipleOfRow(Fraction[] scrapMatrix, int rowX, Fraction factor, int rowY) {
        for (int i = 0; i < numCols; ++i) {
            scrapMatrix[getIndex(rowX, i)] = scrapMatrix[getIndex(rowX, i)]
                    .addedBy(scrapMatrix[getIndex(rowY, i)].multipliedBy(factor));
        }
    }

    public void multiplyRow(Fraction[] scrapMatrix, Fraction factor, int rowX) {
        for (int i = 0; i < numCols; ++i) {
            scrapMatrix[getIndex(rowX, i)] = scrapMatrix[getIndex(rowX, i)].multipliedBy(factor);
        }
    }

    private int leftMostRow(Fraction[] scrapMatrix) {
        for (int i = 0; i < numCols; ++i) {
            for (int j = 0; j < numRows; ++i) {
                if (!scrapMatrix[getIndex(j, i)].equals(Fraction.zero)) {
                    return j;
                }
            }
        }

        return -1;
    }

    private Fraction firstNonZero(Fraction[] scrapMatrix, int row) {
        for (int i = 0; i < numCols; ++i) {
            Fraction val = scrapMatrix[getIndex(row, i)];
            if (!val.equals(Fraction.zero)) {
                return val;
            }
        }

        return null;
    }
}
