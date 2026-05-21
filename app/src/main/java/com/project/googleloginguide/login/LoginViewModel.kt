package com.project.googleloginguide.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.project.googleloginguide.data.TokenRepository
import com.project.googleloginguide.google.GoogleLogin
import com.project.googleloginguide.util.GoogleLoginState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val tokenRepository: TokenRepository,
    private val googleLogin: GoogleLogin
) : ViewModel() {
    private val _sideEffect = MutableSharedFlow<GoogleLoginSideEffect>(extraBufferCapacity = 1)
    val sideEffect = _sideEffect.asSharedFlow()

    fun googleLogin() {
        viewModelScope.launch {
            googleLogin.check { state ->
                when (state) {
                    is GoogleLoginState.Success -> {
                        //TODO 서버에 토큰 전송

                    }

                    is GoogleLoginState.Fail -> {
                        _sideEffect.tryEmit(GoogleLoginSideEffect.LoginFail(state.message))
                    }
                }
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
                return LoginViewModel(
                    tokenRepository,
                    googleLogin
                ) as T
            }
        }
    }
}
