package com.mmetzner.vehiclemaintenance.core.auth

import platform.Foundation.NSUserDefaults

class IosAuthTokenStore : AuthTokenStore {

    private val defaults = NSUserDefaults.standardUserDefaults

    override suspend fun getTokens(): AuthTokens? {
        val accessToken = defaults.stringForKey(KEY_ACCESS_TOKEN) ?: return null
        val refreshToken = defaults.stringForKey(KEY_REFRESH_TOKEN)

        return AuthTokens(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    override suspend fun saveTokens(tokens: AuthTokens) {
        defaults.setObject(tokens.accessToken, forKey = KEY_ACCESS_TOKEN)

        if (tokens.refreshToken != null) {
            defaults.setObject(tokens.refreshToken, forKey = KEY_REFRESH_TOKEN)
        } else {
            defaults.removeObjectForKey(KEY_REFRESH_TOKEN)
        }
    }

    override suspend fun clear() {
        defaults.removeObjectForKey(KEY_ACCESS_TOKEN)
        defaults.removeObjectForKey(KEY_REFRESH_TOKEN)
    }

    private companion object {
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
    }
}
