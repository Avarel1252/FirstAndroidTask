package com.example.firstandroidtask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firstandroidtask.databinding.MyProfileBinding

class MyProfileActivity : AppCompatActivity() {

    private lateinit var bind: MyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = MyProfileBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setUsenameText()

    }

    private fun setUsenameText() {
        val extra: String = intent.getStringExtra("email") ?: ""
        val namePattern = Regex(pattern = "([a-zA-Z]+.)")
        val surnamePattern = Regex(pattern = "(.[a-zA-Z]+)")
        val username =
            namePattern.find(extra).toString() + " " + surnamePattern.find(extra).toString()
        bind.tvUsername.text = username
    }
}
