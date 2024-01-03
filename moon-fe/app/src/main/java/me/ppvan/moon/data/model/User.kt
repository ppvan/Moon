package me.ppvan.moon.data.model

data class User(
    val avatarUrl: String,
    val firstName: String,
    val lastName: String,
    val email: String,

) {
    companion object {
        fun default(): User {
            return User(
                avatarUrl = "https://avatars.githubusercontent.com/ppvan",
                firstName = "Anh Quân",
                lastName = "Phùng Lê",
                email = "phungquan27052003@gmail.com",
            )
        }
    }
}