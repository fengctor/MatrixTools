package projects.feng.gary.matrixtools

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rrefButton = rrefCalculatorButton
        rrefButton.setOnClickListener({
            val numRows = numRowsEditText.getIntText()
            val numCols = numColsEditText.getIntText()

            val intent = Intent(this, RREFActivity::class.java).apply {
                putExtra(EXTRA_NUM_ROWS, numRows)
                putExtra(EXTRA_NUM_COLS, numCols)
            }

            startActivity(intent)
        })
    }

}
