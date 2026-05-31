package com.mmetzner.vehiclemaintenance.core.network

import com.mmetzner.vehiclemaintenance.BuildConfig

actual fun defaultApiBaseUrl(): String = BuildConfig.API_BASE_URL
