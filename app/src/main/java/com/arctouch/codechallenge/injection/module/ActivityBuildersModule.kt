package com.arctouch.codechallenge.injection.module

import com.arctouch.codechallenge.ui.home.HomeActivity
import com.arctouch.codechallenge.ui.movieDetail.MovieDetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
interface ActivityBuildersModule {

    @ContributesAndroidInjector
    fun contributeHomeActivity(): HomeActivity

    @ContributesAndroidInjector
    fun contributeMovieDetailActivity(): MovieDetailActivity

}