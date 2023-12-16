package me.ppvan.moon.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.ppvan.moon.data.remote.RegisterRequest
import me.ppvan.moon.data.retrofit.ApiService
import javax.inject.Inject


enum class RegisterState {
    IDLE,
    LOADING,
    VALIDATION_ERROR,
    SUCCESS,
    FAILURE
}

@HiltViewModel
class RegisterViewModel @Inject constructor(private val moonService: ApiService): ViewModel() {

    private val _firstName = MutableStateFlow("")
    private val _lastName = MutableStateFlow("")
    private val _email = MutableStateFlow("")
    private val _password1 = MutableStateFlow("")
    private val _password2 = MutableStateFlow("")

    val firstName = _firstName.asStateFlow()
    val lastName = _lastName.asStateFlow()
    val email = _email.asStateFlow()
    val password1 = _password1.asStateFlow()
    val password2 = _password2.asStateFlow()

    private val _state = MutableStateFlow(RegisterState.IDLE)
    val state = _state.asStateFlow()

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()


    fun register() {
        _state.update { RegisterState.LOADING }
        viewModelScope.launch(Dispatchers.IO) {
            val registerRequest = RegisterRequest(firstName.value, lastName.value, email.value, password1.value)
            val response = moonService.register(registerRequest)

            withContext(Dispatchers.Main) {
                if (!response.isSuccessful || response.body() == null) {
                    Log.d("RegisterVM", "Failed: ${response.code()}")
                    _state.update { RegisterState.FAILURE }
                    _message.update { "${email.value} already exists" }
                } else {
                    Log.d("RegisterVM", "Success")
                    _state.update { RegisterState.SUCCESS }
                }
            }
        }
    }


    fun onFirstNameChanged(firstName: String) {
        _firstName.update { firstName }
    }

    fun onLastNameChanged(lastName: String) {
        _lastName.update { lastName }
    }

    fun onEmailChanged(email: String) {
        _email.update { email }
    }

    fun onPassword1Changed(password1: String) {
        _password1.update { password1 }
    }

    fun onPassword2Changed(password2: String) {
        _password2.update { password2 }
    }
}