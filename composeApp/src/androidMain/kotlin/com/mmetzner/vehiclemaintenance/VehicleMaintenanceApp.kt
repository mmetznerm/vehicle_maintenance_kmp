package com.mmetzner.vehiclemaintenance

import android.app.Application
import com.mmetzner.vehiclemaintenance.core.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class VehicleMaintenanceApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@VehicleMaintenanceApp)
        }
    }
}


