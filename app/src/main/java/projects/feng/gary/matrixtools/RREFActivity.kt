package projects.feng.gary.matrixtools

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
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

        matrixGrid.rowCount = numRows
        matrixGrid.columnCount = numCols

        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                val cell = EditText(this)
                val param = GridLayout.LayoutParams()
                param.width = 100
                cell.setLayoutParams(param)
                cell.gravity = Gravity.CENTER

                matrixGrid.addView(cell)
            }
        }
    }

    private fun showRref() {
        val matrixArr = Array<Fraction>(numRows * numCols, { _ -> Fraction.zero })

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
