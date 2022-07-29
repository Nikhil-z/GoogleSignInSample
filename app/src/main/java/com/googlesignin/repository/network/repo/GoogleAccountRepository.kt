package com.googlesignin.repository.network.repo

import com.googlesignin.repository.network.ApiFactory
import com.googlesignin.repository.network.BaseRepository
import com.googlesignin.repository.network.model.AuthRequest
import com.googlesignin.repository.network.model.AuthResponse


/**
 * @Author: Nikhil
 * @Date: 22,July,2022
 */
class GoogleAccountRepository : BaseRepository() {

    private val auth = ApiFactory.auth

    suspend fun getAuthCode(serverAuthCode: String?): AuthResponse? {
        val authRequest = AuthRequest(code = serverAuthCode ?: "")
        return safeApiCall(
            call = { auth.authAccess(authRequest).await() },
            errorMessage = "Error Fetching authentication"
        )

    }
}