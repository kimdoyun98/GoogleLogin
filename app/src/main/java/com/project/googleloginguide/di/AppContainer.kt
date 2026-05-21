package com.project.googleloginguide.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.project.googleloginguide.google.GoogleLogin
import com.project.googleloginguide.data.TokenRepository

class AppContainer(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )
    val tokenRepository = TokenRepository(dataStore = context.dataStore)
    val googleLogin = GoogleLogin(context)

    companion object {
        private const val USER_PREFERENCES_NAME = "Test"
    }
}