package com.example.note_mvvm_compose.feature.domain.usecase

import com.example.note_mvvm_compose.feature.domain.model.Note
import com.example.note_mvvm_compose.feature.domain.repository.NoteRepository
import com.example.note_mvvm_compose.feature.domain.util.NoteOrder
import com.example.note_mvvm_compose.feature.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotesUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending) // 디폴트값 내림차순
    ): Flow<List<Note>> = repository.getNotes().map { notes ->
        when (noteOrder.orderType) { // 우선적으로 오름/내림차순 판별 후 날짜/시간/색 별 정렬
            is OrderType.Ascending -> {
                when (noteOrder) {
                    is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
                    is NoteOrder.Date -> notes.sortedBy { it.timestamp }
                    is NoteOrder.Color -> notes.sortedBy { it.color }
                }
            }

            is OrderType.Descending -> {
                when (noteOrder) {
                    is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                    is NoteOrder.Date -> notes.sortedByDescending { it.timestamp }
                    is NoteOrder.Color -> notes.sortedByDescending { it.color }
                }

            }
        }
    }

}