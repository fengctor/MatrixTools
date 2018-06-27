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
import android.widget.EditText
import android.widget.GridLayout
import kotlinx.android.synthetic.main.activity_rref.*

class RREFActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_rref)

        setUpMatrix(numRows, numCols)

        getRrefButton.setOnClickListener(View.OnClickListener { showRref() })
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
        } else {
            cell.isFocusable = false
            cell.isClickable = false
        }

        val filter = InputFilter { source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int ->
            var r: CharSequence? = null
            for (i in start until end) {
                val char = source.get(i)
                if (!Character.isDigit(char) && char != '/' && char != '.' && char != '-') {
                    r = ""
                    break
                }
                val numChar = dest.filter { c -> c == char}.length
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

    private fun showRref() {
        val matrixArr = Array(numRows * numCols, { _ -> Fraction.zero })

        for (i in 0 until numRows * numCols) {
            val cell = matrixGrid.getChildAt(i) as EditText
            matrixArr[i] = Fraction(cell.text.toString())
        }

        val rref = Matrix(matrixArr, numRows, numCols).getRref()

        for (i in 0 until numRows * numCols) {
            val cell = rrefGrid.getChildAt(i) as EditText
            cell.setText(rref[i].toString())
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
