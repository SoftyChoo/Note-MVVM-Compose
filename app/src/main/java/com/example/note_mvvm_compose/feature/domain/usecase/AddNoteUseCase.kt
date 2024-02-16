package com.example.note_mvvm_compose.feature.domain.usecase

import com.example.note_mvvm_compose.feature.domain.model.InvalidNoteException
import com.example.note_mvvm_compose.feature.domain.model.Note
import com.example.note_mvvm_compose.feature.domain.repository.NoteRepository
import kotlin.jvm.Throws

class AddNoteUseCase(
    private val repository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) { // isEmpty와는 다르게 공백 문자열만 있을때도 true
            throw InvalidNoteException("노트의 타이틀을 반드시 입력해주세요!")
        }
        if (note.content.isBlank()) {
            throw InvalidNoteException("노트의 내용을 반드시 입력해주세요!")
        }
        repository.insertNote(note)
    }
}