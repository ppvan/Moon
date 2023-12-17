package me.ppvan.moon.ui.viewmodel

//import me.ppvan.moon.data.repository.AuthService
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.ppvan.moon.data.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileViewModel @Inject constructor() : ViewModel() {
    private val _currentUser = MutableStateFlow(User.default())
    val currentUser = _currentUser.asStateFlow()

    private val _firstName = MutableStateFlow("")
    val firstName = _firstName.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName = _lastName.asStateFlow()

    private val _avatarUrl = MutableStateFlow("")
    val avatarUrl = _avatarUrl.asStateFlow()


    private val _editFirstName = MutableStateFlow(false)
    val editFirstName = _editFirstName.asStateFlow()

    private val _editLastName = MutableStateFlow(false)
    val editLastName = _editLastName.asStateFlow()

    fun toggleEditFirstName(edit: Boolean) {
        _editFirstName.update { edit }
    }

    fun toggleEditLastName(edit: Boolean) {
        _editLastName.update { edit }
    }


    fun onFirstNameChange(newName: String) {
        _firstName.update { newName }
    }

    fun onLastNameChange(newName: String) {
        _lastName.update { newName }
    }

    fun onAvatarUrlChange(newUrl: String) {
        _avatarUrl.update { newUrl }
    }

    fun saveProfile() {
        Log.d("ProfileVM", "Saving profile")
        Log.d("ProfileVM", firstName.value)
        Log.d("ProfileVM", lastName.value)
        _currentUser.update { it.copy(firstName = firstName.value, lastName = lastName.value, avatarUrl = avatarUrl.value) }
    }
}
