package com.project.googleloginguide.google

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.project.googleloginguide.data.TokenRepository
import com.project.googleloginguide.util.GoogleLoginState
import com.project.googleloginguide.util.LoginState
import java.security.SecureRandom

class GoogleLogin(
    private val context: Context,
) {
    private val credentialManager = CredentialManager.Companion.create(context)

    private val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(WEB_CLIENT_ID)
        .setAutoSelectEnabled(true)
        .setNonce(generateSecureRandomNonce())
        .build()

    private val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    suspend fun check(
        loginState: (GoogleLoginState) -> Unit,
    ){
        try {
            val result = credentialManager.getCredential(
                request = request,
                context = context,
            )
            handleSign(result, loginState)
        } catch (e: GetCredentialException) {
            // Handle failures
            Log.e(TAG, "Handle failures: ${e.message}")
        }
    }

    private fun generateSecureRandomNonce(byteLength: Int = 32): String {
        val randomBytes = ByteArray(byteLength)
        SecureRandom().nextBytes(randomBytes)
        return Base64.encodeToString(
            randomBytes,
            Base64.NO_WRAP or Base64.URL_SAFE or Base64.NO_PADDING
        )
    }

    private fun handleSign(result: GetCredentialResponse, loginState: (GoogleLoginState) -> Unit) {
        // Handle the successfully returned credential.
        val credential = result.credential

        when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract the ID for server-side validation.
                        val googleIdTokenCredential = GoogleIdTokenCredential.Companion
                            .createFrom(credential.data)

                        Log.e(TAG, "googleIdTokenCredential: ${googleIdTokenCredential.idToken}")

                        loginState(GoogleLoginState.Success(googleIdTokenCredential))
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                        loginState(GoogleLoginState.Fail(e.message ?: "Received an invalid google id token response"))
                    }
                } else {
                    // Catch any unrecognized credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                    loginState(GoogleLoginState.Fail("Unexpected type of credential"))
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

    companion object {
        private const val TAG = "Google Login"
    }
}
