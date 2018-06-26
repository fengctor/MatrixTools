package projects.feng.gary.matrixtools

import android.widget.EditText

fun EditText.getIntText(): Int {
    return text.toString().toInt()
}

fun Int.abs(): Int {
    return if (this < 0) -this else this;
}