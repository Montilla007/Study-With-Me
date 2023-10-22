package com.example.loginsignupswm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginsignupswm.databinding.ActivityInboxBinding

class InboxActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInboxBinding
    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var userAdapter: UserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInboxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)
        userAdapter = UserAdapter(databaseHelper.getAllUser(), this)

        binding.rvUserlist.layoutManager = LinearLayoutManager(this)
        binding.rvUserlist.adapter = userAdapter
    }
    override fun onResume() {
        super.onResume()
        userAdapter.refreshData(databaseHelper.getAllUser())
    }
}