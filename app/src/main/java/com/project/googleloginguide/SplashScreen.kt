package com.project.googleloginguide

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.googleloginguide.login.LoginScreen
import com.project.googleloginguide.util.LoginState

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    factory: ViewModelProvider.Factory,
) {
    val viewModel: GoogleLoginViewModel = viewModel(factory = factory)
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()

    when(loginState){
        is LoginState.Idle -> {
            //TODO 로딩
        }

        is LoginState.LogIn -> {
            //TODO Navigate Main
        }

        is LoginState.LogOut -> {
            LoginScreen()
        }
    }
}
