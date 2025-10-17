package com.zontechx.servidex.ui.screens.onBoardingScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.zontechx.servidex.R
import com.zontechx.servidex.Screen
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.ui.theme.orange
import kotlinx.coroutines.launch


@Composable
fun OnBoardingScreen(rootNavController: NavHostController) {
    OnBoardingScreen_Main(rootNavController)
}

@Composable
fun OnBoardingScreen_Main(navController: NavController) {

    var TAG = "OnBoardingScreen";
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        3
    }
    val coroutineScope = rememberCoroutineScope()

    fun onClickButton() {
        coroutineScope.launch {
            if (pagerState.currentPage == 2) {
                navController.navigate(Screen.LoginRegisterScreen.route)

            } else {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(bottom = innerPadding.calculateBottomPadding())
                .fillMaxSize()
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
            ) { page ->

                var image = R.drawable.on1
                var title: String = AppConstant.ON_BOARDING_TITLE_1
                var content = AppConstant.ON_BOARDING_1

                when (page) {
                    0 -> {
                        image = R.drawable.on1;
                        title = AppConstant.ON_BOARDING_TITLE_1;
                        content = AppConstant.ON_BOARDING_1;
                    }

                    1 -> {
                        image = R.drawable.on2;
                        title = AppConstant.ON_BOARDING_TITLE_2;
                        content = AppConstant.ON_BOARDING_2;

                    }

                    2 -> {
                        image = R.drawable.on3;
                        title = AppConstant.ON_BOARDING_TITLE_3;
                        content = AppConstant.ON_BOARDING_3;
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = image),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black),
                                    startY = 0.0f,
                                    endY = 150f,
                                    tileMode = TileMode.Clamp
                                )
                            )
                    ) {
                        Column {
                            Text(
                                text = title,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp),
                                style = TextStyle(
                                    brush = Brush.linearGradient(
                                        colors = listOf(AppColor.Yellow, AppColor.Primary)
                                    )
                                )
                            )

                            Text(
                                text = content,
                                color = Color.White,
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 16.dp,
                                    bottom = 100.dp
                                ),
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    lineHeight = 20.sp,
                                    letterSpacing = 2.sp
                                )
                            )
                        }
                    }

                }
            }

            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 16.dp, end = 16.dp)
                    .width(200.dp)
                    .height(80.dp)
                    .align(Alignment.TopEnd)
            )

            PageIndicator(
                pageCount = 3,
                currentPage = pagerState.currentPage,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, end = 16.dp, top = 16.dp, start = 16.dp),
                activeColor = orange,
                inactiveColor = Color.White,
                onClickButton = {
                    onClickButton()
                }
            )
        }
    }
}

@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = orange,
    inactiveColor: Color = Color.White,
    spacing: Dp = 4.dp,
    onClickButton: () -> Unit = {}
) {

    val buttonText: String = if (currentPage == 2) "Get Started" else "Next"

    val horizontalGradientBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xffF57F17),
            Color(0xffFFEE58),
            Color(0xffFFF9C4)
        )
    )

    Row(
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        modifier = modifier,
    ) {

        LazyRow(
            modifier = Modifier.align(Alignment.CenterVertically),
            horizontalArrangement = Arrangement.Start
        ) {
            itemsIndexed(List(pageCount) { it }) { index, _ ->
                val isSelected = currentPage == index
                val color = if (isSelected) activeColor else inactiveColor
                val width = if (isSelected) 40.dp else 8.dp
                val height = 4.dp
                Box(
                    modifier = Modifier
                        .padding(horizontal = spacing)
                        .size(width, height)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }

        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(horizontal = 30.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(
                    brush =
                    Brush.linearGradient(
                        colors = listOf(AppColor.Primary, AppColor.Yellow)
                    )
                ),
            onClick = { onClickButton() }) {
            Text(text = buttonText)
        }
    }
}