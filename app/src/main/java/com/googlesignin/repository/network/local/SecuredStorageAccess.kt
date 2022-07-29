package com.googlesignin.repository.network.local

import android.content.Context
import com.googlesignin.repository.network.model.AuthResponse
import timber.log.Timber


/**
 * @Author: Nikhil
 * @Date: 25,July,2022
 */
class SecuredStorageAccess(val context: Context) {


    private var sharedPreferences: TinyDB? = null


    private val authToken = "authToken"
    private val refreshToken = "refreshToken"
    private val serverAuthToken = "serverAuthToken"

    fun setAuthInfo(token: AuthResponse?) {
        if (token != null) {
            build().putObject(authToken, token)
        } else {
            Timber.d("Failed to save AuthResponse, Cannot save NULL or EMPTY")
        }


    }

    fun getAuthInfo(): AuthResponse? {
        return build().getObject(authToken, AuthResponse::class.java)
    }

    fun isAuthInfoAvailable(): Boolean {
        return build().objectExists(authToken)
    }

    fun setServerAuthToken(token: String?) {
        if (token != null) {
            build().putString(serverAuthToken, token)
        } else {
            Timber.d("Failed to save auth token, Cannot save NULL or EMPTY")
        }


    }

    fun getServerAuthToken(): String {
        return build().getString(serverAuthToken) ?: ""
    }


    fun setRefreshToken(token: String) {
        build().putString(refreshToken, token)

    }

    fun getRefreshToken(): String {
        return build().getString(refreshToken) ?: ""
    }


    private fun build(): TinyDB {
        if (sharedPreferences == null) {
            sharedPreferences = TinyDB(context)
            Timber.d("Doesn't find any sharedPreferences instance ! Creating new one")
        }
        return sharedPreferences!!
    }

    fun info(): TinyDB {
        return build()
    }

}