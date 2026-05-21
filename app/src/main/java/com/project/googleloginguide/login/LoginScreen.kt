package com.project.googleloginguide.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.googleloginguide.R
import com.project.googleloginguide.appContainer

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModel.factory(
            context.appContainer.tokenRepository,
            context.appContainer.googleLogin
        )
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        GoogleLoginButton(
            onClick = viewModel::googleLogin
        )
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect {
            when (it) {
                is GoogleLoginSideEffect.LoginFail -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

@Composable
private fun GoogleLoginButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Image(
        modifier = modifier
            .clip(RoundedCornerShape(32.dp))
            .clickable(onClick = onClick),
        painter = painterResource(R.drawable.android_light_rd_ctn_4x),
        contentDescription = "Google Login",
        contentScale = ContentScale.Fit
    )
}

@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreen()
}
