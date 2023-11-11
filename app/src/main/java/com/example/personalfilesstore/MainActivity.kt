package com.example.personalfilesstore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val signup:TextView=findViewById(R.id.txtSignup)
        val login:Button=findViewById(R.id.btnLogin)
        signup.setOnClickListener {
            startActivity(Intent(this,Signup::class.java))
        }
        login.setOnClickListener {
            startActivity(Intent(this,Home::class.java))
        }
    }
}