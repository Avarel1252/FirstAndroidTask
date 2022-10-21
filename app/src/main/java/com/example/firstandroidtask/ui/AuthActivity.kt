package com.example.firstandroidtask.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firstandroidtask.R
import com.example.firstandroidtask.databinding.ActivityAuthBinding
import com.example.firstandroidtask.utils.ext.shortToast


class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var allAccounts: SharedPreferences
    private lateinit var rememberedAccount: SharedPreferences
    private lateinit var rememberedAccountEditor: SharedPreferences.Editor
    private lateinit var launchProfileActivityIntent: Intent

    companion object {
        const val EMAIL_BUNDLE_KEY = "email"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSharedPreferences()
        launchProfileActivityIntent = Intent(this, MyProfileActivity::class.java)
        bindCredentialsAutoFill()
    }

    private fun bindCredentialsAutoFill() {
        binding.etEmail?.setText(
            rememberedAccount.getString(
                "email",
                ""
            )
        ) // move prefs keys to constants
        binding.etPassword?.setText(rememberedAccount.getString("password", ""))
    }

    private fun initSharedPreferences() {
        allAccounts = getSharedPreferences("allAccounts", MODE_PRIVATE)
        rememberedAccount = getSharedPreferences("rememberedAccounts", MODE_PRIVATE)
        rememberedAccountEditor = rememberedAccount.edit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            "email",
            binding.tInEmail.editText?.text.toString()
        ) // move keys to constants
        outState.putString("password", binding.tInPassword.editText?.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.tInEmail.editText?.setText(savedInstanceState.getString("email", ""))
        binding.tInPassword.editText?.setText(savedInstanceState.getString("password", ""))
    }

    override fun onStop() {
        super.onStop()
        saveFieldsState()
    }

    fun signIn(view: View) {
        val email = binding.tInEmail.editText?.text.toString()
        val password = binding.tInPassword.editText?.text.toString()
        if (checkUserReg(email, password)) {
            launchProfileActivityIntent.putExtra("email", email)
            startActivity(launchProfileActivityIntent)
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
        val email = binding.etEmail?.text.toString()
        val password = binding.tInPassword.editText?.text.toString()
        val error = isValidEmail(email)
        error?.let {
            binding.tInEmail.error = getString(it)
        }
        if (true) {
//        if (isValidEmail(email) && isValidPassword(password)) {
            if (allAccounts.contains(email)) {
                shortToast(R.string.usedEmail)
            } else {
                allAccountsEditor.putString(email, password)
                allAccountsEditor.apply()
                launchProfileActivityIntent.putExtra("email", email)
                startActivity(launchProfileActivityIntent)
            }
        }

    }

    private fun saveFieldsState() {
        if (binding.chkBoxRememberMe.isChecked) {
            rememberedAccountEditor.putString("email", binding.tInEmail.editText?.text.toString())
            rememberedAccountEditor.putString(
                "password",
                binding.tInPassword.editText?.text.toString()
            )
        } else {
            rememberedAccountEditor.clear()
        }
        rememberedAccountEditor.apply()
    }

    // returns string res id, null if no error
    private fun isValidEmail(str: String): Int? {
        val patternEmail = Regex(pattern = "[a-zA-Z]+\\.[a-zA-Z]+@[a-zA-Z]+\\.[a-zA-Z]+")
        return if (str.isEmpty()) {
            R.string.empty_email
        } else if (!patternEmail.matches(str)) {
            R.string.invalid_email
        } else {
            null
        }
    }

    private fun isValidPassword(str: String): Boolean {
        val pattern = Regex(pattern = "[a-zA-Z0-9_]+")
        if (str.isEmpty()) {
            binding.tInPassword.error = resources.getString(R.string.empty_password)
        } else if (!pattern.matches(str)) {
            binding.tInPassword.error = resources.getString(R.string.invalid_password)
        } else {
            return true
        }
        return false
    }
}
