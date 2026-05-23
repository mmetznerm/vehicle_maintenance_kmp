package com.mmetzner.vehiclemaintenance.core.di

import com.mmetzner.vehiclemaintenance.core.auth.AuthTokenStore
import com.mmetzner.vehiclemaintenance.core.auth.InMemoryAuthTokenStore
import org.koin.dsl.module

val authModule = module {
    single<AuthTokenStore> { InMemoryAuthTokenStore() }
}
