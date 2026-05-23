package com.mmetzner.vehiclemaintenance.core.auth

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class InMemoryAuthTokenStore : AuthTokenStore {
    private val mutex = Mutex()
    private var tokens: AuthTokens? = null

    override suspend fun getTokens(): AuthTokens? = mutex.withLock {
        tokens
    }

    override suspend fun saveTokens(tokens: AuthTokens) {
        mutex.withLock {
            this.tokens = tokens
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            tokens = null
        }
    }
}
