package com.example.myapplication.dagger

import android.app.Application

class App : Application() {
    companion object {
        lateinit var component: DaggerComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerDaggerComponent.factory().create(applicationContext)
    }
}