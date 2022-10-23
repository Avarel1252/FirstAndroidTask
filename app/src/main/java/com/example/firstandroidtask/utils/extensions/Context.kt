package com.example.firstandroidtask.utils.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.shortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.shortToast(@StringRes res: Int) {
    Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
}