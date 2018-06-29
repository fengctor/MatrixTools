package projects.feng.gary.matrixtools

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(homeBar as Toolbar)
        supportActionBar?.title = getString(R.string.home_title)

        initSpinners()

        val startButton = startToolButton
        startButton.setOnClickListener({ _ ->
            val numRows = rowSpinner.selectedItem as Int
            val numCols = colSpinner.selectedItem as Int

            val intent = Intent(this, CalculatorActivity::class.java).apply {
                putExtra(EXTRA_NUM_ROWS, numRows)
                putExtra(EXTRA_NUM_COLS, numCols)
            }

            startActivity(intent)
        })
    }

    private fun initSpinners() {
        val dimensionAdapter = ArrayAdapter<Int>(this, R.layout.spinner_holder)
        dimensionAdapter.setDropDownViewResource(R.layout.spinner_row)
        rowSpinner.adapter = dimensionAdapter
        val possibleRows = listOf(1, 2, 3, 4, 5, 6)
        dimensionAdapter.addAll(possibleRows)

        colSpinner.adapter = dimensionAdapter
    }

}
