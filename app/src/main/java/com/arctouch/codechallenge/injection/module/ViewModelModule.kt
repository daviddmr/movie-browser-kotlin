package com.arctouch.codechallenge.injection.module

import android.arch.lifecycle.ViewModelProvider
import com.arctouch.codechallenge.injection.ViewModelFactory
import dagger.Binds
import dagger.Module


@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}