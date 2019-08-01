package com.example.myandroidapp

import android.app.Application
import com.example.myandroidapp.di.AppComponent
import com.example.myandroidapp.di.AppModule
import com.example.myandroidapp.di.DaggerAppComponent

class App : Application(){

    override fun onCreate() {
        super.onCreate()
        var appComponent = DaggerAppComponent.builder().application(this).build().getApp(this)

    }

}