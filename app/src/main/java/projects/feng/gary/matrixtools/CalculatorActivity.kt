package projects.feng.gary.matrixtools

import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputFilter
import android.text.InputType
import android.text.Spanned
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.GridLayout
import kotlinx.android.synthetic.main.activity_calculator.*

class CalculatorActivity : AppCompatActivity() {
    val matrixGrid by lazy {
        matrixLayout
    }
    val rrefGrid by lazy {
        rrefLayout
    }
    val numRows by lazy {
        intent.getIntExtra(EXTRA_NUM_ROWS, 0)
    }
    val numCols by lazy {
        intent.getIntExtra(EXTRA_NUM_COLS, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        setUpMatrix(numRows, numCols)
        initChoiceSpinner()

        getRrefButton.setOnClickListener({ showResult() })
    }

    private fun initChoiceSpinner() {
        val adapter = ArrayAdapter<String>(this, R.layout.spinner_holder)
        adapter.setDropDownViewResource(R.layout.spinner_row)
        type.adapter = adapter

        val choices = listOf("RREF Calculator", "Inverse")
        adapter.addAll(choices)
    }

    private fun createCell(width: Int, focusable: Boolean): EditText {
        val cell = EditText(this)
        val gridParam = GridLayout.LayoutParams()
        gridParam.width = width
        cell.setLayoutParams(gridParam)

        cell.textSize = 14f
        cell.maxLines = 1
        cell.gravity = Gravity.CENTER
        if (focusable) {
            cell.imeOptions = cell.imeOptions or EditorInfo.IME_FLAG_NO_EXTRACT_UI
            cell.inputType = InputType.TYPE_CLASS_PHONE
            val filter = InputFilter { source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int ->
                var r: CharSequence? = null
                for (i in start until end) {
                    val char = source.get(i)
                    if (!Character.isDigit(char) && char != '/' && char != '.' && char != '-') {
                        r = ""
                        break
                    }
                    val numChar = dest.fold (0, { acc, c -> if (c == char) acc + 1 else 0 })
                    if (char == '-' && (dstart != 0 || numChar > 0)) {
                        r = ""
                        break
                    }
                    if ((char == '/' || char == '.') && (numChar > 0 || dstart == 0)) {
                        r = ""
                        break
                    }
                }
                r
            }

            cell.filters = arrayOf(filter)
        } else {
            cell.isFocusable = false
            cell.isClickable = false
        }

        return cell
    }

    private fun setUpMatrix(numRows: Int, numCols: Int) {
        if (numRows <= 0 || numCols <= 0) {
            return
        }

        val display = windowManager.defaultDisplay
        var size = Point()
        display.getSize(size)

        val colWidth = size.x / (numCols + 1)

        matrixGrid.rowCount = numRows
        matrixGrid.columnCount = numCols

        rrefGrid.rowCount = numRows
        rrefGrid.columnCount = numCols

        for (i in 0 until numRows * numCols) {
            matrixGrid.addView(createCell(colWidth, true))
            rrefGrid.addView(createCell(colWidth, false))
        }
    }

    private fun showResult() {
        val matrix = Matrix.zeroMatrix(numRows, numCols)
        for (i in 0 until numRows * numCols) {
            val cell = matrixGrid.getChildAt(i) as EditText
            matrix[i] = Fraction(cell.text.toString())
        }

        val result = when (type.selectedItemPosition) {
            0 -> matrix.solveRref()
            1 -> matrix.solveInverse()
            else -> Matrix.zeroMatrix(numRows, numCols)
        }

        for (i in 0 until numRows * numCols) {
            val cell = rrefGrid.getChildAt(i) as EditText
            cell.setText(result[i].toString())
        }

        hideKeyboard()
    }

    private fun hideKeyboard() {
        val imm = this.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = this.getCurrentFocus();

        if (view == null) {
            view = View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}