package me.ppvan.moon.ui.viewmodel

//import me.ppvan.moon.data.repository.AuthService
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import me.ppvan.moon.data.model.User
import me.ppvan.moon.data.repository.ProfileRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) : ViewModel() {
    val loggedInUser: State<User> = profileRepository.loggedInUser

    // Các phương thức để cập nhật thông tin người dùng
    fun updateName(newName: String) {
        // Gọi phương thức updateName trong profileRepository
        profileRepository.updateName(newName)
    }

    fun updateEmail(newEmail: String) {
        // Gọi phương thức updateEmail trong profileRepository
        profileRepository.updateEmail(newEmail)
    }
}
