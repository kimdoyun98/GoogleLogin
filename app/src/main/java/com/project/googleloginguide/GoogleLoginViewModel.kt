package com.project.googleloginguide

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.project.googleloginguide.data.TokenRepository
import com.project.googleloginguide.google.GoogleLogin
import com.project.googleloginguide.util.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GoogleLoginViewModel(
    private val tokenRepository: TokenRepository,
    private val googleLogin: GoogleLogin
) : ViewModel() {

    private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    init {
        viewModelScope.launch {
            tokenRepository.refreshToken
                .collect { refreshToken ->
                    if (refreshToken == null) {
                        _loginState.value = LoginState.LogOut

                        return@collect
                    }

                    //TODO 서버 Access Token 요청

                    //TODO 만료 -> 로그인, 정상 -> 앱 진행
                }
        }
    }

    companion object {
        fun factory(
            tokenRepository: TokenRepository,
            googleLogin: GoogleLogin,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                return GoogleLoginViewModel(
                    tokenRepository,
                    googleLogin
                ) as T
            }
        }
    }
}
