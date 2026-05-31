package com.mmetzner.vehiclemaintenance.core.di

import com.mmetzner.vehiclemaintenance.feature.auth.data.AuthRepositoryImpl
import com.mmetzner.vehiclemaintenance.feature.auth.data.remote.AuthRemoteDataSource
import com.mmetzner.vehiclemaintenance.feature.auth.domain.repository.AuthRepository
import com.mmetzner.vehiclemaintenance.feature.auth.presentation.login.LoginViewModel
import com.mmetzner.vehiclemaintenance.feature.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single { AuthRemoteDataSource(get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
}
