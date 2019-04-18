package com.arctouch.codechallenge

import android.content.res.Resources
import com.arctouch.codechallenge.injection.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App : DaggerApplication() {

    companion object {
        lateinit var res: Resources
    }

    override fun onCreate() {
        super.onCreate()
        res = this.resources
    }

    override fun applicationInjector(): AndroidInjector<out App> {
        return DaggerAppComponent.builder().create(this)
    }

}