package com.example.firstandroidtask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firstandroidtask.R
import com.example.firstandroidtask.databinding.MyProfileBinding

class MyProfileActivity : AppCompatActivity() {

    private lateinit var bind: MyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = MyProfileBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setUsernameText()

    }

    private fun setUsernameText() {
        val email: String = intent.getStringExtra("email")!!
        val name = parseUserName(email)

        bind.tvUsername.text = getString(R.string.userName, name.first, name.second)
    }

    private fun parseUserName(email: String): Pair<String, String> {
        val name = email.substringBefore(".")
        val surname = email.substringAfter(".").substringBefore("@")
        return Pair(name, surname)
    }
}
