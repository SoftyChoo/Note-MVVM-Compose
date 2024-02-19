@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.note_mvvm_compose.feature.presentation.add_edit_note

import android.annotation.SuppressLint
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.note_mvvm_compose.feature.domain.model.Note
import com.example.note_mvvm_compose.feature.presentation.add_edit_note.components.TransparentHintTextField
import com.example.note_mvvm_compose.feature.presentation.notes.NotesScreen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value
    val snackbarHostState = remember { SnackbarHostState() }

    // 노트 배경 색상 애니메이션을 적용하기 위한 Animatable 초기화
    val noteBackgroundAnimatable = remember {
        Animatable(
            Color( // 기존 노트일 경우 받아온 Note Color, 새로운 노트일 경우 viewModel을 통해 랜텀으로 출력
                if (noteColor != -1) noteColor
                else viewModel.noteColor.value
            )
        )
    }

    val scope = rememberCoroutineScope()

    // LaunchedEffect : 컴포지션이 일어날 때 suspend fun을 실행시켜주는 Composable
    // 매번 리컴포지션이 일어날 때 마다 LaunchedEffect가 재수행 된다면 비효율적이기 때문에
    // LaunchedEffect는 기준값(key)을 두어 key가 바뀔 때만 LaunchedEffect의 suspend fun을 취소하고 재실행한다.
    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is AddEditNoteViewModel.UiEvent.ShowSnackBar -> { // 스낵바
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditNoteViewModel.UiEvent.SaveNote -> { // 저장일 경우 저장 후 이전 화면으로 이동
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditNoteEvent.SaveNote)
                },
                containerColor = MaterialTheme.colorScheme.primary // background color
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save Note")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) } // migration : https://developer.android.com/jetpack/compose/designsystems/material2-material3?hl=ko
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 지정한 모든 색상 반복
                Note.noteColors.forEach { color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(50.dp) // 사각형의 크기를 지정합니다.
                            .shadow(
                                elevation = 15.dp,
                                shape = CircleShape
                            )
                            .clip(CircleShape)
                            .background(color) // 배경을 각각의 색상으로 설정
                            .border(
                                width = 3.dp,
                                color =
                                // 같은 색상일 경우 검정, 아닐경우 투명 테두리
                                if (viewModel.noteColor.value == colorInt) Color.Black
                                else Color.Transparent,
                                shape = CircleShape // 테두리 모양
                            )
                            .clickable {
                                scope.launch {
                                    // 노트 배경 색상에 애니메이션을 적용하여 변경
                                    noteBackgroundAnimatable.animateTo(
                                        targetValue = Color(colorInt), // 목표 색상을 설정
                                        animationSpec = tween(
                                            durationMillis = 500 // 애니메이션 지속 시간을 설정
                                        )
                                    )
                                }
                                // ViewModel 노트 색상 변경 이벤트 전달
                                viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField( // title
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                },
                onFocusChange = { // viewModel의 hint visibility 바꿔주기
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible, // 바꿔진 값을 가져와서 띄운다.
                singleLine = true,
                textStyle = MaterialTheme.typography.titleMedium

            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField( // content
                modifier = Modifier.fillMaxHeight(),
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                },
                onFocusChange = { // viewModel의 hint visibility 바꿔주기
                    viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                },
                isHintVisible = contentState.isHintVisible, // 바꿔진 값을 가져와서 띄운다.
                singleLine = true,
                textStyle = MaterialTheme.typography.bodySmall
            )
        }
    }
}

//@Preview(name = "PreviewAddEditNoteScreen")
//@Composable
//fun PreviewAddEditNoteScreen() {
//    AddEditNoteScreen(
//        NavController(LocalContext.current),
//        0x000000,
//        hiltViewModel()
//    )
//}