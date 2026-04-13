package com.example.cocky_camel_room404_fe

import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

data class LoginResponse(
    val token: String?,
    val message: String
)

data class User(
    val email: String,
    val nickname: String,
    val password: String?,
    val role: String = "User",
    val isPremium: Boolean = false
)

interface Room404Api {
    @POST("api/user/login/{email}/{password}")
    suspend fun login(
        @Path("email") email: String,
        @Path("password") password: String
    ): Response<LoginResponse>

    @POST("api/user")
    suspend fun register(@Body user: User): Response<String>

    @POST("api/user/google-login")
    suspend fun googleLogin(@Body data: Map<String, String>): Response<LoginResponse>
}

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    val instance: Room404Api by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        retrofit.create(Room404Api::class.java)
    }
}