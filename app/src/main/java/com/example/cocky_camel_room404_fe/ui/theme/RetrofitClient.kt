package com.example.cocky_camel_room404_fe

import com.google.gson.GsonBuilder
import retrofit2.Response
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*


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

data class ForgotPasswordRequest(
    val email: String
)

data class VerifyTokenRequest(
    val email: String,
    val token: String
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

    @POST("api/user/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<String>

    @POST("api/user/verify-reset-token")
    suspend fun verifyResetToken(@Body request: VerifyTokenRequest): Response<Map<String, Any>>

    @POST("api/user/reset-password")
    suspend fun resetPassword(@Body body: Map<String, String>): Response<String>

    @GET("api/user/email/{email}")
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

    private val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val gson = GsonBuilder().create()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(httpClient)
        .build()

    val instance: Room404Api by lazy { retrofit.create(Room404Api::class.java) }
}
