package me.ppvan.moon.data.retrofit

import me.ppvan.moon.data.remote.AuthenticationRequest
import me.ppvan.moon.data.remote.LogoutDto
import me.ppvan.moon.data.remote.RegisterRequest
import me.ppvan.moon.data.remote.RegisterResponse
import me.ppvan.moon.data.remote.SongResponse
import me.ppvan.moon.data.remote.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/api/v1/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("/api/v1/auth/authenticate")
    suspend fun authenticate(@Body authenticationRequest: AuthenticationRequest): Response<TokenResponse>

    @POST("/api/v1/auth/logout")
    suspend fun logout(@Body logoutDto: LogoutDto): Response<String>


    @GET("/api/v1/songs/suggestions")
    suspend fun suggest(@Query("keyword") keyword: String): Response<List<String>>

    @GET("/api/v1/songs/search/title")
    suspend fun search(@Query("title") query: String): Response<List<SongResponse>>
}