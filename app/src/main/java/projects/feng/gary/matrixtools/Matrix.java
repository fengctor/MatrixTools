package projects.feng.gary.matrixtools;

import android.graphics.Point;

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
        Point lastLeadingPoint = new Point(-1, -1);
        for (int curRow = 0; curRow < Math.max(numRows, numCols); ++curRow) {
            Point leadingPoint = nextLeadingPoint(result, lastLeadingPoint);

            if (leadingPoint == null) {
                break;
            }

            swapRows(result, curRow, leadingPoint.y);

            int leadingCol = leadingPoint.x;

            multiplyRow(result, result[getIndex(curRow, leadingCol)].reciprocal(), curRow);

            for (int reducedRow = 0; reducedRow < numRows; ++reducedRow) {
                if (reducedRow != curRow) {
                    Fraction factor = result[getIndex(reducedRow, leadingCol)].negate();
                    addMultipleOfRow(result, reducedRow, factor, curRow);
                }
            }

            lastLeadingPoint = leadingPoint;
        }

        return result;
    }

    public int getIndex(int row, int col) {
        return row * numCols + col;
    }

    public Fraction get(int row, int col) {
        return matrix[getIndex(row, col)];
    }


    // ROW OPERATIONS

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

    // The "next leading point" is the coordinates of the cell that will become
    //   the next leading '1'

    private Point nextLeadingPoint(Fraction[] scrapMatrix, Point lastLeadingPoint) {
        for (int i = lastLeadingPoint.x + 1; i < numCols; ++i) {
            for (int j = lastLeadingPoint.y + 1; j < numRows; ++j) {
                if (!scrapMatrix[getIndex(j, i)].equals(Fraction.zero)) {
                    return new Point(i, j);
                }
            }
        }

        return null;
    }
}
