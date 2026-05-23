package com.mmetzner.vehiclemaintenance.core.auth

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String? = null
)
