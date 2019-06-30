package com.arnab.notepad.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) @ColumnInfo val id: Long,
    @ColumnInfo val title: String,
    @ColumnInfo val content: String,
    @ColumnInfo val lastSeen: Long
)