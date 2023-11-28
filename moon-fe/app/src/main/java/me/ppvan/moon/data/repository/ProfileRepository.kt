package me.ppvan.moon.data.repository

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    // Cập nhật tên người dùng
    fun updateName(newName: String) {
        val currentUser = _loggedInUser.value
        if (currentUser != null) {
            _loggedInUser.value = currentUser.copy(name = newName)
        }
    }

    // Cập nhật email người dùng
    fun updateEmail(newEmail: String) {
        val currentUser = _loggedInUser.value
        if (currentUser != null) {
            _loggedInUser.value = currentUser.copy(email = newEmail)
        }
    }

}
