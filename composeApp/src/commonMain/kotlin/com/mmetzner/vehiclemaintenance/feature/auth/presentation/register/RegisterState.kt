package com.mmetzner.vehiclemaintenance.feature.auth.presentation.register

data class RegisterState(
    val fullName: String = "",
    val emailOrPhone: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isAuthenticated: Boolean = false
)
