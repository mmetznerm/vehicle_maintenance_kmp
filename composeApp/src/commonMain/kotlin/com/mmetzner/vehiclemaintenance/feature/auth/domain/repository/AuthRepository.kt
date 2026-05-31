package com.mmetzner.vehiclemaintenance.feature.auth.domain.repository

interface AuthRepository {
    suspend fun hasActiveSession(): Boolean
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun createAccount(fullName: String, emailOrPhone: String, password: String): Result<Unit>
    suspend fun logout()
}
