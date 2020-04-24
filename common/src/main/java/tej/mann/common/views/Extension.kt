package tej.mann.common.views

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, length).show()
}

fun TextView.setTextSafe(text: CharSequence?) {
    this.text = text
    if (text.isNullOrBlank()) visibility = View.GONE
}
