package com.example.tippyy

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.component1
import com.google.firebase.storage.component2


class OtherPage : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    lateinit var chooseimg:Button
    lateinit var uploadimg:Button
    lateinit var img:ImageView
    lateinit var firebaseAuth: FirebaseAuth
    var fileUri:Uri?=null
    var pickedBitmap:Bitmap?=null
    private lateinit var storage: FirebaseStorage

    lateinit var clname:EditText
    lateinit var lv:ListView
    lateinit var cldonlist:ArrayList<String>

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

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_other_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        storage=FirebaseStorage.getInstance()


        val unamerecieved=intent.getStringExtra("Username")
        chooseimg = findViewById(R.id.button1)
        uploadimg = findViewById(R.id.button2)
        img = findViewById(R.id.imageView)

        chooseimg.setOnClickListener{
            pickImage()
        }


        cldonlist = ArrayList()
        update_list()
        Toast.makeText( this@OtherPage,"$unamerecieved", Toast.LENGTH_SHORT).show()

        val clothtyparr =arrayOf("Type", "Toys","linen","misc. Materials","Sanitary Items","Toiletries","stationary","shoes","household items")
        val clothsizearr = arrayOf("Quantity", "1","2","3","4","5","10","25","30","50","100")
        clname = findViewById(R.id.listname)

        val spinid1 = findViewById<Spinner>(R.id.clothingtype)
        val arradpter1 = ArrayAdapter(
            this@OtherPage,
            android.R.layout.simple_spinner_dropdown_item,
            clothtyparr
        )
        spinid1.adapter = arradpter1
        var s1: String = ""
        spinid1?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    Toast.makeText(
                        this@OtherPage,
                        "Type - ${clothtyparr[position]}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                s1 = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val spinid2 = findViewById<Spinner>(R.id.clothingsize)
        val arradpter2 = ArrayAdapter(
            this@OtherPage,
            android.R.layout.simple_spinner_dropdown_item,
            clothsizearr
        )
        spinid2.adapter = arradpter2
        var s2: String = ""
        spinid2?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    Toast.makeText(
                        this@OtherPage,
                        "Size is ${clothsizearr[position]}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                s2 = parent?.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        lv = findViewById(R.id.listView)
        val adapter = ArrayAdapter<String?>(
            this@OtherPage,
            android.R.layout.simple_list_item_1,
            cldonlist as List<String?>
        )
        lv.adapter = adapter

        uploadimg.setOnClickListener {
            if (s1 == "Type" || s2 == "Quality") {
                Toast.makeText(
                    this@OtherPage,
                    "Please enter valid type/quantity",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Log.i("Test Credentials", "${clname.text.toString()},${s1},${s2}")
                val item = clname.text.toString()
                if (item.isNotEmpty()) {
                    update_list()
                    adapter.notifyDataSetChanged()
                }
                if (unamerecieved != null) {
                    uploadImage(item,s1,s2)
                }
                else{
                    uploadImage(item,s1,s2)
                }
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
        val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(galleryIntent)
    }
    private fun update_list(){
        cldonlist.clear()
        firebaseAuth= FirebaseAuth.getInstance()
        storage=FirebaseStorage.getInstance()
        val listref=storage.getReference("Users/"+firebaseAuth.currentUser?.uid+"/Items")
        listref.listAll().addOnSuccessListener { (items,_)->
            for (item in items){
                cldonlist.add(item.name)
            }
        }.addOnFailureListener {
            Log.i("fail", "failure")
        }
    }

    private fun uploadImage(item:String, s1:String, s2:String) {
        firebaseAuth= FirebaseAuth.getInstance()
        if (fileUri != null) {
            val ref: StorageReference = FirebaseStorage.getInstance().getReference("Users/"+firebaseAuth.currentUser?.uid+"/Items/").child("OTHER_\"$item\"_Type_${s1}_Quantity_${s2}")
            ref.putFile(fileUri!!).addOnSuccessListener {
                Toast.makeText(applicationContext, "Image Uploaded..", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(applicationContext, "Fail to Upload Image..", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
