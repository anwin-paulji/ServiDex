package com.zontechx.servidex.ui.provider.addServiceScreen.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.data.model.PricingType
import com.zontechx.servidex.data.static.AppConstant.EMERGENCY_SERVICE
import com.zontechx.servidex.data.static.AppConstant.EMERGENCY_SERVICE_DESCRIPTION
import com.zontechx.servidex.data.static.AppConstant.EXPERIANCE_LEVEL
import com.zontechx.servidex.data.static.AppConstant.EXPERIANCE_LEVEL_DESCRIPTION
import com.zontechx.servidex.data.static.AppConstant.EXPERIANCE_LEVEL_HINT
import com.zontechx.servidex.data.static.AppConstant.NO
import com.zontechx.servidex.data.static.AppConstant.SERVICE_CHARGES
import com.zontechx.servidex.data.static.AppConstant.SERVICE_CHARGES_DESCRIPTION
import com.zontechx.servidex.data.static.AppConstant.SERVICE_DESCRIPTION
import com.zontechx.servidex.data.static.AppConstant.SERVICE_DESCRIPTION_DESCRIPTION
import com.zontechx.servidex.data.static.AppConstant.SERVICE_DESCRIPTION_HINT
import com.zontechx.servidex.data.static.AppConstant.SERVICE_TITLE
import com.zontechx.servidex.data.static.AppConstant.SERVICE_TITLE_DESCRIPTION
import com.zontechx.servidex.data.static.AppConstant.SERVICE_TITLE_HINT
import com.zontechx.servidex.data.static.AppConstant.SET_SERVICE_AREA
import com.zontechx.servidex.data.static.AppConstant.YES
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.ui.provider.addServiceScreen.vm.AddServiceScreenVM
import com.zontechx.servidex.ui.theme.AppColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ServiceDetailsForm(pagerState: PagerState, vm: AddServiceScreenVM = viewModel()) {

    val ctx = LocalContext.current

    val isFormValid by vm.isStep2FormValid.collectAsState()
    val serviceTitle by vm.serviceTitle.collectAsState()
    val serviceDescription by vm.serviceDescription.collectAsState()
    val emergencyOption by vm.emergencyService.collectAsState()

    var loadAfterLaunch by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val onClickSubCategory: () -> Unit = {
        if (vm.selectedCategory.value.id != null) {
            SubCategoryListSheet.show()
        } else {
            CategoryListSheet.show()
        }
    }

    /**
     * Hide Keyboard
     * Page to Specific Index
     * */
    val handleSubmitButtonClick = {

        keyboardController?.hide()

        vm.addServiceDetailsAPI()
    }

    val responseState by vm.addServiceResponse.collectAsState()

    when (responseState) {
        is ApiResponse.Error -> {
            val errorMsg = (responseState as ApiResponse.Error).message
            Toast.makeText(ctx, "Failed : ${errorMsg}", Toast.LENGTH_SHORT).show()
        }

        else -> {}
    }

    LaunchedEffect(Unit) {
        delay(500)
        loadAfterLaunch = true
    }


    Box(modifier = Modifier.imePadding()) {
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(bottom = 60.dp)
                .verticalScroll(rememberScrollState())
        ) {

            if (loadAfterLaunch) {

                CategorySelection(
                    onClickCategory = { CategoryListSheet.show() },
                    onClickSubCategory = { onClickSubCategory() })

                CustomTextField(
                    title = SERVICE_TITLE,
                    hint = SERVICE_TITLE_HINT,
                    description = SERVICE_TITLE_DESCRIPTION,
                    textValue = serviceTitle,
                    onValueChange = { vm.onServiceTitleChange(it) })

                CustomTextField(
                    title = SERVICE_DESCRIPTION,
                    hint = SERVICE_DESCRIPTION_HINT,
                    description = SERVICE_DESCRIPTION_DESCRIPTION,
                    textValue = serviceDescription,
                    onValueChange = { vm.onServiceDesicriptionChange(it) })

                ExperianceLevel(
                    title = EXPERIANCE_LEVEL,
                    hint = EXPERIANCE_LEVEL_HINT,
                    description = EXPERIANCE_LEVEL_DESCRIPTION,
                    onValueChange = {
                        vm.onExperienceLevelChange(it)
                    })

                ServiceChargesField()

                EmergencyService(
                    title = EMERGENCY_SERVICE,
                    description = EMERGENCY_SERVICE_DESCRIPTION,
                    selectionOption = emergencyOption,
                    onValueChange = {
                        vm.onEmergencyServiceChange(it)
                    })

                // For Location
                ServiceArea( title = SET_SERVICE_AREA, description = SERVICE_TITLE_DESCRIPTION)
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .align(Alignment.BottomCenter)
        ) {
            if (responseState is ApiResponse.Loading) {
                CircularProgressIndicator(color = AppColor.Primary, strokeWidth = 4.dp)
            } else {
                BottomSubmitButton(enableButton = isFormValid, onClick = {
                    handleSubmitButtonClick()
                })
            }
        }
    }
}

@Preview
@Composable
fun ExperianceLevelPreview() {
    ExperianceLevel(
        title = "", hint = "", description = "", onValueChange = {})
}


@Composable
fun ExperianceLevel(
    title: String = "",
    hint: String = "",
    description: String = "",
    onValueChange: (String) -> Unit,
    vm: AddServiceScreenVM = viewModel()
) {

    val selectedExperienceLevel by vm.experienceLevel.collectAsState()

    val options = listOf(
        "0-1 years",
        "1-2 years",
        "2-3 years",
        "3-4 years",
        "4-5 years",
        "5-6 years",
        "6-7 years",
        "7-8 years",
        "8-9 years",
        "9-10 years",
        "10+ years"
    )

    var expanded by remember { mutableStateOf(false) }

    var textFieldModifier =
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 5.dp)
            .fillMaxWidth()

    var titleModifier =
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 2.dp)
            .fillMaxWidth()

    var descriptionModifier =
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 15.dp)
            .fillMaxWidth()

    var titleTextStyle =
        TextStyle(color = AppColor.gray_3, fontWeight = FontWeight.W400, fontSize = 16.sp)

    var hintTextStyle =
        TextStyle(color = AppColor.gray_4, fontWeight = FontWeight.W400, fontSize = 16.sp)

    var descritptionTextStyle =
        TextStyle(color = AppColor.gray_3, fontWeight = FontWeight.W400, fontSize = 12.sp)

    var inputTextStyle =
        TextStyle(color = AppColor.Black, fontWeight = FontWeight.W600, fontSize = 18.sp)

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = Color.White)
    ) {
        Text(
            text = title, modifier = titleModifier, style = titleTextStyle
        )

        TextField(
            value = selectedExperienceLevel,
            onValueChange = onValueChange,
            placeholder = { Text(text = hint, style = hintTextStyle) },
            singleLine = true,
            textStyle = inputTextStyle,
            enabled = false,
            colors = TextFieldDefaults.colors(
                disabledContainerColor = AppColor.gray_2,
                focusedContainerColor = AppColor.gray_2,
                unfocusedContainerColor = AppColor.gray_2,
                focusedIndicatorColor = AppColor.gray_2,
                unfocusedIndicatorColor = AppColor.gray_2,
                cursorColor = AppColor.Black
            ),
            modifier = textFieldModifier.clickable(enabled = true, onClick = { expanded = true })
        )

        Text(
            text = description, modifier = descriptionModifier, style = descritptionTextStyle
        )

        DropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onValueChange(option)
                    expanded = false
                }, text = { Text(option) })
            }
        }
    }
}

enum class EmergencyServiceOption {
    AVAILABLE, NOT_AVAILABLE
}

@Composable
fun EmergencyService(
    title: String = "",
    description: String = "",
    selectionOption: EmergencyServiceOption,
    onValueChange: (EmergencyServiceOption) -> Unit,
) {

    var titleModifier =
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 2.dp)
            .fillMaxWidth()

    var descriptionModifier =
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 15.dp)
            .fillMaxWidth()

    var titleTextStyle =
        TextStyle(color = AppColor.gray_3, fontWeight = FontWeight.W400, fontSize = 16.sp)

    var descritptionTextStyle =
        TextStyle(color = AppColor.gray_3, fontWeight = FontWeight.W400, fontSize = 12.sp)

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = Color.White)
    ) {
        Text(
            text = title, modifier = titleModifier, style = titleTextStyle
        )

        Row {
            Row(
                modifier = Modifier, verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectionOption == EmergencyServiceOption.AVAILABLE,
                    onClick = { onValueChange(EmergencyServiceOption.AVAILABLE) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = AppColor.Yellow, unselectedColor = AppColor.darkGray
                    )
                )
                Text(
                    text = YES,
                    modifier = Modifier.clickable { onValueChange(EmergencyServiceOption.AVAILABLE) })
            }

            Row(
                modifier = Modifier, verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectionOption == EmergencyServiceOption.NOT_AVAILABLE,
                    onClick = { onValueChange(EmergencyServiceOption.NOT_AVAILABLE) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = AppColor.Yellow, unselectedColor = AppColor.darkGray
                    )
                )
                Text(
                    text = NO,
                    modifier = Modifier.clickable { onValueChange(EmergencyServiceOption.NOT_AVAILABLE) })
            }
        }

        Text(
            text = description, modifier = descriptionModifier, style = descritptionTextStyle
        )
    }
}


@Composable
fun ServiceChargesField(vm: AddServiceScreenVM = viewModel()) {

    val title = SERVICE_CHARGES
    val description = SERVICE_CHARGES_DESCRIPTION

    var hint = "Charge per (Minuits / Hourly)"

    val price by vm.price.collectAsState()
    val priceType by vm.priceType.collectAsState()
    val unitDuratioin by vm.unitDuration.collectAsState()


    var textFieldModifier =
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 5.dp)
            .fillMaxWidth()

    var titleModifier =
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 2.dp)
            .fillMaxWidth()

    var descriptionModifier =
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 15.dp)
            .fillMaxWidth()

    var titleTextStyle =
        TextStyle(color = AppColor.gray_3, fontWeight = FontWeight.W400, fontSize = 16.sp)

    var hintTextStyle =
        TextStyle(color = AppColor.gray_4, fontWeight = FontWeight.W400, fontSize = 16.sp)

    var descritptionTextStyle =
        TextStyle(color = AppColor.gray_3, fontWeight = FontWeight.W400, fontSize = 12.sp)

    var inputTextStyle =
        TextStyle(color = AppColor.Black, fontWeight = FontWeight.W600, fontSize = 18.sp)

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = Color.White)
    ) {
        Text(
            text = title, modifier = titleModifier, style = titleTextStyle
        )

        Row {
            Row(
                modifier = Modifier, verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    enabled = false,
                    selected = priceType == PricingType.MINUTES,
                    onClick = { vm.onPriceTypeChange(PricingType.MINUTES) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = AppColor.Yellow, unselectedColor = AppColor.darkGray
                    )
                )
                Text(
                    text = PricingType.MINUTES.toString(),
                    modifier = Modifier.clickable { vm.onPriceTypeChange(PricingType.MINUTES) })
            }

            Row(
                modifier = Modifier, verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = priceType == PricingType.HOURS,
                    onClick = { vm.onPriceTypeChange(PricingType.HOURS) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = AppColor.Yellow, unselectedColor = AppColor.darkGray
                    )
                )
                Text(
                    text = PricingType.HOURS.toString(),
                    modifier = Modifier.clickable { vm.onPriceTypeChange(PricingType.HOURS) })
            }
        }

        TextField(
            value = price,
            onValueChange = { newValue ->
                // Keep only digits and max 5 characters
                val filteredValue = newValue.filter { it.isDigit() }
                    .take(5)
                vm.onPriceChange(filteredValue)
            },
            prefix = { Text(text = "₹ ") },
            suffix = { Text(text = "/ ${priceType}") },
            placeholder = { Text(text = hint, style = hintTextStyle) },
            singleLine = true,
            textStyle = inputTextStyle,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.NumberPassword
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = AppColor.gray_2,
                unfocusedContainerColor = AppColor.gray_2,
                focusedIndicatorColor = AppColor.gray_2,
                unfocusedIndicatorColor = AppColor.gray_2,
                cursorColor = AppColor.Black
            ),
            modifier = textFieldModifier
        )

        Text(
            text = description, modifier = descriptionModifier, style = descritptionTextStyle
        )
    }
}