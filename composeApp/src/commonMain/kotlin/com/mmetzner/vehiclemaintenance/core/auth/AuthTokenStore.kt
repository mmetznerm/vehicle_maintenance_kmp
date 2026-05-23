package com.mmetzner.vehiclemaintenance.core.auth

interface AuthTokenStore {
    suspend fun getTokens(): AuthTokens?
    suspend fun saveTokens(tokens: AuthTokens)
    suspend fun clear()
}
