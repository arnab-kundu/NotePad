package com.arnab.notepad.models.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.arnab.notepad.models.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM NOTE ORDER BY lastseen DESC")
    fun getAllNotes(): List<Note>

    @Insert
    fun addNewNote(note: Note)

    @Query("DELETE FROM NOTE")
    fun deleteAll()

    @Query("DELETE FROM NOTE WHERE id = :ID")
    fun deleteSelectedNoteId(ID: Long)

    @Query("SELECT * FROM NOTE WHERE id = :ID")
    fun selectNote(ID: Long): Note

    //@Query("UPDATE NOTE SET title = 'asd',content = 'asd' WHERE id = (note.id)")
    @Update
    fun updateNote(note: Note): Int
}