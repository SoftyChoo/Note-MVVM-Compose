package com.example.note_mvvm_compose.feature.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note_mvvm_compose.feature.domain.model.Note
import com.example.note_mvvm_compose.feature.domain.usecase.NoteUseCases
import com.example.note_mvvm_compose.feature.domain.util.NoteOrder
import com.example.note_mvvm_compose.feature.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(NoteState())
    val state: State<NoteState> get() = _state // 읽기 전용

    private var recentlyDeleteNote: Note? = null // 복구용

    private var getNotesJob : Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {
                if (state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType){
                    return
                }
                getNotes(event.noteOrder)
            }

            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNoteUseCase(event.note)
                    recentlyDeleteNote = event.note
                }
            }

            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNoteUseCase(
                        recentlyDeleteNote ?: return@launch
                    ) // 널이 아닐 경우에만 복원, null이면 코루틴 종료
                    recentlyDeleteNote = null
                }
            }

            is NotesEvent.ToggleOrderSection -> {
                _state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder){
        // 정렬을 기반으로 목록을 가져와서 상태 업데이트

        // getNotes함수를 호출할 때 마다 이전 코루틴을 취소
        // -> 코루틴 중복 실행 및 메모리 누수를 방지
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotesUseCase(noteOrder)
            .map { notes ->
                NoteState(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }.onEach { newNoteState ->
                _state.value = newNoteState
            }.launchIn(viewModelScope)
            // launchIn(viewModelScope)
            // 뷰 모델의 수명 주기와 일치시킴
            // 뷰 모델이 파괴될 때 해당 코루틴이 취소되도록 하여 메모리 누수나 예기치 않은 동작을 방지한다고 함
    }
}