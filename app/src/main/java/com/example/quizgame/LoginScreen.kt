import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.quizgame.backend.data.LoginDetails
import com.example.quizgame.backend.data.LoginResponse
import com.example.quizgame.screen.Screen

import com.lari.api_testing.ApiService
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(apiService: ApiService, navController: NavController) {
    val context = LocalContext.current
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val loadingState = remember { mutableStateOf(false) }
    var loginResultState = remember { mutableStateOf<Result<LoginResponse?>?>(null) }
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White) // Set your desired background color here
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val email = emailState.value
                val password = passwordState.value

                // Perform login
                loadingState.value = true
//                viewModelScope.launch {
                scope.launch {
                    loginResultState.value = performLogin(apiService, email, password)
                    loginResultState.value?.let { result ->
                        when {
                            result.isSuccess -> {
                                val loginResponse = result.getOrThrow()
                                if (loginResponse != null) {
                                    if(loginResponse.status){
                                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                                        navController.navigate(Screen.Home.route){
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            // Ensure the start destination is not saved in the back stack
                                            launchSingleTop = true
                                            // Restore the state when navigating back to the start destination
                                            restoreState = true
                                        }
                                    }else {
                                        Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                                        loadingState.value = false
                                    }
                                }
                                // Handle successful login response

                            }
                            result.isFailure -> {
                                val exception = result.exceptionOrNull()
                                // Handle login failure
                                Toast.makeText(context, "Login failed: ${exception?.message}", Toast.LENGTH_SHORT).show()
                                loadingState.value = false

                            }
                        }
                    }
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Login")
        }

        if (loadingState.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }


    }
}

suspend fun performLogin(apiService: ApiService, email: String, password: String): Result<LoginResponse?> {
    return try {
        // Call the login function within a coroutine scope
        val response = apiService.login(LoginDetails(email, password))

        if (response.isSuccessful) {
            // Login successful, handle the response
            val loginResponse = response.body()
            Result.success(loginResponse)
        } else {
            // Login failed, handle the error
            val errorBody = response.errorBody()?.string()
            Result.failure(Exception(errorBody))
        }
    } catch (e: Exception) {
        // Handle other exceptions such as network errors
        Result.failure(e)
    }
}



@Composable
fun LoginSuccessScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Login Successful!", fontSize = 20.sp)
    }
}

@Composable
fun LoginFailedScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Login Failed. Invalid credentials!", color = Color.Red, fontSize = 20.sp)
    }
}
fun callLogin(call: ApiService){


}


