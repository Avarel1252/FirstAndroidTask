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
    private lateinit var intent1: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = SignupBinding.inflate(layoutInflater)
        setContentView(bind.root)

        allAccounts = getSharedPreferences("allAccounts", MODE_PRIVATE)
        rememberedAccount = getSharedPreferences("rememberedAccounts", MODE_PRIVATE)
        rememberedAccountEditor = rememberedAccount.edit()
        intent1 = Intent(applicationContext, MyProfileActivity::class.java)

        bind.tInEmail.editText?.setText(rememberedAccount.getString("email", ""))
        bind.tInPassword.editText?.setText(rememberedAccount.getString("password", ""))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("email", bind.tInEmail.editText?.text.toString())
        outState.putString("password", bind.tInPassword.editText?.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        bind.tInEmail.editText?.setText(savedInstanceState.getString("email", ""))
        bind.tInPassword.editText?.setText(savedInstanceState.getString("password", ""))
    }

    override fun onStop() {
        super.onStop()
        saveFieldsState()
    }

    fun signIn(view: View) {
        val email = bind.tInEmail.editText?.text.toString()
        val password = bind.tInPassword.editText?.text.toString()
        if (checkUserReg(email, password)) {
            intent1.putExtra("email", email)
            startActivity(intent1)
        } else Toast.makeText(applicationContext, R.string.userNotReged, Toast.LENGTH_SHORT).show()
    }

    private fun checkUserReg(email: String, password: String): Boolean {
        if (allAccounts.contains(email)) {
            return allAccounts.getString(email, null) == password
        }
        return false
    }

    fun registration(view: View) {
        val allAccountsEditor = allAccounts.edit()
        val email = bind.tInEmail.editText?.text.toString()
        val password = bind.tInPassword.editText?.text.toString()
        if (isValidEmail(email) && isValidPassword(password)) {
            if (allAccounts.contains(email)) {
                Toast.makeText(applicationContext, R.string.usedEmail, Toast.LENGTH_SHORT).show()
            } else {
                allAccountsEditor.putString(email, password)
                allAccountsEditor.apply()
                intent1.putExtra("email", email)
                startActivity(intent1)

            }
        }

    }

    private fun saveFieldsState() {
        if (bind.chkBoxRememberMe.isChecked) {
            rememberedAccountEditor.putString("email", bind.tInEmail.editText?.text.toString())
            rememberedAccountEditor.putString(
                "password",
                bind.tInPassword.editText?.text.toString()
            )
        } else {
            rememberedAccountEditor.putString("email", "")
            rememberedAccountEditor.putString("password", "")
        }
        rememberedAccountEditor.apply()
    }

    private fun isValidEmail(str: String): Boolean {
        val patternEmail = Regex(pattern = "[a-zA-Z]+\\.[a-zA-Z]+@[a-zA-Z]+\\.[a-zA-Z]+")
        if (str.isEmpty()) {
            bind.tInEmail.error = resources.getString(R.string.empty_email)
        } else if (!patternEmail.matches(str)) {
            bind.tInEmail.error = resources.getString(R.string.invalid_email)
        } else return true
        return false
    }

    private fun isValidPassword(str: String): Boolean {
        val pattern = Regex(pattern = "[a-zA-Z0-9_]+")
        if (str.isEmpty()) {
            bind.tInPassword.error = resources.getString(R.string.empty_password)
        } else if (!pattern.matches(str)) {
            bind.tInPassword.error = resources.getString(R.string.invalid_password)
        } else return true
        return false
    }
}
