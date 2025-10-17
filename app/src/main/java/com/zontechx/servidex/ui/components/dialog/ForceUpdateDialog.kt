package com.zontechx.servidex.ui.components.dialog

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.zontechx.servidex.R
import com.zontechx.servidex.ui.theme.AppColor
import kotlin.system.exitProcess


object ForceUpdateDialog {

    var instagramUrl:String = "https://play.google.com/store/apps/details?id=com.instagram.android&pcampaignid=web_share"

    private var showSheet by mutableStateOf(false)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Register(context:Context, onApply: () -> Unit) {

        if (showSheet) {

            val montserratFont = FontFamily(
                Font(R.font.montserrat_semibold, FontWeight.Normal)
            )

            Dialog(onDismissRequest = {}) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .height(215.dp)
                                .fillMaxWidth()
                                .background(AppColor.orange_9)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.rocker_icon),
                                contentDescription = "",
                                modifier = Modifier.padding(top = 24.dp, bottom = 11.dp)
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                "New Update Available",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "A new version of ServiDex is available. Please update to continue.",
                                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                                style = TextStyle(color = AppColor.HINT_COLOR_2, fontSize = 16.sp, textAlign = TextAlign.Center)
                            )
                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AppColor.Primary
                                ),
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl))
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(intent)

                                    // Exit app gracefully
                                    if (context is Activity) {
                                        context.finishAffinity() // close all activities
                                    }
                                    // Optional: ensure process is killed
                                    android.os.Process.killProcess(android.os.Process.myPid())
                                }
                            ) {
                                Text("Update Now", style = TextStyle(color = AppColor.buttonTestColor, fontSize = 16.sp, fontFamily = montserratFont))
                            }
                        }
                    }
                }
            }
        }
    }

    fun show() {
        showSheet = true
    }

    fun dismiss() {
        showSheet = false
    }
}

@Preview(showBackground = true)
@Composable
fun ForceUpdateDialogPreview() {
    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(215.dp)
                        .fillMaxWidth()
                        .background(AppColor.orange_9)
                ) {
                    Image(
                        painter = painterResource(R.drawable.rocker_icon),
                        contentDescription = "",
                        modifier = Modifier.padding(top = 24.dp, bottom = 11.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        "New Update Available",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "A new version of ServiDex is available. Please update to continue.",
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                        style = TextStyle(color = AppColor.HINT_COLOR_2, fontSize = 16.sp)
                    )
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColor.Primary,
                            contentColor = Color.Black
                        ),
                        onClick = {}
                    ) {
                        Text("Update Now")
                    }
                }
            }
        }
    }
}