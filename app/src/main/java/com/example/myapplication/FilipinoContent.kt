package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class FilipinoContent : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filipino_content)

        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        val back = findViewById<ImageView>(R.id.ivBack)
        back.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        // Set a listener for BottomNavigationView item clicks
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.overview -> {
                    // Replace the fragment container with the OverviewFragment
                    replaceFragment(FilipinoOverviewFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.lessons->{
                    replaceFragment(ChapterFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.similarsubjects -> {
                    replaceFragment(OtherSubjectFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    // Handle unhandled items, e.g., show a message or do nothing
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }

        // Load the initial fragment (OverviewFragment)
        replaceFragment(FilipinoOverviewFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
