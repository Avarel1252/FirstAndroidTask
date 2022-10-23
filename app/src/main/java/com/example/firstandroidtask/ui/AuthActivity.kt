package com.example.firstandroidtask.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.firstandroidtask.R
import com.example.firstandroidtask.databinding.ActivityAuthBinding
import com.example.firstandroidtask.utils.Constants
import com.example.firstandroidtask.utils.Verificator
import com.example.firstandroidtask.utils.extensions.shortToast


class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var allAccountsPref: SharedPreferences
    private lateinit var rememberedAccount: SharedPreferences
    private lateinit var launchProfileActivityIntent: Intent
    private lateinit var verificator: Verificator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        credentialsAutoFill()
    }

    private fun credentialsAutoFill() {
        with(binding) {
            etEmail?.setText(
                rememberedAccount.getString(Constants.SHARED_PREF_EMAIL_KEY, "")
            )
            etPassword?.setText(
                rememberedAccount.getString(Constants.SHARED_PREF_PASSWORD_KEY, "")
            )
        }

    }

    private fun init() {
        verificator = Verificator()
        allAccountsPref = getSharedPreferences("allAccounts", MODE_PRIVATE)
        rememberedAccount = getSharedPreferences("rememberedAccounts", MODE_PRIVATE)
        launchProfileActivityIntent = Intent(applicationContext, MyProfileActivity::class.java)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        with(outState) {
            putString(Constants.EMAIL_BUNDLE_KEY, binding.etEmail?.text.toString())
            putString(Constants.PASSWORD_BUNDLE_KEY, binding.etPassword?.text.toString())
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        with(binding) {
            etEmail?.setText(savedInstanceState.getString(Constants.EMAIL_BUNDLE_KEY, ""))
            etPassword?.setText(savedInstanceState.getString(Constants.PASSWORD_BUNDLE_KEY, ""))
        }
    }

    override fun onStop() {
        super.onStop()
        saveFieldsState()
    }

    fun signIn(view: View) {
        val email = binding.etEmail?.text.toString()
        val password = binding.etPassword?.text.toString()

        if (verificator.checkUserReg(allAccountsPref, email, password)) {
            launchProfileActivityIntent.putExtra(Constants.EXTRA_EMAIL_KEY, email)
            startActivity(launchProfileActivityIntent)
        } else {
            shortToast(R.string.userNotReged)
        }
    }


    fun registration(view: View) {
        val allAccountsPrefEditor = allAccountsPref.edit()
        val email = binding.etEmail?.text.toString()
        val password = binding.etPassword?.text.toString()

        val emailValid =
            verificator.isValidEmail(email)?.let { binding.tInEmail.error = getString(it) }
        val passwordValid =
            verificator.isValidPassword(password)?.let { binding.tInPassword.error = getString(it) }

        if (emailValid == null && passwordValid == null) {
            if (allAccountsPref.contains(email)) {
                shortToast(R.string.usedEmail)
            } else {
                allAccountsPrefEditor.putString(email, password)
                allAccountsPrefEditor.apply()
                launchProfileActivityIntent.putExtra(Constants.EXTRA_EMAIL_KEY, email)
                startActivity(launchProfileActivityIntent)
            }
        }
    }

    private fun saveFieldsState() {
        with(rememberedAccount.edit()) {
            if (binding.chkBoxRememberMe.isChecked) {
                putString(
                    Constants.SHARED_PREF_EMAIL_KEY,
                    binding.etEmail?.text.toString()
                )
                putString(
                    Constants.SHARED_PREF_PASSWORD_KEY,
                    binding.etPassword?.text.toString()
                )
            } else {
                clear()
            }
            apply()
        }
    }


}
