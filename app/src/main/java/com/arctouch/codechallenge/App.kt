package com.arctouch.codechallenge

import com.arctouch.codechallenge.injection.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out App> {
        return DaggerAppComponent.builder().create(this)
    }

}