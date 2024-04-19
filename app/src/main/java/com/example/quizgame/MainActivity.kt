package com.example.quizgame

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.quizgame.screen.Screen
import com.example.quizgame.ui.theme.QuizGameTheme


class MainActivity : ComponentActivity() {

    lateinit var navController : NavHostController;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QuizGameTheme {
                navController = rememberNavController()

                // A must for centralizeing NavController
                setContent {
                    SetupNavGraph(navController = navController)

                }

                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    TreeView(
                        treeImage = painterResource(id = R.drawable.tree),
                        navController
                    )

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreeView(
    treeImage: Painter,
    navController: NavController,
) {

    var showBottomSheet by remember { mutableStateOf(false) }

    //stores in Hashmap for all acitivites to be able to refer, but also limited
    var selectedTimePeriod by remember { mutableIntStateOf(0) }

    //clearing all activities on top of the current activity in the stack
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White) // Set your desired background color here
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Taskbar(
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.weight(1f))
        if(showBottomSheet){
            ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Select Time Period:",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Slider(
                        value = selectedTimePeriod.toFloat(),
                        onValueChange = { newValue ->
                            selectedTimePeriod = newValue.toInt()
                        },
                        valueRange = 1f..60f,
                        steps = 59,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Text(
                        text = "$selectedTimePeriod minutes",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(

                        onClick = {
                            //Replacing this with navController
                            val bundle = Bundle()
                            bundle.putInt("Time", selectedTimePeriod)
                            Log.d("ButtonClicked " , Screen.Timer.route)
                            navController.navigate(Screen.Timer.route  + "/$selectedTimePeriod") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(Color.Blue)
                    ) {
                        Text(text = "Start Forest!")
                    }
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = treeImage,
                contentDescription = "Tree",
                modifier = Modifier.size(200.dp)
            )
            Button(

                onClick = {showBottomSheet = true },
                shape = CircleShape,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text(text = "Start planting!")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePeriodSelection(
    activity: Activity,
    intent: Intent
) {
    var selectedTimePeriod by remember { mutableStateOf(5) } // Default time period is 5 minutes
    var showBottomSheet by remember { mutableStateOf(true) }
    ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select Time Period:",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Slider(
                value = selectedTimePeriod.toFloat(),
                onValueChange = { newValue ->
                    selectedTimePeriod = newValue.toInt()
                },
                valueRange = 1f..60f,
                steps = 59,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Text(
                text = "$selectedTimePeriod minutes",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { activity.startActivity(intent) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(Color.Blue)
            ) {
                Text(text = "Start Forest!")
            }
        }
    }

}


@Composable
fun Taskbar(
    modifier: Modifier = Modifier,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    Surface(
        color = Color.Gray,
        modifier = modifier ,

        ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Button(
                onClick = { menuExpanded = !menuExpanded  },
                shape = CircleShape
            ) {
                Text(text = "Settings")
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(onClick = { /* Handle refresh! */ },text = { Text(text = "About us") })
                DropdownMenuItem(onClick = { /* Handle refresh! */ },text = { Text(text = "Statistics") })
                DropdownMenuItem(onClick = { /* Handle refresh! */ },text = { Text(text = "Settings") })

            }
        }
    }
}
