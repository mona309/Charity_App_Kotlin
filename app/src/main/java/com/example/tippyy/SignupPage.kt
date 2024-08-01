package com.example.tippyy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tippyy.databinding.ActivityLoginPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference

class SignupPage : AppCompatActivity() {
    private lateinit var uname: EditText
    private lateinit var pword: EditText
    private lateinit var pword1: EditText
    private lateinit var lbutton: Button
    private lateinit var signupb: TextView
    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        uname=findViewById(R.id.username)
        pword=findViewById(R.id.password)
        pword1=findViewById(R.id.password1)
        lbutton=findViewById(R.id.loginbutton)
        signupb=findViewById(R.id.sig)
        binding=ActivityLoginPageBinding.inflate(layoutInflater)
        firebaseAuth=FirebaseAuth.getInstance()
        signupb.setOnClickListener{
            startActivity(Intent(this,LoginPage::class.java))
        }

        lbutton.setOnClickListener{
            val u = uname.text.toString()
            val p = pword.text.toString()
            val cp=pword1.text.toString()
            Log.i("Test Credentials","Username : $u and password : $p && $cp")

            if (u.isNotEmpty()&&p.isNotEmpty()&&cp.isNotEmpty()){
                if (p==cp){
                    firebaseAuth.createUserWithEmailAndPassword(u,p).addOnCompleteListener{
                        if (it.isSuccessful){

                            startActivity(Intent(this,LoginPage::class.java))
                        }else{
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this,"Password doesn't match",Toast.LENGTH_SHORT).show()
                }
            }else{

                Toast.makeText(this,"Error : Fields empty",Toast.LENGTH_SHORT).show()
            }
        }
    }
}