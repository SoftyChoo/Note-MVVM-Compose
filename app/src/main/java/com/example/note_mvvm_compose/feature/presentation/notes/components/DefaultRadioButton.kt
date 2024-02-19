package com.example.note_mvvm_compose.feature.presentation.notes.components
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun DefaultRadioButton(
    text: String,
    checked: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row( // 가로 배치
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = checked,
            onClick = onSelect, // onSelect 콜백을
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onBackground
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodySmall )
    }
}

//@Preview(name = "DefaultRadioButton")
//@Composable
//fun PreviewDefaultRadioButton() {
//    val buttonState = rememberSaveable { true }
//    DefaultRadioButton(text = "testText", checked = buttonState, onSelect = { /*TODO*/ })
//}