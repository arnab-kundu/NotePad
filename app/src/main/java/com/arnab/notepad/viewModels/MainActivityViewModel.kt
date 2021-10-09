package com.arnab.notepad.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.arnab.notepad.db.NoteRepository
import com.arnab.notepad.models.Note

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    val note = Note(id = 0, title = "", content = "", lastSeen = 0)
    val note1 = Note(0, "", "", 0)


    val notes: LiveData<List<Note>>? = null
    val repository: NoteRepository? = null

}