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
    fun openSelectedNote(id: Long) {
        return noteDao.selectNote(id)
    }
}