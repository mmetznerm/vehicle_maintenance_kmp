package com.mmetzner.vehiclemaintenance.core.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun appModules(): List<Module> = listOf(networkModule, vehicleModule, databaseModule)

expect val platformModule: Module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(appModules() + platformModule)
}

fun initKoinIOS() = initKoin {}
