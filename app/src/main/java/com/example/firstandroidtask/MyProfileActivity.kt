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
        setUsernameText()

    }

    private fun setUsernameText() {
        val extra: String = intent.getStringExtra("email")!!
        val name = extra.substringBefore(".")
        val surname = extra.substringAfter(".").substringBefore("@")
        val username = "$name $surname"
        bind.tvUsername.text = username
    }
}
