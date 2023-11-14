package com.example.personalfilesstore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.personalfilesstore.adapters.FilesAdapter
import com.example.personalfilesstore.models.FileModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Home : AppCompatActivity() {
    var list=ArrayList<FileModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val addNew:FloatingActionButton=findViewById(R.id.addnew)
        addNew.setOnClickListener {
            startActivity(Intent(this@Home,AddNew::class.java))
        }
        FirebaseDatabase.getInstance().reference.child("Files").child(FirebaseAuth.getInstance().currentUser!!.uid.toString()).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children) {
                    val name = snap.child("Name").value.toString()
                    val url = snap.child("url").value.toString()
                    val file = FileModel(name, url)
                    list.add(file)
                    val rcview:RecyclerView=findViewById(R.id.rcview)
                    val layout:LayoutManager=LinearLayoutManager(this@Home)
                    val adapter=FilesAdapter(list)
                    rcview.adapter=adapter
                    rcview.layoutManager=layout
                }
            }

            override fun onCancelled(error: DatabaseError) {
               Toast.makeText(this@Home,"Error",Toast.LENGTH_LONG).show()
            }

        })


    }
}