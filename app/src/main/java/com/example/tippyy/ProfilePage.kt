package com.example.tippyy

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProfilePage : AppCompatActivity() {
    private lateinit var bottomnav: BottomNavigationView
    private lateinit var firebaseAuth: FirebaseAuth
    var fileUri: Uri?=null

    lateinit var img:ImageView
    var pickedBitmap: Bitmap?=null
    lateinit var chooseimg:Button
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var b1:Button
    private lateinit var a1: EditText
    private lateinit var a2: EditText
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            fileUri = data?.data

            if (fileUri != null){
                val source = ImageDecoder.createSource(this.contentResolver,fileUri!!)
                pickedBitmap = ImageDecoder.decodeBitmap(source)
                val imageSelect = findViewById<ImageView>(R.id.imageView)
                imageSelect.setImageBitmap(pickedBitmap)

            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val unameR=intent.getStringExtra("Username")
        a1=findViewById(R.id.a1)

        img = findViewById(R.id.imageView)
        a2=findViewById(R.id.a2)
        b1=findViewById(R.id.save)
        firebaseAuth=FirebaseAuth.getInstance()
        databaseReference= FirebaseDatabase.getInstance().getReference("Users")

        chooseimg = findViewById(R.id.button1)
        chooseimg.setOnClickListener{
            pickImage()
        }

        b1.setOnClickListener{
            val uid=firebaseAuth.currentUser?.uid
            if (uid != null) {
                databaseReference.child(uid).setValue(User(unameR,"Donator",a1.text.toString(),a2.text.toString())).addOnCompleteListener{
                    if(it.isSuccessful){
                        uploadImage()
                    }else{
                        Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        Toast.makeText( this@ProfilePage,"$unameR", Toast.LENGTH_SHORT).show()
        bottomnav=findViewById(R.id.bottnav)
        bottomnav.setOnItemSelectedListener{
            when(it.itemId){
                R.id.home->{
                    val logger= Intent(this,HomePage::class.java).putExtra("Username",unameR)
                    startActivity(logger)
                    overridePendingTransition(0, 0)
                    true

                }
                R.id.ourstories->{
                    val logger= Intent(this,StoryPage::class.java).putExtra("Username",unameR)
                    startActivity(logger)
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.profile->{
                    true
                }
                else -> {false}
            }

        }
    }
    private fun pickImage(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //No Permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }else {
            openGallery()
        }
    }
    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(galleryIntent)
    }
    fun uploadImage() {
        if (fileUri != null) {
            val ref: StorageReference = FirebaseStorage.getInstance().getReference("Users/"+firebaseAuth.currentUser?.uid+"/").child("Profile_pic")
            ref.putFile(fileUri!!).addOnSuccessListener {
                Toast.makeText(this,"Data updated",Toast.LENGTH_SHORT).show()
            }
        }
    }
}