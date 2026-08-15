package com.app.movieapp

import android.app.Application
import com.app.movieapp.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Logs Koin errors and debug info in logcat
            androidLogger(Level.ERROR)
            // Injects the Android application context
            androidContext(this@MyApp)
            // Loads all your defined Koin modules
            modules(appModules)
        }
    }
}