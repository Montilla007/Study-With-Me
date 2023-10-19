package com.example.noted

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private var notes: List<Note>, context: Context) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val db: NoteDatabaseHelper = NoteDatabaseHelper(context)

    class NoteViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val tvrvtitle: TextView = itemview.findViewById(R.id.tv_rv_title)
        val tvrvdescription: TextView = itemview.findViewById(R.id.tv_rv_description)
        val tvrvtimestamp: TextView = itemview.findViewById(R.id.tv_rv_timestamp)
        val clfragment: ConstraintLayout = itemview.findViewById(R.id.cl_fragment)
        val btnnotedelete: ImageView = itemview.findViewById(R.id.btn_note_delete)
        val cvnotewarning: CardView = itemview.findViewById(R.id.cv_note_warning)
        val cvbtncheck: ImageView = itemview.findViewById(R.id.cv_btn_check)
        val cvbtnclose: ImageView = itemview.findViewById(R.id.cv_btn_close)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_fragment, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]

        holder.tvrvtitle.text = note.title
        holder.tvrvdescription.text = note.content
        holder.tvrvtimestamp.text = note.timestamp

        holder.clfragment.setOnClickListener {
            holder.cvnotewarning.visibility = View.INVISIBLE
            val intent = Intent(holder.itemView.context, NoteEdit::class.java).apply {
                putExtra("note_id", note.id)
            }
            holder.itemView.context.startActivity(intent)
        }
        holder.btnnotedelete.setOnClickListener {
            holder.cvnotewarning.visibility = View.VISIBLE
        }
        holder.cvbtncheck.setOnClickListener {
            db.deleteNote(note.id)
            refreshData(db.getAllNotes())
            Toast.makeText(holder.itemView.context, "Note Deleted", Toast.LENGTH_SHORT).show()
            holder.cvnotewarning.visibility = View.INVISIBLE
        }
        holder.cvbtnclose.setOnClickListener {
            holder.cvnotewarning.visibility = View.INVISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newNotes: List<Note>){
        notes = newNotes
        notifyDataSetChanged()
    }
}