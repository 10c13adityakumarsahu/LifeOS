package com.example.lifeos

import android.app.Application
import com.example.lifeos.data.AppContainer
import com.example.lifeos.data.DefaultAppContainer

class LifeOSApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
