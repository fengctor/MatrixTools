package projects.feng.gary.matrixtools

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import projects.feng.gary.matrixtools.R.id.*

const val EXTRA_NUM_ROWS = "number_of_rows"
const val EXTRA_NUM_COLS = "number_of_cols"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*findViewById<View>(R.id.rrefButton).setOnClickListener {
            val text = findViewById<TextView>(R.id.text)

            val test = arrayOf(Fraction(5), Fraction(3), Fraction(2),
                               Fraction("3/2"), Fraction(0), Fraction("1/2"),
                               Fraction(3), Fraction(0), Fraction(1),
                               Fraction(6), Fraction(0), Fraction(2))
            val matrix = Matrix(test, 4, 3)
            val rref = matrix.getRref()

            for (i in 0..3) {
                for (j in 0..2) {
                    text.append(rref[i, j].toString())
                    text.append("\t\t");
                }
                text.append("\n")
            }
        }*/

        val rrefButton = rrefCalculatorButton as Button
        rrefButton.setOnClickListener({
            val numRows = (numRowsEditText as EditText).getIntText()
            val numCols = (numColsEditText as EditText).getIntText()

            val intent = Intent(this, RREFActivity::class.java).apply {
                putExtra(EXTRA_NUM_ROWS, numRows)
                putExtra(EXTRA_NUM_COLS, numCols)
            }

            startActivity(intent)
        })
    }

    fun EditText.getIntText(): Int {
        return text.toString().toInt()
    }


}
