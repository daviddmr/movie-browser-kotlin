package com.david.moviebrowser.injection.module

import com.david.moviebrowser.util.schedulers.BaseScheduler
import com.david.moviebrowser.util.schedulers.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [(ViewModelModule::class)])
class AppModule {

    @Provides
    @Singleton
    fun provideScheduler(): BaseScheduler {
        return SchedulerProvider()
    }

}