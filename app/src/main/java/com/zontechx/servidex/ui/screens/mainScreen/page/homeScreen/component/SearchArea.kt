package com.zontechx.servidex.ui.screens.mainScreen.page.homeScreen.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchArea() {

    val searchInputField =
        @Composable {
            SearchBarDefaults.InputField(
                query = "",
                onQueryChange = {},
                expanded = false,
                onSearch = {},
                onExpandedChange = {},
                modifier = Modifier,
            )
        }

    Column(
        modifier = Modifier.padding(start = 15.dp, end = 15.dp).fillMaxWidth()
    ) {
        Text(
            text = "What service do you want?",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W400)
        )

        SearchBar(
            inputField = searchInputField,
            expanded = true,
            onExpandedChange = {},
            modifier = Modifier.fillMaxWidth()
        ) {

        }
    }
}

@Preview
@Composable
fun SearchAreaPreview() {
    SearchArea()
}