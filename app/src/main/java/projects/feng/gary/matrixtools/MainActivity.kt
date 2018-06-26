package projects.feng.gary.matrixtools

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
