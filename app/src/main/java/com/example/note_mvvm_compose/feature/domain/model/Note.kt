package com.example.note_mvvm_compose.feature.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.note_mvvm_compose.theme.BabyBlue
import com.example.note_mvvm_compose.theme.LightGreen
import com.example.note_mvvm_compose.theme.RedOrange
import com.example.note_mvvm_compose.theme.RedPink
import com.example.note_mvvm_compose.theme.Violet

// note 정보를 다룰 data Class
@Entity
data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    @PrimaryKey val id: Int? = null
) {
    companion object {
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

class InvalidNoteException(message:String) : Exception(message)
