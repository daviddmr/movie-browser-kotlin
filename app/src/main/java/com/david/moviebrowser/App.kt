package com.david.moviebrowser

import android.app.Application
import android.content.res.Resources
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        lateinit var res: Resources
    }

    override fun onCreate() {
        super.onCreate()
        res = this.resources
    }
}