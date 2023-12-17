package me.ppvan.moon.data.repository

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import me.ppvan.moon.data.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor() {
    private var _loggedInUser = mutableStateOf(User.default())
    val loggedInUser: State<User> get() = _loggedInUser

    init {
        _loggedInUser.value = User.default()
    }



}
