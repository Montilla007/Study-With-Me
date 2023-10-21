package com.example.loginsignupswm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import android.util.Patterns
import com.example.loginsignupswm.databinding.ActivityEditProfileBinding

class EditProfile : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backbutton2.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.scale_up)
            binding.backbutton2.startAnimation(animation)
            finish()
        }


        binding.savebtn.setOnClickListener {
            val name = binding.profilenameedit.text.toString()
            val oldUsername = binding.profileusernamedit.text.toString()
            val newUsername = binding.newUsernameEditText.text.toString()
            val email = binding.profileemailedit.text.toString()

            // Check if the username is empty or invalid
            val isNameEmpty = name.isEmpty()
            val isUsernameValid = isUsernameValid(newUsername)
            val isEmailValid = isEmailValid(email)

            showNameEmptyAlert(isNameEmpty)
            showUsernameLengthAlert(isUsernameValid)
            showEmailFormatAlert(isEmailValid)

            if (!isNameEmpty && isUsernameValid && isEmailValid) {
                val databaseHelper = DatabaseHelper(this)
                val usernameUpdated = databaseHelper.updateUsername(oldUsername, newUsername)

                if (usernameUpdated) {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent()
                    intent.putExtra("name", name)
                    intent.putExtra("username", newUsername)
                    intent.putExtra("email", email)
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    Toast.makeText(this, "Failed to update profile. The new username might be already taken.", Toast.LENGTH_SHORT).show()
                }
            }
        }




    }
    private fun showNameEmptyAlert(isEmpty: Boolean) {
        if (isEmpty) {
            binding.profilenameedit.error = "Name cannot be empty!"
        } else {
            binding.profilenameedit.error = null
        }
    }

    private fun showUsernameLengthAlert(isValid: Boolean) {
        if (!isValid) {
            binding.profileusernamedit.error = "Username must be at least 8 characters long."
        } else {
            binding.profileusernamedit.error = null
        }
    }

    private fun showEmailFormatAlert(isValid: Boolean) {
        if (!isValid) {
            binding.profileemailedit.error = "Invalid email format."
        } else {
            binding.profileemailedit.error = null
        }
    }


    private fun isUsernameValid(username: String): Boolean {
        return username.length >= 8
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
