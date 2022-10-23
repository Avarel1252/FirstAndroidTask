package com.example.firstandroidtask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firstandroidtask.databinding.ActivityMyProfileBinding
import com.example.firstandroidtask.utils.ParseData

class MyProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUsernameText()
    }

    private fun setUsernameText() {
        val extra: String = intent.getStringExtra("email")!!
        val (name, surname) = ParseData().parseUserName(extra)
        binding.tvUsername.text = getString(R.string.userName, name, surname)
    }


}
