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
import me.ppvan.moon.data.remote.AuthenticationRequest
import me.ppvan.moon.data.retrofit.ApiService
import me.ppvan.moon.data.retrofit.UserStore
import javax.inject.Inject

enum class LoginState {
    IDLE,
    LOADING,
    SUCCESS,
    FAILURE
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val moonService: ApiService,
    private val userStore: UserStore
) : ViewModel() {

    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")

    val email = _email.asStateFlow()
    val password = _password.asStateFlow()

    private val _state = MutableStateFlow(LoginState.IDLE)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch (Dispatchers.IO) {
            userStore.getAccessToken.collect {
                Log.d("LoginVM", it)
            }
        }
    }


    fun login() {
        val authenticationRequest = AuthenticationRequest(email.value, password.value)
        _state.update { LoginState.LOADING }
        viewModelScope.launch(Dispatchers.IO) {
            val response = moonService.authenticate(authenticationRequest)
            withContext(Dispatchers.Main) {
                if (!response.isSuccessful || response.body() == null) {
                    Log.d("LoginVM", "Failed: ${response.code()}")
                    _state.update { LoginState.FAILURE }
                } else {
                    val token = response.body()!!
                    userStore.saveToken(token.refreshToken)

                    _state.update { LoginState.SUCCESS }
                }
            }
        }
    }

    fun onEmailChange(email: String) {
        _email.update { email }
    }

    fun onPasswordChange(password: String) {
        _password.update { password }
    }
}