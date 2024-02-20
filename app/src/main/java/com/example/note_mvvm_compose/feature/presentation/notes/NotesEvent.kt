package com.example.note_mvvm_compose.feature.presentation.notes

import com.example.note_mvvm_compose.feature.domain.model.Note
import com.example.note_mvvm_compose.feature.domain.util.NoteOrder

// 사용자가 동작시킬 이벤트를 모아놓는 SealedClass
sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder) : NotesEvent()
    data class DeleteNote(val note: Note) : NotesEvent()
    object RestoreNote: NotesEvent() // 복원
    object ToggleOrderSection: NotesEvent() // 정렬 섹션 토글
}