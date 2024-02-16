package com.example.note_mvvm_compose.feature.domain.usecase


// ViewModel 에 UseCase 를 주입해 줄 클래스
class NoteUseCases(
    val getNoteUseCase: GetNoteUseCase,
    val deleteNoteUseCase: DeleteNoteUseCase,
    val addNoteUseCase: AddNoteUseCase
) {
}