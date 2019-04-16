package com.arctouch.codechallenge.injection.module

import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.home.HomeActivity
import com.arctouch.codechallenge.splash.SplashActivity
import com.arctouch.codechallenge.ui.movieDetail.MovieDetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
interface ActivityBuildersModule {

    @ContributesAndroidInjector
    fun contributeBaseActivity(): BaseActivity

    @ContributesAndroidInjector
    fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    fun contributeHomeActivity(): HomeActivity

    @ContributesAndroidInjector
    fun contributeMovieDetailActivity(): MovieDetailActivity

}