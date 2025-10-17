package com.zontechx.servidex.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zontechx.servidex.R
import com.zontechx.servidex.ui.theme.AppColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingReviewSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit = {},
    onRatingSubmit: (Int, String) -> Unit = { _, _ -> }
) {
    // Local UI state (hoisted within the sheet)
    var rating by remember { mutableStateOf(0) }
    var review by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(16.dp),
        containerColor = AppColor.White,
        dragHandle = {}
    ) {
        RatingReviewSheetContent(
            rating = rating,
            onRatingChanged = { rating = it },
            review = review,
            onReviewChanged = { review = it },
            onSubmit = { onRatingSubmit(rating, review) }
        )
    }
}

/**
 * Reusable child content for the sheet.
 * Stateless — receives state & callbacks from parent.
 */
@Preview
@Composable
fun RatingReviewSheetContent(
    rating: Int = 0,
    onRatingChanged: (Int) -> Unit = { _ ->},
    review: String = "",
    onReviewChanged: (String) -> Unit = { _ ->},
    onSubmit: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(color = AppColor.White)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Rate & Review",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                color = AppColor.Black,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.roboto_semibold))
            )
        )

        Text(
            text = "Rate your experience with this service provider and help others choose better.",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                color = AppColor.gray_4,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular))
            )
        )

        // Your star rating (1..5) — stateless usage
        StarRating(
            rating = rating,
            onRatingChanged = onRatingChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            starSize = 36.dp,
            activeColor = AppColor.Primary
        )

        // Review text field
        OutlinedTextField(
            value = review,
            onValueChange = onReviewChanged,
            label = { Text("Write your review") },
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

        // Submit button — replace with AnimatedButton if you have it
        AnimatedButton(
            text = "Submit",
            paddingStart = 0.dp,
            paddingEnd = 0.dp,
            backgroundColor = if (rating > 0) AppColor.Primary else AppColor.gray_3,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (rating > 0) onSubmit()
            }
        )
    }
}