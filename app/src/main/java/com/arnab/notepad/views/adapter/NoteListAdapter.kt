package com.arnab.notepad.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arnab.notepad.R
import com.arnab.notepad.models.Note
import kotlinx.android.synthetic.main.row_note.view.*

class NoteListAdapter(val notes: ArrayList<Note>) : RecyclerView.Adapter<NoteListAdapter.NoteViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note: Note = notes[position]
        holder.tvSubject.text = note.title
        holder.tvDescription.text = note.content

    }


    inner class NoteViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var tvSubject: TextView
        lateinit var tvDescription: TextView

        init {
            tvSubject = itemView.tv_subject
            tvDescription = itemView.tv_description
        }
    }


    fun restoreItem(item: Note, position: Int) {
        //notes.add(position, item);
        notifyItemInserted(position);
    }

    fun removeItem(position: Int) {
        notes.drop(position)
        notifyItemRemoved(position)
    }

    fun getData(): ArrayList<Note> {
        return notes
    }
}
