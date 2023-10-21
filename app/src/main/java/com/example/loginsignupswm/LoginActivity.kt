package com.example.loginsignupswm

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.loginsignupswm.databinding.ActivityLoginBinding
import com.example.loginsignupswm.databinding.ActivityMainBinding
import com.jakewharton.rxbinding2.widget.RxTextView

@SuppressLint("CheckResult")
class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        databaseHelper = DatabaseHelper(this)

        binding.btnLogin.setOnClickListener {
            val etEmail = binding.etEmail.text.toString()
            val etPassword = binding.etPassword.text.toString()
            loginDatabase(etEmail, etPassword)
            Log.i("Test", "HomeLogin")
        }


//  Username Validation

        val usernameStream = RxTextView.textChanges(binding.etEmail)
            .skipInitialValue()
            .map { username ->
                username.isEmpty()
            }
        usernameStream.subscribe {
            showTextMinimalAlert(it, "Email/Username")
        }

//  Password Validation

        val passwordStream = RxTextView.textChanges(binding.etPassword)
            .skipInitialValue()
            .map { username ->
                username.isEmpty()
            }
        usernameStream.subscribe {
            showTextMinimalAlert(it, "Password")
        }

//  Button Enable True or False

        val invalidFieldStream = io.reactivex.Observable.combineLatest(
            usernameStream,
            passwordStream,
            { usernameInvalid: Boolean, passwordInvalid: Boolean ->
                !usernameInvalid && !passwordInvalid
            })
        invalidFieldStream.subscribe { isValid ->
            if (isValid) {
                binding.btnLogin.isEnabled = true
                binding.btnLogin.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.enabled_button_color)
            } else {
                binding.btnLogin.isEnabled = false
                binding.btnLogin.backgroundTintList =
                    ContextCompat.getColorStateList(this, android.R.color.darker_gray)
            }
        }




        binding.tvHaventAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginDatabase(username: String, password: String) {
        val userExists = databaseHelper.readUser(username, password)
        if (userExists) {
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ProfileActivity::class.java)

            // Pass the 'username' to ProfileActivity using an intent extra
            intent.putExtra("username", username)

            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
        }
    }



    private fun showTextMinimalAlert(isNotValid: Boolean, text: String) {
        if (text == "Email/Username")
            binding.etEmail.error = if (isNotValid) "$text Cannot be empty!" else null
        else if (text == "Password")
            binding.etPassword.error = if (isNotValid) "$text Cannot be empty!" else null
    }
}