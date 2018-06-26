package projects.feng.gary.matrixtools

import android.widget.EditText

fun EditText.getIntText(): Int {
    return text.toString().toInt()
}
