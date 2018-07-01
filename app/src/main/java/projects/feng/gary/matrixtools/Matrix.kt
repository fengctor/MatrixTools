package projects.feng.gary.matrixtools

import kotlin.math.min

class Matrix(val matrixArr: Array<Fraction>, val numRows: Int, val numCols: Int) {
    companion object {
        fun zeroMatrix(rows: Int, cols: Int): Matrix = Matrix(Array(rows * cols, { Fraction.zero }), rows, cols)
        fun identityMatrix(rows: Int): Matrix =
                Matrix(Array(rows * rows, { pos ->
                    if (pos / rows == pos.rem(rows)) Fraction.one else Fraction.zero
                }), rows, rows)
    }

    lateinit var rref: Matrix
    private var rrefFound = false


    //---------------------------OPERATOR OVERLOADS-----------------------------------------------//

    operator fun get(i: Int): Fraction = matrixArr[i]

    operator fun get(i: Int, j: Int): Fraction = matrixArr[getIndex(i, j)]


    operator fun set(i: Int, value: Fraction) {
        matrixArr[i] = value
    }

    operator fun set(i: Int, j: Int, value: Fraction) {
        matrixArr[getIndex(i, j)] = value;
    }

    override operator fun equals(other: Any?): Boolean {
        return other != null &&
                other is Matrix &&
                this.numRows == other.numRows &&
                this.numCols == other.numCols &&
                this.matrixArr contentEquals other.matrixArr
    }


    //---------------------------MATRIX FUNCTIONS-------------------------------------------------//

    fun solveRref(): Matrix {
        if (!rrefFound) {
            lockstepRref(zeroMatrix(numRows, numCols))
        }
        return rref
    }

    private fun lockstepRref(matrix: Matrix): Matrix {
        rref = this.clone()
        val other = matrix.clone()

        var lastLeadingPosition = Position(-1, -1)

        for (curRow in 0 until min(numRows, numCols)) {
            val leadingPosition = rref.nextLeadingPosition(lastLeadingPosition)

            if (leadingPosition == null) {
                break
            }

            rref.swapRows(curRow, leadingPosition.row)
            other.swapRows(curRow, leadingPosition.row)

            val multFactor = rref[curRow, leadingPosition.col].reciprocal()
            rref.multiplyRow(multFactor, curRow)
            other.multiplyRow(multFactor, curRow)

            for (row in 0 until numRows) {
                if (row != curRow) {
                    val rowFactor = -rref[row, leadingPosition.col]
                    rref.addMultipleOfRow(row, rowFactor, curRow)
                    other.addMultipleOfRow(row, rowFactor, curRow)
                }
            }

            lastLeadingPosition = leadingPosition
        }

        rrefFound = true
        return other
    }

    fun solveInverse(): Matrix? {
        val result = lockstepRref(identityMatrix(numRows))
        return if (rref == identityMatrix(numRows)) result else null
    }

    fun getRank(): Int {
        if (!rrefFound) {
            solveRref()
        }

        var index = 0
        var rank = 0
        while (index < numRows * numCols) {
            if (rref[index] == Fraction.one) {
                ++rank
                index += numCols + 1
            } else {
                ++index
            }
        }

        return rank
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

}