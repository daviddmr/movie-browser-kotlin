package com.arctouch.codechallenge.injection.module

import com.arctouch.codechallenge.util.schedulers.BaseScheduler
import com.arctouch.codechallenge.util.schedulers.SchedulerProvider
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