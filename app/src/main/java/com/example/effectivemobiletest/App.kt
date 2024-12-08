package com.example.effectivemobiletest

import android.app.Application
import android.content.Context
import com.example.effectivemobiletest.shared.di.DI

class App : Application() {

    companion object {
        lateinit var instance: App
            private set

        fun applicationContext(): Context {
            return instance.applicationContext
        }
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        DI.init(this)
    }
}