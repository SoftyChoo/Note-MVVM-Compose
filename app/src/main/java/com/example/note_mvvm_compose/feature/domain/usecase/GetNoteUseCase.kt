package com.example.note_mvvm_compose.feature.domain.usecase

import com.example.note_mvvm_compose.feature.domain.model.Note
import com.example.note_mvvm_compose.feature.domain.repository.NoteRepository

class GetNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}