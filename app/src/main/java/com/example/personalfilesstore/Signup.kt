package com.example.personalfilesstore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Signup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val email: EditText =findViewById(R.id.EdtEmail)
        val name:EditText=findViewById(R.id.EdtName)
        val password: EditText =findViewById(R.id.EdtPassword)
        val confirmPassword:EditText=findViewById(R.id.EdtConfirmPassword)
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val btnSignup: Button =findViewById(R.id.btnSignup)
        btnSignup.setOnClickListener {
            if(email.text.toString().equals("")||password.text.toString().equals("")||name.text.toString().equals(""))
            {
                if(password.text.toString()!=confirmPassword.text.toString())
                {
                    val alertdialog= AlertDialog.Builder(this)
                    alertdialog.setMessage("Password and confirm password donot match")
                    alertdialog.setTitle("Error encountered")
                    alertdialog.setPositiveButton("Ok"){dialog,_->
                        dialog.dismiss()
                    }
                    alertdialog.setNegativeButton("Cancel"){dialog,_->
                        dialog.dismiss()
                    }
                    alertdialog.create().show()
                }
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
                signup(name.text.toString(),email.text.toString(), password.text.toString())
            }
        }
    }

    private fun signup(name: String, email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful)
            {
                val user=User(name,email,password)
                FirebaseDatabase.getInstance().reference.child(it.result.user!!.uid.toString()).setValue(user).addOnCompleteListener {
                    Toast.makeText(this@Signup,"Registration successful", Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(this@Signup,"Error encountered try again", Toast.LENGTH_LONG).show()
            }
        }
    }
    }