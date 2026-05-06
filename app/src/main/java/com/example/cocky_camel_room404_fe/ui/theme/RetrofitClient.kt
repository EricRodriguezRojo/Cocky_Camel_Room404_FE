package com.example.cocky_camel_room404_fe

import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class LoginResponse(
    val token: String?,
    val message: String,
    val role: String? = "User"
)

data class User(
    val email: String,
    val nickname: String,
    val password: String?,
    val role: String = "User",
    val isPremium: Boolean = false
)

data class FakeEmailDto(
    val id: Int? = null,
    val sender: String,
    val bodyText: String
)

data class RankingDto(
    val nickname: String,
    val totalPoints: Long,
    val totalTime: Long
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

    @GET("api/user/{email}")
    suspend fun getUser(@Path("email") email: String): Response<User>

    @POST("api/game/trigger-malware")
    suspend fun triggerMalware(
        @Header("Authorization") token: String
    ): Response<Map<String, String>>

    @GET("api/emails")
    suspend fun getEmails(): Response<List<FakeEmailDto>>

    @POST("api/emails")
    suspend fun createEmail(@Body email: FakeEmailDto): Response<FakeEmailDto>

    @PUT("api/emails/{id}")
    suspend fun updateEmail(@Path("id") id: Int, @Body email: FakeEmailDto): Response<FakeEmailDto>

    @DELETE("api/emails/{id}")
    suspend fun deleteEmail(@Path("id") id: Int): Response<Void>

    @POST("api/progress/complete/{puzzleName}")
    suspend fun completePuzzle(
        @Header("Authorization") token: String,
        @Path("puzzleName") puzzleName: String,
        @Body body: Map<String, Int>
    ): Response<Map<String, String>>

    @GET("api/ranking")
    suspend fun getRanking(): Response<List<RankingDto>>
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