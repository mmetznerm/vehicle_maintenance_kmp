package com.mmetzner.vehiclemaintenance.core.di

import com.mmetzner.vehiclemaintenance.core.database.DatabaseBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { DatabaseBuilder(androidContext()) }
}



