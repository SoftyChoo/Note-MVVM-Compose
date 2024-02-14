package com.example.note_mvvm_compose.feature.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.note_mvvm_compose.feature.domain.model.Note

// Room DB
@Database(
    entities = [Note::class],
    version = 1
)
abstract class NoteDataBase : RoomDatabase() {

    abstract val noteDao: NoteDao
}