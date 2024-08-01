package com.example.tippyy

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class VolunteerPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_page)
        var tt1=findViewById<TextView>(R.id.text1)
        tt1.movementMethod=LinkMovementMethod.getInstance()

        var tt2=findViewById<TextView>(R.id.text2)
        tt2.movementMethod=LinkMovementMethod.getInstance()

        var tt3=findViewById<TextView>(R.id.text3)
        tt3.movementMethod=LinkMovementMethod.getInstance()
    }
}

