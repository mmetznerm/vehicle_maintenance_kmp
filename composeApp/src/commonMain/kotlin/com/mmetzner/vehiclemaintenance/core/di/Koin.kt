package com.mmetzner.vehiclemaintenance.core.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun appModules(): List<Module> = listOf(networkModule, vehicleModule)

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(appModules())
}

fun initKoinIOS() = initKoin {}