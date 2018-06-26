package projects.feng.gary.matrixtools

import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Display
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.GridLayout
import kotlinx.android.synthetic.main.activity_rref.*

class RREFActivity : AppCompatActivity() {
    val matrixGrid by lazy {
        matrixLayout
    }
    val numRows by lazy {
        intent.getIntExtra(EXTRA_NUM_ROWS, 0)
    }
    val numCols by lazy {
        intent.getIntExtra(EXTRA_NUM_COLS, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rref)

        setUpMatrix(numRows, numCols)

        getRrefButton.setOnClickListener(View.OnClickListener { showRref() })
    }

    private fun setUpMatrix(numRows: Int, numCols: Int) {
        if (numRows <= 0 || numCols <= 0) {
            return
        }

        val display = windowManager.defaultDisplay
        var size = Point()
        display.getSize(size)

        val halfWidth = size.x
        val colWidth = halfWidth / (numCols + 2)

        matrixGrid.rowCount = numRows
        matrixGrid.columnCount = numCols

        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                val cell = EditText(this)
                val param = GridLayout.LayoutParams()
                param.width = colWidth
                cell.setLayoutParams(param)
                cell.gravity = Gravity.CENTER
                cell.imeOptions = cell.imeOptions or EditorInfo.IME_FLAG_NO_EXTRACT_UI

                matrixGrid.addView(cell)
            }
        }
    }

    private fun showRref() {
        val matrixArr = Array(numRows * numCols, { _ -> Fraction.zero })

        for (i in 0 until numRows * numCols) {
            val cell = matrixGrid.getChildAt(i) as EditText
            matrixArr[i] = Fraction(cell.text.toString())
        }

        val rref = Matrix(matrixArr, numRows, numCols).getRref()

        for (i in 0 until numRows * numCols) {
            val cell = matrixGrid.getChildAt(i) as EditText
            cell.setText(rref[i].toString())
        }
    }
}
