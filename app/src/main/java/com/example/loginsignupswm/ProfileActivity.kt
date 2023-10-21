package com.example.loginsignupswm

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.loginsignupswm.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var imageView: ImageView
    private lateinit var btPickImage: Button

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        imageView = findViewById(R.id.image_view)
        btPickImage = findViewById(R.id.pick_image)

        val username = intent.getStringExtra("username")

        // Create an instance of your DatabaseHelper
        val databaseHelper = DatabaseHelper(this)

        // Fetch and set user data if available
        val db = databaseHelper.readableDatabase
        val selection = "${DatabaseHelper.COLUMN_USERNAME} = ?"
        val selectionArgs = arrayOf(username)
        val cursor = db.query(DatabaseHelper.TABLE_NAME, null, selection, selectionArgs, null, null, null)

        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FULLNAME))
            val email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL))

            // Set the name, username, and email in TextViews
            binding.profilename.text = name
            binding.profileusername.text = username
            binding.profileemail.text = email
        }

        cursor.close()

        btPickImage.setOnClickListener {
            pickImage()
        }



        binding.editprofilebtn.setOnClickListener {
            startActivityForResult(Intent(this, EditProfile::class.java), 123)
        }

    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            val uri = data.data
            if (uri != null) {
                imageView.setImageURI(uri)
            }
        } else if (requestCode == 123 && resultCode == RESULT_OK && data != null) {
            val name = data.getStringExtra("name")
            val username = data.getStringExtra("username")
            val email = data.getStringExtra("email")


            binding.profilename.text = name
            binding.profileusername.text = username
            binding.profileemail.text = email
        }
    }
}


