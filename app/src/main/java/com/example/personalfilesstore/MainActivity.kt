package com.example.personalfilesstore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(FirebaseAuth.getInstance().currentUser!!.uid.toString()!="")
        {
            startActivity(Intent(this,Home::class.java))
        }
        val email:EditText=findViewById(R.id.EdtUSername)
        val password: EditText =findViewById(R.id.EdtPassword)
        val signup:TextView=findViewById(R.id.txtSignup)
        val login:Button=findViewById(R.id.btnLogin)
        signup.setOnClickListener {
            startActivity(Intent(this,Signup::class.java))
        }
        login.setOnClickListener {
                if(email.text.toString().equals("")||password.text.toString().equals(""))
                {
                    val alertd= AlertDialog.Builder(this)
                    alertd.setMessage("Please fill all the details to continue")
                    alertd.setTitle("Empty Username or Password")
                    alertd.setPositiveButton("Ok"){dialog,_->
                        dialog.dismiss()
                    }
                    alertd.setNegativeButton("Cancel"){dialog,_->
                        dialog.dismiss()
                    }
                    alertd.create().show()
                }
                else {
                    login(email.text.toString(), password.text.toString())
                }
            }
            }

    private fun login(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful)
            {
                startActivity(Intent(this,Home::class.java))
                Toast.makeText(this@MainActivity,"Login successful",Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this@MainActivity,"Wrong password or email",Toast.LENGTH_LONG).show()
            }
        }
    }
}
