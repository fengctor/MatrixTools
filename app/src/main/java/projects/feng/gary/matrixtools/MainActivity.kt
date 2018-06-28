package projects.feng.gary.matrixtools

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSpinners()

        val startButton = startToolButton
        startButton.setOnClickListener({ _ ->
            val numRows = rowSpinner.selectedItem as Int
            val numCols = colSpinner.selectedItem as Int

            val intent = Intent(this, RREFActivity::class.java).apply {
                putExtra(EXTRA_NUM_ROWS, numRows)
                putExtra(EXTRA_NUM_COLS, numCols)
            }

            startActivity(intent)
        })
    }

    private fun initSpinners() {
        val adapter = ArrayAdapter<String>(this, R.layout.spinner_holder)
        adapter.setDropDownViewResource(R.layout.spinner_row)
        toolType.adapter = adapter

        val choices = listOf("RREF Calculator")
        adapter.addAll(choices)

        val dimensionAdapter = ArrayAdapter<Int>(this, R.layout.spinner_holder)
        adapter.setDropDownViewResource(R.layout.spinner_row)
        rowSpinner.adapter = dimensionAdapter
        val possibleRows = listOf(1, 2, 3, 4, 5)
        dimensionAdapter.addAll(possibleRows)

        colSpinner.adapter = dimensionAdapter
    }

}
