package com.example.draveterinaria.utils

import android.content.Context
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.result.Credentials
import com.auth0.android.provider.WebAuthProvider

fun login(context: Context, account: Auth0, onSuccess: (Credentials) -> Unit) {
    WebAuthProvider.login(account)
        .withScheme("demo")
        .withAudience("https://${account.domain}/userinfo")
        .start(context, object : com.auth0.android.callback.Callback<Credentials, AuthenticationException> {
            override fun onSuccess(result: Credentials) {
                onSuccess(result)
            }

            override fun onFailure(error: AuthenticationException) {
                error.printStackTrace()
            }
        })
}
