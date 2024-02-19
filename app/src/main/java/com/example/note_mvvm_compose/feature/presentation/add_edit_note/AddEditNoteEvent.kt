package com.example.note_mvvm_compose.feature.presentation.add_edit_note

import androidx.compose.ui.focus.FocusState

/*
FocusState interface
Inactive: 포커스를 가지고 있지 않은 비활성 상태
Active: 포커스를 가지고 있는 활성 상태
Focused: 포커스를 가지고 있고 사용자가 해당 요소에 입력을 할 수 있는 상태
 */
sealed class AddEditNoteEvent {
    data class EnteredTitle(val value: String) : AddEditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditNoteEvent()

    data class EnteredContent(val value: String) : AddEditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState) : AddEditNoteEvent()

    data class ChangeColor(val color: Int) : AddEditNoteEvent()
    object SaveNote: AddEditNoteEvent()

}