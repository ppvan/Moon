package me.ppvan.moon.data.remote

import com.google.gson.annotations.SerializedName

class TokenResponseDto(
    @field:SerializedName("accsess_token") val accessToken: String,
    @field:SerializedName("refresh_token") val refreshToken: String
)