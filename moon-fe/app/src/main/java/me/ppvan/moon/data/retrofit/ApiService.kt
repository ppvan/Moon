package me.ppvan.moon.data.retrofit

import me.ppvan.moon.data.remote.AuthenticationDto
import me.ppvan.moon.data.remote.LogoutDto
import me.ppvan.moon.data.remote.RegisterDto
import me.ppvan.moon.data.remote.TokenResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/v1/auth/register")
    suspend fun register(@Body registerDto: RegisterDto): Response<TokenResponseDto>

    @POST("/api/v1/auth/authenticate")
    suspend fun authenticate(@Body authenticationDto: AuthenticationDto): Response<TokenResponseDto>

    @POST("/api/v1/auth/logout")
    suspend fun logout(@Body logoutDto: LogoutDto): Response<String>
}