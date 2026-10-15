package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutUtama)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etNama = findViewById<EditText>(R.id.etNama)
        val btnSapa = findViewById<Button>(R.id.btnSapa)

        btnSapa.setOnClickListener {
            val nama = etNama.text.toString()
            if (nama.isNotEmpty()) {
                val greetings = listOf(
                    "semangat ko, kasih keras!",
                    "jangan menyerah, dikit lagi jago!",
                    "mantap kali kodingannya!",
                    "fokus terus, pasti bisa!",
                    "keren banget progresmu hari ini!"
                )
                val randomGreeting = greetings.random()
                Toast.makeText(this, "Halo $nama, $randomGreeting", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Silakan masukkan nama terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }
}