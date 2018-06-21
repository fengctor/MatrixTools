package projects.feng.gary.matrixtools;

import android.graphics.Point;

import java.util.Arrays;

public class Matrix {
    private Fraction[] matrixArr;
    private int numRows;
    private int numCols;

    public Matrix(Fraction[] matrixArr, int numRows, int numCols) {
        this.matrixArr = matrixArr;
        this.numRows = numRows;
        this.numCols = numCols;
    }

    public Matrix RREF() {
        Matrix result = new Matrix(Arrays.copyOf(matrixArr, numRows * numCols), numRows, numCols);
        Point lastLeadingPoint = new Point(-1, -1);
        for (int curRow = 0; curRow < Math.max(numRows, numCols); ++curRow) {
            Point leadingPoint = nextLeadingPoint(result, lastLeadingPoint);

            if (leadingPoint == null) {
                break;
            }

            result.swapRows(curRow, leadingPoint.y);

            int leadingCol = leadingPoint.x;

            result.multiplyRow(result.get(curRow, leadingCol).reciprocal(), curRow);

            for (int otherRow = 0; otherRow < numRows; ++otherRow) {
                if (otherRow != curRow) {
                    Fraction factor = result.get(otherRow, leadingCol).negate();
                    result.addMultipleOfRow(otherRow, factor, curRow);
                }
            }

            lastLeadingPoint = leadingPoint;
        }

        return result;
    }

    public Fraction get(int row, int col) {
        return matrixArr[getIndex(row, col)];
    }

    public void set(int row, int col, Fraction val) {
        matrixArr[getIndex(row, col)] = val;
    }


    //---------------------------ROW OPERATIONS---------------------------------------------------//

    public void swapRows(int rowX, int rowY) {
        for (int i = 0; i < numCols; ++i) {
            Fraction temp = this.get(rowX, i);
            this.set(rowX, i, this.get(rowY, i));
            this.set(rowY, i, temp);
        }
    }

    public void addMultipleOfRow(int rowX, Fraction factor, int rowY) {
        for (int i = 0; i < numCols; ++i) {
            this.set(
                    rowX, i, this.get(rowX, i)
                    .addedBy(this.get(rowY, i).multipliedBy(factor)));
        }
    }

    public void multiplyRow(Fraction factor, int rowX) {
        for (int i = 0; i < numCols; ++i) {
            this.set(rowX, i, this.get(rowX, i).multipliedBy(factor));
        }
    }


    //---------------------------HELPERS----------------------------------------------------------//


    private int getIndex(int row, int col) {
        return row * numCols + col;
    }

    // The "next leading point" is the coordinates of the cell that will become
    //   the next leading '1'

    private Point nextLeadingPoint(Matrix matrix, Point lastLeadingPoint) {
        for (int i = lastLeadingPoint.x + 1; i < numCols; ++i) {
            for (int j = lastLeadingPoint.y + 1; j < numRows; ++j) {
                if (!matrix.get(j, i).equals(Fraction.zero)) {
                    return new Point(i, j);
                }
            }
        }

        return null;
    }
}
