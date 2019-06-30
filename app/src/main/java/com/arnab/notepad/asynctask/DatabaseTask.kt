package com.arnab.notepad.asynctask

import android.os.AsyncTask
import com.arnab.notepad.NotepadApplication
import com.arnab.notepad.db.NoteDatabase
import com.arnab.notepad.db.NoteRepository
import com.arnab.notepad.models.Note

class DatabaseTask : AsyncTask<Note, Void, Void>() {


    override fun doInBackground(vararg params: Note?): Void? {

        val noteDao = NoteDatabase.getDatabase(NotepadApplication()).noteDao()
        val repository = NoteRepository(noteDao)
        params[0]?.let { repository.insert(it) }
        return null
    }
}