package com.david.moviebrowser.injection.module

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(
    ViewModelComponent::class
)
class AppModule