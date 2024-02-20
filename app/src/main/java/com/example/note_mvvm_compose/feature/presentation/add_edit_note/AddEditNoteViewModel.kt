package com.example.note_mvvm_compose.feature.presentation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note_mvvm_compose.feature.domain.model.InvalidNoteException
import com.example.note_mvvm_compose.feature.domain.model.Note
import com.example.note_mvvm_compose.feature.domain.usecase.GetNoteUseCase
import com.example.note_mvvm_compose.feature.domain.usecase.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _noteTitle = mutableStateOf(NoteTextFieldState(hint = "Enter the title"))
    val noteTitle: State<NoteTextFieldState> get() = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(hint = "Enter the content"))
    val noteContent: State<NoteTextFieldState> get() = _noteContent

    private val _noteColor = mutableStateOf(Note.noteColors.random().toArgb()) // toArgb : int 반환
    val noteColor: State<Int> get() = _noteColor

    private val _eventFlow =
        MutableSharedFlow<UiEvent>() // 일회성 이벤트를 위한 MutableSharedFlow : UI에서 옵저빙 가능, 이벤트에 따라 다른 작업 수행 가능
    val eventFlow: SharedFlow<UiEvent> get() = _eventFlow

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId -> // noteId 가져오기
            if (noteId != -1) { // Id 가 유효할 경우
                viewModelScope.launch {
                    noteUseCases.getNoteUseCase(noteId)?.also { note -> // id에 맞는 노트의 정보를 가져온다.
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _noteColor.value = note.color
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitle.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = _noteContent.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = _noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _noteContent.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color
            }

            AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNoteUseCase(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(), // 현재시간
                                color = noteColor.value,
                                id = currentNoteId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote) // 저장 완료 이벤트를 UI에 발송
                    } catch (e: InvalidNoteException) { // 정상적으로 저장되지 않았을 경우
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
        }
    }


    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SaveNote : UiEvent() // 저장 버튼 클릭
    }
}
