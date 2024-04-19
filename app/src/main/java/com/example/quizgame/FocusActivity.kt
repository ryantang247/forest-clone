package com.example.quizgame

import LoginScreen
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quizgame.screen.Screen
import com.example.quizgame.ui.theme.QuizGameTheme


//class FocusActivity: ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContent {
//            QuizGameTheme {
//                // A surface container using the 'background' color from the theme
//
//                val loggedIn = remember { mutableStateOf(false) }
//                val loginResponseCallback = remember { mutableStateOf(false) }
//
//                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//
////                    if(!loggedIn.value){
////                        LoginScreen(loginResponseCallback)
////
////                    }else {
//                        TreeViewFocus(
//                            treeImage = painterResource(id = R.drawable.tree),
//                            buttonText = "Open",
//                            onButtonClick = {}
//                        )
//                }
//            }
//        }
//    }
//
//}

@Composable
fun TreeViewFocus(
    treeImage: Painter,
    timerInitialValue: Int,
    navController: NavController
) {
    val currentActivity = LocalContext.current as? Activity
    val currentContext = LocalContext.current
    var isTimerFinished by remember { mutableStateOf(false) }

    var timerValue by remember { mutableIntStateOf(timerInitialValue * 100) }

    //the key will induce change in LaunchedEffect
    LaunchedEffect(key1 =  timerValue){
        startTimer(currentContext)
        if(timerValue>0 && !isTimerFinished){
            timerValue -=1
        }else {
            timerValue =0
            isTimerFinished = true
//
            showPopup(currentContext, navController)
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

private fun showPopup(context: Context, navController:NavController) {
    val activity = context as? Activity ?: return // Ensure context is an Activity

    if (activity.isFinishing) {
        return // Don't show dialog if activity is finishing
    }

    val builder = AlertDialog.Builder(context)
    builder.setTitle("Timer Finished")
    builder.setMessage("Your timer has finished!")

    builder.setPositiveButton("OK") { dialog, _ ->
        // Dismiss dialog and finish activity only if activity is not finishing
        if (!activity.isFinishing) {
            navController.navigate(Screen.Home.route)
            dialog.dismiss()
            activity.finish()
        }
    }


}

