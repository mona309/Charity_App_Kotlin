@file:Suppress("DEPRECATION")

package com.example.tippyy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tippyy.databinding.ActivityHomePageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference


class HomePage : AppCompatActivity() {
    private lateinit var bottomnav: BottomNavigationView
    private lateinit var binding: ActivityHomePageBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val unameR=intent.getStringExtra("Username")

        firebaseAuth=FirebaseAuth.getInstance()
        databaseReference= FirebaseDatabase.getInstance().getReference("Users")
        val uid=firebaseAuth.currentUser?.uid
        if (uid != null) {
            databaseReference.child(uid).child("name").setValue(unameR)
            databaseReference.child(uid).child("status").setValue("Donator")
        }
        bottomnav=findViewById(R.id.bottnav)
        bottomnav.setOnItemSelectedListener{
            when(it.itemId){
                R.id.profile->{
                    val logger= Intent(this,ProfilePage::class.java).putExtra("Username",unameR)
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
                R.id.home->{
                    true
                }
                else -> {false}
            }

        }
        findViewById<CardView>(R.id.clothes).setOnClickListener {
            startActivity(Intent(this,ClothesPage::class.java).putExtra("Username",unameR))

        }
        findViewById<CardView>(R.id.books).setOnClickListener {
            startActivity(Intent(this,BooksPage::class.java).putExtra("Username",unameR))

        }
        findViewById<CardView>(R.id.food).setOnClickListener {
            startActivity(Intent(this,FoodPage::class.java).putExtra("Username",unameR))

        }
        findViewById<CardView>(R.id.other).setOnClickListener {
            startActivity(Intent(this,OtherPage::class.java).putExtra("Username",unameR))

        }
        findViewById<CardView>(R.id.volunteer).setOnClickListener {
            startActivity(Intent(this,VolunteerPage::class.java).putExtra("Username",unameR))

        }
        findViewById<CardView>(R.id.money).setOnClickListener {
            startActivity(Intent(this,MoneyPage::class.java).putExtra("Username",unameR))

        }
    }
}
/*
         when (menuItem.itemId) {
            R.id.homeItem -> {
               supportFragmentManager.beginTransaction()
                  .replace(R.id.fragmentContainer, HomeFragment())
                  .commit()
               true
            }
            R.id.profileItem -> {
               supportFragmentManager.beginTransaction()
                  .replace(R.id.fragmentContainer,
ProfileFragment())
                  .commit()
               true
            }
            else -> false
         }
 */
