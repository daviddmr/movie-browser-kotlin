package com.arctouch.codechallenge.ui.movieDetail

import android.arch.lifecycle.ViewModel
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.util.schedulers.BaseScheduler
import javax.inject.Inject

class MovieDetailViewModel
@Inject
constructor(scheduler: BaseScheduler,
            tmdbApi: TmdbApi
) : ViewModel() {

}