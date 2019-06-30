package com.arnab.notepad.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arnab.notepad.models.Note
import com.arnab.notepad.models.dao.NoteDao

@Database(entities = [Note::class], version = 1,exportSchema = false)
public abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                // Create database here
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "Note_database"
                )/*.allowMainThreadQueries()*/.build()
                INSTANCE = instance
                return instance
            }
        }
    }
}