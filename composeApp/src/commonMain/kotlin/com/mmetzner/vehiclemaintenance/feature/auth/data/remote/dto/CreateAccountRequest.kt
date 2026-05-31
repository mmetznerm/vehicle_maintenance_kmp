package com.mmetzner.vehiclemaintenance.feature.auth.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateAccountRequest(
    @SerialName("fullName") val fullName: String,
    @SerialName("emailOrPhone") val emailOrPhone: String,
    @SerialName("password") val password: String
)
