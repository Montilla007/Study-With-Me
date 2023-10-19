package com.example.noted

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.noted.databinding.ActivityNoteEditBinding

class NoteEdit : AppCompatActivity() {

    private lateinit var binding: ActivityNoteEditBinding
    private lateinit var db: NoteDatabaseHelper
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)

        val autoKeyboard = binding.etNoteUpTitle
        autoKeyboard.requestFocus()

        Handler().postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(autoKeyboard, InputMethodManager.SHOW_IMPLICIT)
        }, 200)

        noteId = intent.getIntExtra("note_id", -1)
        if (noteId == -1) {
            finish()
            return
        }

        val note = db.getNoteByID(noteId)
        binding.etNoteUpTitle.setText(note.title)
        binding.etNoteUpDescription.setText(note.content)

        binding.btnNoteUpReturn.setOnClickListener{
            Log.i("test", "Edit mode disabled!")
            val intent = Intent(this, NoteList::class.java)
            startActivity(intent)
        }

        binding.btnNoteUpSave.setOnClickListener{
            Log.i("test", "Edit note saved")
            val newTitle = binding.etNoteUpTitle.text.toString()
            val newContent = binding.etNoteUpDescription.text.toString()
            val newTimestamp = note.timestamp
            val newNote = Note(noteId, newTitle, newContent, newTimestamp)
            db.editNote(newNote)
            finish()
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_LONG).show()
        }
    }
}