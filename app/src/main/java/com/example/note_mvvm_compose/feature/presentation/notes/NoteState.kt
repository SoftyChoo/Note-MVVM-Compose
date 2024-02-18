package com.example.note_mvvm_compose.feature.presentation.notes

import com.example.note_mvvm_compose.feature.domain.model.Note
import com.example.note_mvvm_compose.feature.domain.util.NoteOrder
import com.example.note_mvvm_compose.feature.domain.util.OrderType

// viewModel 안에서 다뤄줄 ViewModel의 생명주기를 갖는 data class
// viewModel에서 직접 사용할 데이터

data class NoteState(
    val notes: List<Note> = emptyList(), // 노트 리스트
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending), // 노트의 정렬 상태
    val isOrderSectionVisible: Boolean = false // 상단 정렬 섹션 visible 상태
)