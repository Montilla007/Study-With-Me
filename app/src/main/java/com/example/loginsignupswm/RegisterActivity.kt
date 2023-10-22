package com.example.loginsignupswm

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Observable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.loginsignupswm.databinding.ActivityMainBinding
import com.example.loginsignupswm.databinding.ActivityRegisterBinding
import com.jakewharton.rxbinding2.widget.RxTextView

@SuppressLint("CheckResult")
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        binding.btnRegister.setOnClickListener {
            val signupUsername = binding.etUsername.text.toString()
            val signupPassword = binding.etPassword.text.toString()
            val fullname = binding.etFullname.text.toString()
            val email = binding.etEmail.text.toString()

            signupDataBase(signupUsername, signupPassword, fullname, email)
            Log.i("Test", "Register")
        }


//  Fullname Validation

        val nameStream = RxTextView.textChanges(binding.etFullname)
            .skipInitialValue()
            .map { name ->
                name.isEmpty()
            }

        nameStream.subscribe{
            showNameExistAlert(it)
            Log.i("Test", "Name")
        }

//  Email Validation

        val emailStream = RxTextView.textChanges(binding.etEmail)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe{
            showEmailValidationAlert(it)
            Log.i("Test", "Email")
        }

//  Username Validation

        val usernameStream = RxTextView.textChanges(binding.etUsername)
            .skipInitialValue()
            .map { username ->
                username.length < 8
            }
        usernameStream.subscribe{
            showTextMinimalAlert(it, "Username")
        }

//  Password Validation

        val passwordStream = RxTextView.textChanges(binding.etPassword)
            .skipInitialValue()
            .map { username ->
                username.length < 8
            }
        usernameStream.subscribe{
            showTextMinimalAlert(it, "Password")
        }

//  Confirm Password Validation
        val passwordConfirmStream = io.reactivex.Observable.merge(
            RxTextView.textChanges(binding.etPassword)
                .skipInitialValue()
                .map { password ->
                    password.toString() != binding.etConfirmPassword.text.toString()
                },
            RxTextView.textChanges(binding.etConfirmPassword)
                .skipInitialValue()
                .map { confirmPassword ->
                    confirmPassword.toString() != binding.etPassword.text.toString()
                })
        passwordConfirmStream.subscribe{
            showPasswordConfirmAlert(it)
        }

//  Button Enable True or False

        val invalidFieldStream = io.reactivex.Observable.combineLatest(
            nameStream,
            emailStream,
            usernameStream,
            passwordStream,
            passwordConfirmStream,
            { nameInvalid: Boolean, emailInvalid: Boolean, usernameInvalid: Boolean, passwordInvalid: Boolean, passwordConfirmInvalid: Boolean ->
                !nameInvalid && !emailInvalid && !usernameInvalid && !passwordInvalid && !passwordConfirmInvalid
            })
        invalidFieldStream.subscribe { isValid ->
            if (isValid) {
                binding.btnRegister.isEnabled = true
                binding.btnRegister.backgroundTintList = ContextCompat.getColorStateList(this, R.color.enabled_button_color)
            } else {
                binding.btnRegister.isEnabled = false
                binding.btnRegister.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.darker_gray)
            }
        }

//  Click
        binding.tvHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun signupDataBase(username: String, password: String, fullname: String, email: String) {
        val databaseHelper = DatabaseHelper(this)

        if (databaseHelper.isUsernameTaken(username)) {
            Toast.makeText(this, "Username already exists.", Toast.LENGTH_SHORT).show()
        } else {
            val insertedRowId = databaseHelper.insertUser(username, password, fullname, email)
            if (insertedRowId != -1L) {
                Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Signup failed. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun showNameExistAlert(isNotValid: Boolean) {
        binding.etFullname.error = if(isNotValid) "Name cannot be empty!" else null
    }

    private fun showTextMinimalAlert(isNotValid: Boolean, text: String){
        if (text == "Username")
            binding.etUsername.error = if (isNotValid) "$text Must be more than 8 letters!" else null
        else if (text == "Password")
            binding.etPassword.error =  if (isNotValid) "$text Must be more than 6 characters" else null
    }

    private fun showEmailValidationAlert(isNotValid: Boolean){
        binding.etEmail.error = if (isNotValid) "Invalid Email!" else null
    }

    private fun showPasswordConfirmAlert(isNotValid: Boolean) {
        binding.etConfirmPassword.error = if (isNotValid) "Password does not match!" else null
    }
}