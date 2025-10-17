package com.zontechx.servidex.ui.screens.webViewScreen

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.widget.Toolbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.zontechx.servidex.LocalRootNavController
import com.zontechx.servidex.R
import com.zontechx.servidex.ui.components.RoundIconButton
import com.zontechx.servidex.ui.theme.AppColor

enum class WebViewType {
    TermsOfService, PrivacyPolicy, HowToUse
}

@Composable
fun WebViewScreen(webViewType: String) {
    val rootNavController = LocalRootNavController.current

    val (url, title) = remember(webViewType) {
        when (webViewType) {
            WebViewType.TermsOfService.name -> "https://servidex.in/terms" to "Terms of Service"
            WebViewType.PrivacyPolicy.name -> "https://servidex.in/privacy" to "Privacy Policy"
            WebViewType.HowToUse.name -> "https://servidex.in" to "How to Use"
            else -> "https://servidex.in" to "Servidex"
        }
    }

    Scaffold(
        topBar = {
            Toolbar(
                toolbarTitle = title, onBackClick = { rootNavController.popBackStack() })
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .background(AppColor.White)
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            MyWebView(url)
        }
    }
}


@Composable
fun Toolbar(toolbarTitle: String, onBackClick: () -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(start = 16.dp, end = 16.dp)
            .background(color = AppColor.White)
    ) {
        RoundIconButton(
            iconRes = R.drawable.arrow_left,
            iconTint = AppColor.Black,
            iconSize = 24.dp,
            modifier = Modifier.clickable { onBackClick() })
        Text(
            text = toolbarTitle, color = AppColor.Black, fontSize = 18.sp, fontFamily = FontFamily(
                Font(R.font.roboto_semibold)
            )
        )
    }
}

@Composable
fun MyWebView(url: String) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }

    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
//            settings.domStorageEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    isLoading = false
                }
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        AndroidView(
            factory = { webView },
            update = { it.loadUrl(url) },
            modifier = Modifier.fillMaxSize()
        )

        if (isLoading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AppColor.Primary)
            }
        }
    }
}
