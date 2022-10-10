package com.example.firstandroidtask

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firstandroidtask.databinding.SignupBinding


class AuthActivity : AppCompatActivity() {
    private lateinit var bind: SignupBinding
    private lateinit var allAccounts: SharedPreferences
    private lateinit var rememberedAccount: SharedPreferences
    private lateinit var rememberedAccountEditor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = SignupBinding.inflate(layoutInflater)
        allAccounts = getSharedPreferences("allAccounts", MODE_PRIVATE)
        rememberedAccount = getSharedPreferences("rememberedAccounts", MODE_PRIVATE)
        rememberedAccountEditor = rememberedAccount.edit()
        setContentView(bind.root)
        bind.tInEmail.editText?.setText(rememberedAccount.getString("email", ""))
        bind.tInPassword.editText?.setText(rememberedAccount.getString("password", ""))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        rememberedAccountEditor.putString("email", bind.tInEmail.editText?.text.toString())
        rememberedAccountEditor.putString("password", bind.tInPassword.editText?.text.toString())
        rememberedAccountEditor.apply()

    }

    fun signIn(view: View) {
        if (checkUserReg()) {
            intent.putExtra("email", bind.tInEmail.editText?.text.toString())
            startActivity(Intent(applicationContext, MyProfileActivity::class.java))
        } else Toast.makeText(applicationContext, R.string.userNotReged, Toast.LENGTH_SHORT).show()
    }

    private fun checkUserReg(): Boolean {
        val email = bind.tInEmail.editText?.text.toString()
        val password = bind.tInPassword.editText?.text.toString()
        if (allAccounts.contains(email)) {
            return allAccounts.getString(email, null) == password
        }
        return false
    }

    fun registration(view: View) {
        val allAccountsEditor = allAccounts.edit()
        val email = bind.tInEmail.editText?.text.toString()
        val password = bind.tInPassword.editText?.text.toString()
        val intent = Intent(applicationContext, MyProfileActivity::class.java)
        if (isValidEmail(email) && isValidPassword(password)) {
            saveFieldsState(email, password)
            if (allAccounts.contains(email)) {
                Toast.makeText(applicationContext, R.string.usedEmail, Toast.LENGTH_SHORT).show()
            } else {
                allAccountsEditor.putString(email, password)
                allAccountsEditor.apply()
                startActivity(intent)
            }
        }

    }

    private fun saveFieldsState(email: String, password: String) {
        if (bind.chkBoxRememberMe.isChecked) {
            Toast.makeText(applicationContext, "checked", Toast.LENGTH_SHORT).show()
            rememberedAccountEditor.putString("email", email)
            rememberedAccountEditor.putString("password", password)
        } else {
            Toast.makeText(applicationContext, "unchecked", Toast.LENGTH_SHORT).show()
            rememberedAccountEditor.putString("email", "")
            rememberedAccountEditor.putString("password", "")
        }
        rememberedAccountEditor.apply()
    }

    private fun isValidEmail(str: String): Boolean {
        val patternEmail = Regex(pattern = "[a-zA-Z]+.[a-zA-Z]+@[a-zA-Z]+.[a-zA-Z]+")
        if (str.isEmpty()) {
            bind.tInEmail.error = R.string.empty_email.toString()
        } else if (!patternEmail.matches(str)) {
            bind.tInEmail.error = R.string.invalid_email.toString()
        } else return true
        return false
    }

    private fun isValidPassword(str: String): Boolean {
        val pattern = Regex(pattern = "[a-zA-Z0-9_]+")
        if (str.isEmpty()) {
            bind.tInPassword.error = R.string.empty_password.toString()
        } else if (!pattern.matches(str)) {
            bind.tInPassword.error = R.string.invalid_password.toString()
        } else return true
        return false
    }
}
