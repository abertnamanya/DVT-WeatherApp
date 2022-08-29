package za.co.dvt.weatherapp.utils

import android.content.Context
import android.widget.Toast

class ToastNotification(context: Context) {
    private val context = context
    fun showToastNotification(message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}