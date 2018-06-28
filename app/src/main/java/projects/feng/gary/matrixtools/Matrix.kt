package projects.feng.gary.matrixtools

import java.util.Arrays.copyOf
import kotlin.math.min

class Matrix(val matrixArr: Array<Fraction>, val numRows: Int, val numCols: Int) {
    companion object {
        fun zeroMatrix(rows: Int, cols: Int): Matrix = Matrix(Array(rows * cols, { Fraction.zero }), rows, cols)
        fun identityMatrix(rows: Int): Matrix =
                Matrix(Array(rows * rows, { pos ->
                    if (pos / rows == pos.rem(rows)) Fraction.one else Fraction.zero
                }), rows, rows)
    }

    val RREF by lazy { this.clone() }


    //---------------------------OPERATOR OVERLOADS-----------------------------------------------//

    operator fun get(i: Int): Fraction {
        return matrixArr[i]
    }

    operator fun get(i: Int, j: Int): Fraction {
        return matrixArr[getIndex(i, j)]
    }

    operator fun set(i: Int, j: Int, value: Fraction) {
        matrixArr[getIndex(i, j)] = value;
    }


    //---------------------------MATRIX FUNCTIONS-------------------------------------------------//

    fun solveRref(): Matrix {
        lockstepRref(zeroMatrix(numRows, numCols))
        return RREF
    }

    fun lockstepRref(other: Matrix): Matrix {
        var lastLeadingPosition: Position = Position(-1, -1)

        for (curRow in 0 until min(numRows, numCols)) {
            val leadingPosition = RREF.nextLeadingPosition(lastLeadingPosition)

            if (leadingPosition == null) {
                break
            }

            RREF.swapRows(curRow, leadingPosition.row)
            other.swapRows(curRow, leadingPosition.row)

            RREF.multiplyRow(RREF[curRow, leadingPosition.col].reciprocal(), curRow)
            other.multiplyRow(RREF[curRow, leadingPosition.col].reciprocal(), curRow)

            for (row in 0 until numRows) {
                if (row != curRow) {
                    val factor = -RREF[row, leadingPosition.col]
                    RREF.addMultipleOfRow(row, factor, curRow)
                    other.addMultipleOfRow(row, factor, curRow)
                }
            }

            lastLeadingPosition = leadingPosition
        }

        return other
    }

    fun clone(): Matrix = Matrix(matrixArr.copyOf(), numRows, numCols)


    //---------------------------ROW OPERATIONS---------------------------------------------------//

    fun swapRows(rowX: Int, rowY: Int) {
        for (i in 0 until numCols) {
            val temp = this[rowX, i]
            this[rowX, i] = this[rowY, i]
            this[rowY, i] = temp
        }
    }

    fun addMultipleOfRow(rowX: Int, factor: Fraction, rowY: Int) {
        for (i in 0 until numCols) {
            this[rowX, i] = this[rowX, i] + factor * this[rowY, i]
        }
    }

    fun multiplyRow(factor: Fraction, rowX: Int) {
        for (i in 0 until numCols) {
            this[rowX, i] = factor * this[rowX, i];
        }
    }


    //---------------------------HELPERS----------------------------------------------------------//

    private fun getIndex(i: Int, j: Int): Int {
        return i * numCols + j
    }

    private fun nextLeadingPosition(lastLeadingPosition: Position): Position? {
        for (i in lastLeadingPosition.col + 1 until numCols) {
            for (j in lastLeadingPosition.row + 1 until numRows) {
                if (this[j, i] != Fraction.zero) {
                    return Position(j, i)
                }
            }
        }

        return null
    }


    //---------------------------HELPER CLASS-----------------------------------------------------//

    class Position(val row: Int, val col: Int);


    //---------------------------OVERRIDES--------------------------------------------------------//

    override fun equals(other: Any?): Boolean {
        return other != null &&
                other is Matrix &&
                this.numRows == other.numRows &&
                this.numCols == other.numCols &&
                this.matrixArr.equals(other.matrixArr)
    }
}