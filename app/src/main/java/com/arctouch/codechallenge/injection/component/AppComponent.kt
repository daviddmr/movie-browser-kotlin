package com.arctouch.codechallenge.injection.component

import com.arctouch.codechallenge.App
import com.arctouch.codechallenge.injection.module.ActivityBuildersModule
import com.arctouch.codechallenge.injection.module.AppModule
import com.arctouch.codechallenge.injection.module.RetrofitModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ActivityBuildersModule::class,
    AppModule::class,
    RetrofitModule::class
])
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()

}
