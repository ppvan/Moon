package me.ppvan.moon.data.remote

import com.google.gson.annotations.SerializedName

class TokenResponse(
    @field:SerializedName("access_token") val accessToken: String,
    @field:SerializedName("refresh_token") val refreshToken: String
)