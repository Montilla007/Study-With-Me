package com.example.noted

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.noted.databinding.ActivityNoteAddBinding
import java.text.SimpleDateFormat
import java.util.Date

class NoteAdd : AppCompatActivity() {

    private  lateinit var binding: ActivityNoteAddBinding
    private lateinit var db: NoteDatabaseHelper

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)

        val autoKeyboard = binding.etNoteTitle
        autoKeyboard.requestFocus()

        Handler().postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(autoKeyboard, InputMethodManager.SHOW_IMPLICIT)
            Log.i("test", "working")
        }, 200)

        binding.btnNoteReturn.setOnClickListener{
            Log.i("test", "Add note disabled!")
            val intent = Intent(this, NoteList::class.java)
            startActivity(intent)
        }

        binding.btnNoteSave.setOnClickListener {
            Log.i("test", "Save note saved")
            val currentDate = Date()
            val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm:ss")
            val timestamp = dateFormat.format(currentDate)

            val title = binding.etNoteTitle.text.toString()
            val content = binding.etNoteDescription.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                Log.i("test", "Note data saved!")
                val note = Note(0, title, content, timestamp)
                db.insertNote(note)
                finish()
                Toast.makeText(this, "Note Saved", Toast.LENGTH_LONG).show()
            }
            else if (title.isEmpty() || content.isEmpty()){
                Log.i("test", "Insufficient data")
                Toast.makeText(this, "Empty Title or Description", Toast.LENGTH_SHORT).show()
            }
        }
    }
}