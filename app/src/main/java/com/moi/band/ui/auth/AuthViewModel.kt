package com.moi.band.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moi.band.data.model.AuthResponse
import com.moi.band.data.repository.AuthRepository
import com.moi.band.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<AuthResponse>?>(null)
    val loginState: StateFlow<Resource<AuthResponse>?> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<Resource<AuthResponse>?>(null)
    val registerState: StateFlow<Resource<AuthResponse>?> = _registerState.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            authRepository.login(username, password).collect {
                _loginState.value = it
            }
        }
    }

    fun register(
        username: String,
        email: String,
        password: String,
        passwordConfirm: String,
        displayName: String
    ) {
        viewModelScope.launch {
            authRepository.register(username, email, password, passwordConfirm, displayName).collect {
                _registerState.value = it
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun resetLoginState() {
        _loginState.value = null
    }

    fun resetRegisterState() {
        _registerState.value = null
    }
}