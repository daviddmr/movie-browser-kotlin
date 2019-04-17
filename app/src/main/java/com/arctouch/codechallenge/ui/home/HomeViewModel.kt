package com.arctouch.codechallenge.ui.home

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.SingleLiveEvent
import com.arctouch.codechallenge.util.schedulers.BaseScheduler
import javax.inject.Inject

class HomeViewModel
@Inject
constructor(
        scheduler: BaseScheduler,
        tmdbApi: TmdbApi
) : ViewModel() {

    //Events
    val updateHomeListEvent = SingleLiveEvent<Unit>()

    val moviesWithGenres = ObservableArrayList<Movie>()

    init {
        tmdbApi.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, 1, TmdbApi.DEFAULT_REGION)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe {
                    moviesWithGenres.addAll(it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    })
                    updateHomeListEvent.call()
                }
    }
}