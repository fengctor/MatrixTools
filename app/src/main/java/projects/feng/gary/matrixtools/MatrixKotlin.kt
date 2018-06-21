package projects.feng.gary.matrixtools

class MatrixKotlin(val matrixArr: Array<FractionKotlin>, val numRows: Int, val numCols: Int) {
    operator fun get(i: Int, j: Int): FractionKotlin? {
        return if (i > numRows || j > numCols)
            null
        else
            matrixArr[getIndex(i, j)]
    }

    private fun getIndex(i: Int, j: Int): Int {
        return i * numCols + j
    }
}