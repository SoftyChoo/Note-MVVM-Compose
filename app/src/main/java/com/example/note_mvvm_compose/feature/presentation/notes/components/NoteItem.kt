package com.example.note_mvvm_compose.feature.presentation.notes.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.example.note_mvvm_compose.feature.domain.model.Note

@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    cutCornerSize: Dp = 30.dp, // 노트 접힌 모양을 위한 절단된 모서리 사이즈
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Canvas(
            // Canvas 에선 고정된 사이즈가 필요하다
            // 만약 상위 Box에 Modifier가 없다고 가정했을 떄 fillMaxSize는 상위 부모에 영향을 미치기 떄문에 좋지 않다.
            // matchParentSize는 부모가 제약조건을 측정한 후 캔버스에 크기를 제공하므로 Canvas는 정상적인 크기를 할당받아 정상 작동한다.
            // + 여러 화면을 지원하기 위해 크기를 하드코딩 하지 않고 matchParentSize을 사용한다.
            modifier = Modifier.matchParentSize()
        ) {
            // ClipPath를 사용하여 절단된 모서리를 만들기
            val clipPath = Path().apply {
                lineTo(size.width - cutCornerSize.toPx(), 0f) // 잘린 모서리 시작 지점 : 오른쪽 모서리를 잘라주기 위해 cutCornerSize만큼 빼줌
                lineTo(size.width, cutCornerSize.toPx()) // 잘린 모서리 끝 지점
                lineTo(size.width, size.height) // 우측 하단 지점
                lineTo(0f, size.height) // 왼쪽 하단 지점
                close()  // 시작점 & 끝점 연결
            }
            clipPath(clipPath) {
                drawRoundRect( // 노트 배경 그리기
                    color = Color(note.color),
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
                drawRoundRect( // 노트의 접힌 부분을 칠하기
                    color = Color( // 어두운 색으로 blend
                        ColorUtils.blendARGB(note.color, 0x000000, 0.2f)
                    ),
                    topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
                    size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(end = 32.dp)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Note",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

//@Preview(name = "PreviewNoteItem")
//@Composable
//fun PreviewNoteItem() {
//    val note = Note(
//        title = "Sample Note Title",
//        content = "This is a sample note content.",
//        timestamp = System.currentTimeMillis(), // 현재 시간
//        color = 0x111111,
//        id = 1
//    )
//
//    NoteItem(
//        note = note,
//        onDeleteClick = { }
//    )
//}
