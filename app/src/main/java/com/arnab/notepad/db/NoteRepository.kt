package com.arnab.notepad.db

import androidx.annotation.WorkerThread
import com.arnab.notepad.models.Note
import com.arnab.notepad.models.dao.NoteDao

class NoteRepository(private val noteDao: NoteDao) {

    //val allWords: LiveData<List<Word>> = wordDao.getAllWords()

    @WorkerThread
    fun insert(note: Note) {
        noteDao.addNewNote(note)
    }

    @WorkerThread
    fun getAllNotes(): List<Note> {
        return noteDao.getAllNotes()
    }

    @WorkerThread
    fun deleteSelectedNote(id: Long) {
        return noteDao.deleteSelectedNoteId(id)
    }

    @WorkerThread
    fun getSelectedNote(id: Long?): Note {
        return noteDao.selectNote(id!!)
    }

    @WorkerThread
    fun updateNote(note: Note): Int {
        return noteDao.updateNote(note)
    }
}