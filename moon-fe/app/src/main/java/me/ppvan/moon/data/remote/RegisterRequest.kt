package me.ppvan.moon.data.remote

data class RegisterRequest(
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String
)