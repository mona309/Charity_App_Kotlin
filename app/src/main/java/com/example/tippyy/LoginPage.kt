package com.example.tippyy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import com.example.tippyy.databinding.ActivityLoginPageBinding
import com.google.firebase.auth.FirebaseAuth


class LoginPage : AppCompatActivity() {
    private lateinit var uname: EditText
    private lateinit var pword:EditText
    private lateinit var lbutton: Button
    private lateinit var signupb: TextView
    private lateinit var binding:ActivityLoginPageBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        uname=findViewById(R.id.username)
        pword=findViewById(R.id.password)
        lbutton=findViewById(R.id.loginbutton)
        signupb=findViewById(R.id.sig)
        binding=ActivityLoginPageBinding.inflate(layoutInflater)
        firebaseAuth=FirebaseAuth.getInstance()

        signupb.setOnClickListener{
            startActivity(Intent(this,SignupPage::class.java))
        }

        lbutton.setOnClickListener{
            val u = uname.text.toString()
            val p = pword.text.toString()

            Log.i("Test Credentials","Username : $u and password : $p")
            val logger= Intent(this,HomePage::class.java)

            if (u.isNotEmpty()&&p.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(u,p).addOnCompleteListener{
                    if(it.isSuccessful){
                        logger.putExtra("Username",(uname.text.toString()))
                        startActivity(logger)
                    }else{
                        val builder = AlertDialog.Builder(this@LoginPage)
                        builder.setTitle("Invalid Login")
                        builder.setMessage("Check username / password")
                        builder.setCancelable(false)
                        builder.setPositiveButton("ok") { _,_->
                            Toast.makeText(this@LoginPage,"clicked ok", Toast.LENGTH_LONG).show()
                        }
                        val bx=builder.create()
                        bx.show()
                        Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                Toast.makeText(this,"Error : Fields empty",Toast.LENGTH_SHORT).show()
            }
        }

    }
}