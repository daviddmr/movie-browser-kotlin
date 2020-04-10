package com.david.moviebrowser.injection.module

import com.david.moviebrowser.ui.home.HomeActivity
import com.david.moviebrowser.ui.movieDetail.MovieDetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
interface ActivityBuildersModule {

    @ContributesAndroidInjector
    fun contributeHomeActivity(): HomeActivity

    @ContributesAndroidInjector
    fun contributeMovieDetailActivity(): MovieDetailActivity

}