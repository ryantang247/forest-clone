package com.lari.api_testing

import com.example.quizgame.backend.data.LoginDetails
import com.example.quizgame.backend.data.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    @Headers("Content-Type: application/json")
    suspend fun login(@Body loginRequest: LoginDetails): Response<LoginResponse>
}