package me.ppvan.moon.data.model

data class User(
    val avatarUrl: String,
    val name: String,
    val email: String,

) {
    companion object {
        fun default(): User {
            return User(
                avatarUrl = "",
                name = "Phùng Lê Anh Quân",
                email = "phungquan27052003@gmail.com",
            )
        }
    }
}