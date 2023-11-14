package com.example.personalfilesstore

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import com.example.personalfilesstore.models.FileModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.UUID

class AddNew : AppCompatActivity() {
    private lateinit var FileUrl: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new)
        val btnBrowser: Button =findViewById(R.id.btnBrowseFile)
        val fileName:EditText=findViewById(R.id.EdtFilename)
        btnBrowser.setOnClickListener {
            selectImage()
        }
        val btnSave:Button=findViewById(R.id.btnSaveFile)
        btnSave.setOnClickListener {
            val file_name=fileName.text.toString()
            if(file_name!=""||FileUrl.toString()!="")
            {
                val file=FileModel(file_name,FileUrl.toString())
                val key=FirebaseDatabase.getInstance().reference.push().key
                FirebaseDatabase.getInstance().reference.child("Files").child(FirebaseAuth.getInstance().currentUser!!.uid).child(key.toString())
                    .setValue(file).addOnCompleteListener {
                    Toast.makeText(this,"File add  successful", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun openCamera() {
        val camera_intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(camera_intent, 123)
    }
    private fun selectImage() {
        val choice = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val myAlertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        myAlertDialog.setTitle("Select Image")
        myAlertDialog.setItems(choice, DialogInterface.OnClickListener { dialog, item ->
            when {
                // Select "Choose from Gallery" to pick image from gallery
                choice[item] == "Choose from Gallery" -> {
                    val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                    startActivityForResult(gallery, 100)
                }
                // Select "Take Photo" to take a photo
                choice[item] == "Take Photo" -> {
                    openCamera()
                }
                // Select "Cancel" to cancel the task
                choice[item] == "Cancel" -> {
                    dialog.dismiss()
                }
            }
        })
        myAlertDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode === 123) {
            val photo=data!!.extras!!["data"] as Bitmap
            // BitMap is data structure of image file which store the image in memory
            // Set the image in imageview for display
            uploadImage(photo)
        }
        else if(requestCode===100) {
            val imageSelected = data?.data
            FileUrl=imageSelected!!.path!!.toUri()
            val pathColumn = arrayOf(MediaStore.Images.Media.DATA)
            if (imageSelected != null) {
                val contentResolver=this.contentResolver
                val myCursor = contentResolver.query(
                    imageSelected,
                    pathColumn, null, null, null
                )
                // Setting the image to the ImageView
                if (myCursor != null) {
                    myCursor.moveToFirst()
                    val columnIndex = myCursor.getColumnIndex(pathColumn[0])
                    val picturePath = myCursor.getString(columnIndex)
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageSelected)
                    uploadImage(imageSelected)
                    myCursor.close()
                }
            }
        }
    }

    fun uploadImage(fileUri: Bitmap) {
        // on below line checking weather our file uri is null or not.
        if (fileUri != null) {
            // on below line displaying a progress dialog when uploading an image.
            val progressDialog = ProgressDialog(this)
            // on below line setting title and message for our progress dialog and displaying our progress dialog.
            progressDialog.setTitle("Uploading...")
            progressDialog.setMessage("Uploading your image..")
            progressDialog.show()

            // on below line creating a storage refrence for firebase storage and creating a child in it with
            // random uuid.
            val ref: StorageReference = FirebaseStorage.getInstance().getReference()
                .child(UUID.randomUUID().toString())
            // on below line adding a file to our storage.
            val baos= ByteArrayOutputStream()
            fileUri.compress(Bitmap.CompressFormat.JPEG,100,baos)
            val data=baos.toByteArray()
            ref.putBytes(data).addOnSuccessListener {
                // this method is called when file is uploaded.
                // in this case we are dismissing our progress dialog and displaying a toast message
                it.metadata!!.reference!!.downloadUrl.addOnSuccessListener {uri->
                    FileUrl=uri
                }
                progressDialog.dismiss()
                Toast.makeText(this, "Image Uploaded..", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                // this method is called when there is failure in file upload.
                // in this case we are dismissing the dialog and displaying toast message
                progressDialog.dismiss()
                Toast.makeText(this, "Fail to Upload Image..", Toast.LENGTH_SHORT)
                    .show()
            }.addOnCompleteListener {
                if(it.isComplete)
                {
                    Toast.makeText(this,FileUrl.toString(),Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun uploadImage(fileUri: Uri) {
// on below line checking weather our file uri is null or not.
        if (fileUri != null) {
            // on below line displaying a progress dialog when uploading an image.
            val progressDialog = ProgressDialog(this)
            // on below line setting title and message for our progress dialog and displaying our progress dialog.
            progressDialog.setTitle("Uploading...")
            progressDialog.setMessage("Uploading your image..")
            progressDialog.show()

            // on below line creating a storage refrence for firebase storage and creating a child in it with
            // random uuid.
            val ref: StorageReference = FirebaseStorage.getInstance().getReference()
                .child(UUID.randomUUID().toString())
            // on below line adding a file to our storage.
            ref.putFile(fileUri!!).addOnSuccessListener {
                // this method is called when file is uploaded.
                // in this case we are dismissing our progress dialog and displaying a toast message
                progressDialog.dismiss()
                it.metadata!!.reference!!.downloadUrl.addOnSuccessListener {uri->
                    FileUrl=uri
                }
                Toast.makeText(this, "Image Uploaded..", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                // this method is called when there is failure in file upload.
                // in this case we are dismissing the dialog and displaying toast message
                progressDialog.dismiss()
                Toast.makeText(this, "Fail to Upload Image..", Toast.LENGTH_SHORT)
                    .show()
            }.addOnCompleteListener {
            }
        }
    }
}