package com.david.moviebrowser.injection.component

import com.david.moviebrowser.App
import com.david.moviebrowser.injection.module.ActivityBuildersModule
import com.david.moviebrowser.injection.module.AppModule
import com.david.moviebrowser.injection.module.RetrofitModule
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
