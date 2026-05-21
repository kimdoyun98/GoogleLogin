package com.project.googleloginguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.project.googleloginguide.ui.theme.GoogleLoginGuideTheme

class GoogleLoginGuideActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GoogleLoginGuideTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SplashScreen(
                        modifier = Modifier.padding(innerPadding),
                        factory = GoogleLoginViewModel.factory(
                            appContainer.tokenRepository,
                            appContainer.googleLogin
                        )
                    )
                }
            }
        }
    }
}
