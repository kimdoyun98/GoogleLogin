package com.project.googleloginguide.util

import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

sealed interface LoginState {
    object Idle: LoginState
    object LogOut: LoginState
    object LogIn: LoginState
}

sealed interface GoogleLoginState{
    data class Success(val token: GoogleIdTokenCredential): GoogleLoginState
    data class Fail(val message: String): GoogleLoginState
}