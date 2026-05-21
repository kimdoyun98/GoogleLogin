package com.project.googleloginguide.login

sealed interface GoogleLoginSideEffect {
    data class LoginFail(val message: String): GoogleLoginSideEffect
}
