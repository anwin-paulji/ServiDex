package com.zontechx.servidex.ui.screens.mainScreen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.LocalSharedViewModel
import com.zontechx.servidex.R
import com.zontechx.servidex.model.LocationData
import com.zontechx.servidex.model.response.LocationDataItem
import com.zontechx.servidex.ui.screens.mainScreen.vm.MainScreenVM
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.ui.vm.SharedVM
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSelectionSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onClick: () -> Unit = {},
    onclickSelectFromMap: () -> Unit = {},
    onClickUseCurrentLocation: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val vm: MainScreenVM = viewModel()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        containerColor = AppColor.White,
        dragHandle = {}) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Toolbar {
                scope.launch {
                    sheetState.hide()
                    vm.onClickLocationSheet(false)
                }
            }

            SearchBar()
//            UserCurrentLocation()
//            PopularCities()
            AllCities(onclickSelectFromMap = onclickSelectFromMap)
        }
    }
}


@Preview
@Composable
fun Toolbar(onClickBack: () -> Unit = {}) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.arrow_down),
            modifier = Modifier
                .size(36.dp)
                .clickable { onClickBack() },
            contentDescription = ""
        )
        Text(
            text = "Select Location", style = TextStyle(
                color = AppColor.darkGray, fontSize = 18.sp, fontFamily = FontFamily(
                    Font(R.font.roboto_medium)
                )
            )
        )
    }

}

@Preview
@Composable
fun SearchBar() {

    var searchQuery by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Search...", fontSize = 14.sp) },
        leadingIcon = {
            Icon(
                Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)
            )
        },
        textStyle = TextStyle(fontSize = 18.sp, lineHeight = 18.sp, color = AppColor.darkGray),
        singleLine = true,
        shape = RoundedCornerShape(50.dp),
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 44.dp)
            .padding(bottom = 10.dp), // ✅ smaller but safe minimum height
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = AppColor.white_3,
            unfocusedContainerColor = AppColor.white_3,
            focusedBorderColor = AppColor.white_3,
            unfocusedBorderColor = AppColor.white_3
        )
    )
}

@Preview
@Composable
fun PopularCities() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Text(
            text = "Popular Cities", style = TextStyle(
                color = AppColor.darkGray,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.roboto_medium)),
                textAlign = TextAlign.Center
            )
        )
    }
}

@Preview
@Composable
fun AllCities(onclickSelectFromMap: () -> Unit = {}) {
    val TAG = "AllCities"
    val vm: MainScreenVM = viewModel()
    val locationList by vm.locationList.collectAsState()
    val sharedVM: SharedVM = LocalSharedViewModel.current
    val address by sharedVM.currentAddress.collectAsState()

    fun handleClick() {
//        address?.let { sharedVM.setCurrentAddress(it) }
//        vm.onClickLocationSheet(false)
        onclickSelectFromMap()
    }

    fun handleItemClick(item: LocationDataItem) {
        sharedVM.setCurrentAddress(
            address = LocationData(
                locationName = item.locationName,
                longitude = item.location.coordinates[0], //In Mongo DB longitude >> index 0 || latitude > Index 1
                latitude = item.location.coordinates[1]
            )
        )
        vm.onClickLocationSheet(false)
    }

    LaunchedEffect(Unit) {
        if (locationList.size < 100) {
            vm.loadNextPage()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "All Cities", style = TextStyle(
                    color = AppColor.darkGray,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_medium)),
                    textAlign = TextAlign.Center
                )
            )
            Text(
                text = "Select from Map",
                modifier = Modifier
                    .clickable { handleClick() }
                    .padding(top = 10.dp, bottom = 10.dp),
                style = TextStyle(
                    color = AppColor.Primary,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_semibold))
                )
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
        ) {
            items(
                items = locationList, key = { item -> item._id.`$oid` }) { item ->
                Text(
                    text = item.locationName,
                    style = TextStyle(fontSize = 16.sp, color = AppColor.darkGray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { handleItemClick(item) }
                        .padding(start = 15.dp, top = 15.dp, end = 15.dp, bottom = 15.dp))
                Divider(color = AppColor.gray_2, thickness = 0.5.dp, startIndent = 0.dp)

                if (locationList.lastOrNull()?._id == item._id) { // check last item
                    LaunchedEffect(Unit) {
                        vm.loadNextPage()
                    }
                }
            }

            if (vm.isLoading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}