package com.example.quizgame

import LoginScreen
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quizgame.screen.Screen


@Composable
fun TreeViewFocus(
    treeImage: Painter,
    timerInitialValue: Int,
    navController: NavController
) {

    var isTimerFinished by remember { mutableStateOf(false) }
    var openAlertDialog by  remember { mutableStateOf(false) }
    var timerValue by remember { mutableIntStateOf(timerInitialValue * 100) }

    //the key will induce change in LaunchedEffect
    LaunchedEffect(key1 =  timerValue){
//        startTimer(currentContext)
        if(timerValue>0 && !isTimerFinished){
            timerValue -=1
        }else {
            timerValue =0
            isTimerFinished = true

        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Taskbar(
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = treeImage,
                contentDescription = "Tree",
                modifier = Modifier.size(200.dp)
            )
            Text(
                text = "Timer: $timerValue",
                modifier = Modifier.padding(16.dp)
            )
            Button(
                onClick = { navController.navigate(Screen.Home.route)},
                shape = CircleShape,
                modifier = Modifier.align(Alignment.BottomCenter) ,
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text(text = "Cancel")
            }
        }
        AlertDialogExample(
            isTimerFinished = isTimerFinished,
            onDismissRequest = { openAlertDialog = false },
            onConfirmation = {
                openAlertDialog = false
                println("Confirmed!") // Add logic here to handle confirmation.
            },
            dialogTitle = "Timer finished",
            dialogText = "Great job focusing!",
            icon = Icons.Default.Info ,
            navController
        )
    }
}

private fun startTimer(context: Context) {
    object : CountDownTimer(60000, 1000) { // 60 seconds countdown
        override fun onTick(millisUntilFinished: Long) {
            // Timer is ticking
        }

        override fun onFinish() {
            // Timer finished, show the popup
            return
        }
    }.start()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogExample(
    isTimerFinished: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    navController: NavController
) {
    if (isTimerFinished) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                    navController.navigate(Screen.Home.route)

                }
            ) {
                Text("Confirm")
            }
        },
    )
    }
}

