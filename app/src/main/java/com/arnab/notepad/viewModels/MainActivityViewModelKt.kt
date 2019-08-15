package com.arnab.notepad.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.arnab.notepad.db.NoteRepository
import com.arnab.notepad.models.Note

class MainActivityViewModelKt : ViewModel() {

    val note = Note(id = 0, title = "", content = "", lastSeen = 0)
    val note1 = Note(0, "", "", 0)


    val notes: LiveData<List<Note>>? = null
    val repository: NoteRepository? = null

}