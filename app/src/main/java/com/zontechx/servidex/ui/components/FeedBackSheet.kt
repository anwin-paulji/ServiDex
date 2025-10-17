package com.zontechx.servidex.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zontechx.servidex.R
import com.zontechx.servidex.ui.theme.AppColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedBackSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit = {},
    onFeedBackSubmit: (Int, String) -> Unit = { _, _ -> }
) {

    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(16.dp),
        containerColor = AppColor.White,
        dragHandle = {}
    ) {
        FeedBackSheetContent(onSubmit = { selectedEmoji, thoughts ->
            scope.launch {
                sheetState.hide()
                onFeedBackSubmit(selectedEmoji, thoughts)
            }
        })
    }
}

@Preview
@Composable
fun FeedBackSheetContent(
    modifier: Modifier = Modifier,
    onSubmit: (Int, String) -> Unit = { _, _ -> },
) {

    var selectedLevel by remember { mutableStateOf(0) }
    var thoughts by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = AppColor.White)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Help us improve our service",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                color = AppColor.Black,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.roboto_semibold))
            )
        )

        HorizontalDivider(thickness = 1.dp, color = AppColor.gray_3)

        Text(
            text = "How would you like to describe your experience with our Servidex?",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                color = AppColor.gray_4,
                textAlign = TextAlign.Start,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular))
            )
        )

        EmojiListing(
            selectedLevel = selectedLevel,
            onLevelSelected  = { level  ->
                selectedLevel = level
            }
        )

        Text(
            text = "Whats's you overall experience?",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                color = AppColor.gray_4,
                textAlign = TextAlign.Start,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular))
            )
        )

        // Review text field
        OutlinedTextField(
            value = thoughts,
            onValueChange = { thoughts = it },
            label = { Text("Please share your thoughts...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            shape = RoundedCornerShape(8.dp),
            minLines = 5,
            maxLines = 8,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = AppColor.white_3,
                focusedContainerColor = AppColor.White,
                focusedBorderColor = AppColor.Primary,
                unfocusedBorderColor = AppColor.white_3,
                focusedLabelColor = AppColor.Black,
                cursorColor = AppColor.Black
            )
        )

        AnimatedButton(
            text = "Submit",
            paddingStart = 0.dp,
            paddingEnd = 0.dp,
            backgroundColor = if (selectedLevel > 0 && selectedLevel < 6 ) AppColor.Primary else AppColor.gray_3,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (selectedLevel > 0 && selectedLevel < 6 ) onSubmit(selectedLevel, thoughts)
            }
        )
    }
}

@Composable
fun EmojiListing(
    selectedLevel: Int, // 1 to 5
    onLevelSelected: (Int) -> Unit
) {
    val emojis = listOf("😔", "😐", "🙂", "😃", "🤩")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColor.White)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        emojis.forEachIndexed { index, emoji ->
            val level = index + 1
            EmojiCard(
                emoji = emoji,
                isSelected = (selectedLevel == level),
                onClick = { onLevelSelected(level) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun EmojiCard(
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) AppColor.Primary else AppColor.gray_4
    val backgroundColor = if (isSelected) AppColor.Primary.copy(alpha = 0.1f) else AppColor.White

    Box(
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
    }
}

//@Composable
//fun EmojiListing(
//    selectedEmoji: String,
//    onEmojiSelected: (String) -> Unit
//) {
//    val emojis = listOf("😔", "😐", "🙂", "😃", "🤩")
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(AppColor.White)
//            .padding(vertical = 4.dp),
//        horizontalArrangement = Arrangement.spacedBy(10.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        emojis.forEach { emoji ->
//            EmojiCard(
//                emoji = emoji,
//                isSelected = (emoji == selectedEmoji),
//                onClick = { onEmojiSelected(emoji) },
//                modifier = Modifier.weight(1f)
//            )
//        }
//    }
//}
//
//@Composable
//fun EmojiCard(
//    emoji: String,
//    isSelected: Boolean,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val borderColor = if (isSelected) AppColor.Primary else AppColor.gray_4
//    val backgroundColor = if (isSelected) AppColor.Primary.copy(alpha = 0.1f) else AppColor.White
//
//    Box(
//        modifier = modifier
//            .size(48.dp)
//            .clip(RoundedCornerShape(10.dp))
//            .background(backgroundColor)
//            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
//            .clickable { onClick() },
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = emoji,
//            fontSize = 24.sp,
//            textAlign = TextAlign.Center
//        )
//    }
//}
