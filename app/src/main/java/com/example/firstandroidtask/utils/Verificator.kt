package com.example.firstandroidtask.utils

import android.content.SharedPreferences
import com.example.firstandroidtask.R

class Verificator {

    // returns string res id, null if no error
    fun isValidEmail(str: String): Int? {
        val patternEmail = Regex(pattern = "[a-zA-Z]+\\.[a-zA-Z]+@[a-zA-Z]+\\.[a-zA-Z]+")
        return if (str.isEmpty()) {
            R.string.empty_email
        } else if (!patternEmail.matches(str)) {
            R.string.invalid_email
        } else {
            null
        }
    }

    // returns string res id, null if no error
    fun isValidPassword(str: String): Int? {
        val pattern = Regex(pattern = "[a-zA-Z0-9_]+")
        return if (str.isEmpty()) {
            R.string.empty_password
        } else if (!pattern.matches(str)) {
            R.string.invalid_password
        } else {
            null
        }
    }

    // if user registered and correct password -> true
    fun checkUserReg(shPref: SharedPreferences, email: String, password: String): Boolean {
        if (shPref.contains(email)) {
            return shPref.getString(email, null) == password
        }
        return false
    }
}