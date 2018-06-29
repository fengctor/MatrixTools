package projects.feng.gary.matrixtools

import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
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
import kotlin.math.min

class CalculatorActivity : AppCompatActivity() {

    val numRows by lazy {
        intent.getIntExtra(EXTRA_NUM_ROWS, 0)
    }
    val numCols by lazy {
        intent.getIntExtra(EXTRA_NUM_COLS, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        setSupportActionBar(calcBar as Toolbar)
        supportActionBar?.title = getString(R.string.calculator_title)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        setUpMatrix(numRows, numCols)
        initChoiceSpinner()

        getButton.setOnClickListener({ showResult() })
    }

    private fun initChoiceSpinner() {
        val adapter = ArrayAdapter<String>(this, R.layout.spinner_holder)
        adapter.setDropDownViewResource(R.layout.spinner_row)
        type.adapter = adapter

        val choices = listOf(
                getString(R.string.calculator_rref),
                getString(R.string.calculator_inverse))
        adapter.addAll(choices)
    }

    private fun createCell(width: Int, focusable: Boolean): EditText {
        val cell = EditText(this)
        val gridParam = GridLayout.LayoutParams()
        gridParam.width = width
        cell.setLayoutParams(gridParam)

        cell.textSize = min(width.toFloat() / 16f, 24f)
        cell.maxLines = 1
        cell.gravity = Gravity.CENTER
        if (focusable) {
            cell.imeOptions = cell.imeOptions or EditorInfo.IME_FLAG_NO_EXTRACT_UI
            cell.inputType = InputType.TYPE_CLASS_PHONE
            val filter = InputFilter { source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int ->
                var replace: CharSequence? = null
                for (i in start until end) {
                    val char = source.get(i)
                    if (!Character.isDigit(char) && char != '/' && char != '.' && char != '-') {
                        replace = ""
                        break
                    }
                    val numChar = dest.fold (0, { acc, c -> if (c == char) acc + 1 else 0 })
                    if (char == '-' && (dstart != 0 || numChar > 0)) {
                        replace = ""
                        break
                    }
                    if ((char == '/' || char == '.') && (numChar > 0 || dstart == 0)) {
                        replace = ""
                        break
                    }
                }
                replace
            }

            cell.filters = arrayOf(filter)
        } else {
            cell.isFocusable = false
            cell.isClickable = false
            cell.background = null

            cell.setOnClickListener({ sendToast(R.string.wrong_cell) })
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

        resultGrid.rowCount = numRows
        resultGrid.columnCount = numCols

        for (i in 0 until numRows * numCols) {
            matrixGrid.addView(createCell(colWidth, true))
            resultGrid.addView(createCell(colWidth, false))
        }
    }

    private fun showResult() {
        hideKeyboard()

        val matrix = getGridAsMatrix()

        val result = when (type.selectedItemPosition) {
            0 -> matrix.solveRref()
            1 -> matrix.solveInverse()
            else -> Matrix.zeroMatrix(numRows, numCols)
        }

        if (result == null) {
            sendToast(R.string.not_applicable)
            return
        }

        for (i in 0 until numRows * numCols) {
            val cell = resultGrid.getChildAt(i) as EditText
            cell.setText(result[i].toString())
        }
    }

    private fun getGridAsMatrix(): Matrix {
        val matrix = Matrix.zeroMatrix(numRows, numCols)
        for (i in 0 until numRows * numCols) {
            val cell = matrixGrid.getChildAt(i) as EditText
            matrix[i] = Fraction(cell.text.toString())
        }

        return matrix
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
