package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.fragments.AkunFragment
import com.example.myapplication.fragments.AktivitasFragment
import com.example.myapplication.fragments.MutasiFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class TaskMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_menu)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Load default fragment (Mutasi)
        if (savedInstanceState == null) {
            loadFragment(MutasiFragment())
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_mutasi -> {
                    loadFragment(MutasiFragment())
                    true
                }
                R.id.menu_aktivitas -> {
                    loadFragment(AktivitasFragment())
                    true
                }
                R.id.menu_akun -> {
                    loadFragment(AkunFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
