package com.example.tippyy

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class StoryPage : AppCompatActivity() {
    private lateinit var bottomnav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_story_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val unameR=intent.getStringExtra("Username")
        Toast.makeText( this@StoryPage,"$unameR", Toast.LENGTH_SHORT).show()
        bottomnav=findViewById(R.id.bottnav)
        bottomnav.setOnItemSelectedListener{
            when(it.itemId){
                R.id.profile->{
                    val logger= Intent(this,ProfilePage::class.java).putExtra("Username",unameR)
                    startActivity(logger)
                    overridePendingTransition(0, 0)
                    true

                }
                R.id.home->{
                    val logger= Intent(this,HomePage::class.java).putExtra("Username",unameR)
                    startActivity(logger)
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.ourstories->{
                    true
                }
                else -> {false}
            }

        }
    }
}