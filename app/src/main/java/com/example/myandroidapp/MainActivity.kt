package com.example.myandroidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myandroidapp.ui.insertupdate.InsertTodoActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fab_add.setOnClickListener {
            val intent = Intent(this, InsertTodoActivity::class.java)
            startActivity(intent)
        }
    }
}
