package com.example.effectivemobiletest.shared.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

object DI {
    fun init(appContext: Context) {
        startKoin {
            androidContext(appContext)
            modules(
                dataModule,
                viewModelsModule
            )
        }
    }
}