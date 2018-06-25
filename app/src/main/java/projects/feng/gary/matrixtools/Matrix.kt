package projects.feng.gary.matrixtools

import kotlin.math.max

class Matrix(val matrixArr: Array<Fraction>, val numRows: Int, val numCols: Int) {

    operator fun get(i: Int, j: Int): Fraction {
        return matrixArr[getIndex(i, j)]
    }

    operator fun set(i: Int, j: Int, value: Fraction) {
        matrixArr[getIndex(i, j)] = value;
    }

    fun getRref(): Matrix {
        val result = Matrix(matrixArr.copyOf(), numRows, numCols)
        var lastLeadingPosition: Position = Position(-1, -1)

        for (curRow in 0 until max(numRows, numCols)) {
            val leadingPosition = result.nextLeadingPosition(lastLeadingPosition)

            if (leadingPosition == null) {
                break
            }

            result.swapRows(curRow, leadingPosition.row)

            result.multiplyRow(result[curRow, leadingPosition.col].reciprocal(), curRow)

            for (otherRow in 0 until numRows) {
                if (otherRow != curRow) {
                    val factor: Fraction = -result[otherRow, leadingPosition.col]
                    result.addMultipleOfRow(otherRow, factor, curRow)
                }
            }

            lastLeadingPosition = leadingPosition
        }

        return result
    }

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


    //---------------------------CLASS------------------------------------------------------------//

    class Position(val row: Int, val col: Int);
}