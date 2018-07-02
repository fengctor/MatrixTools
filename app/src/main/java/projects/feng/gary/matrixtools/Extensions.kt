package projects.feng.gary.matrixtools

import android.annotation.TargetApi
import android.content.Context
import android.graphics.PorterDuff
import android.view.Gravity
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.math.BigInteger


fun BigInteger.abs(): BigInteger = if (this < 0.toBigInteger()) -this else this


@TargetApi(27)
fun Context.sendToast(messageRes: Int, vararg formatArgs: Any) {
    val message = getString(messageRes, *formatArgs)
    val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    val toastView = toast.view

    toastView.background.setColorFilter(
            if (BuildConfig.VERSION_CODE < 23) {
                resources.getColor(R.color.colorPrimary)
            } else {
                resources.getColor(R.color.colorPrimary, theme)
            },
            PorterDuff.Mode.SRC_IN)
    toastView.findViewById<TextView>(android.R.id.message).gravity = Gravity.CENTER
    toast.show()
}