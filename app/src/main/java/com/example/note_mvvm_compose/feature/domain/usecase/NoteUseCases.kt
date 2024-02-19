package com.example.note_mvvm_compose.feature.domain.usecase

// ViewModel 에 UseCase 를 주입해 줄 클래스
class NoteUseCases(
    val getNotesUseCase: GetNotesUseCase,
    val deleteNoteUseCase: DeleteNoteUseCase,
    val addNoteUseCase: AddNoteUseCase,
    val getNoteUseCase: GetNoteUseCase
)