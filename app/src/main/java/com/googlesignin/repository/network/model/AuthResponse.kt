package com.googlesignin.repository.network.model

data class AuthResponse(
    val access_token: String,
    val expires_in: Int,
    val id_token: String,
    val refresh_token: String,
    val token_type: String
)