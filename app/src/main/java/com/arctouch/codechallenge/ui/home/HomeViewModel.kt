package com.arctouch.codechallenge.ui.home

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.injection.module.RetrofitModule
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.SingleLiveEvent
import com.arctouch.codechallenge.util.schedulers.BaseScheduler
import javax.inject.Inject

class HomeViewModel
@Inject
constructor(
        private val scheduler: BaseScheduler,
        private val tmdbApi: TmdbApi
) : ViewModel() {

    //Events
    val updateHomeListEvent = SingleLiveEvent<Unit>()

    val moviesWithGenres = ObservableArrayList<Movie>()
    val topRatedMovies = ObservableArrayList<Movie>()
    var currentPage: Long = 1L

    init {
        getGenres()
    }

    private fun getGenres() {
        tmdbApi.genres(RetrofitModule.API_KEY, RetrofitModule.DEFAULT_LANGUAGE)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe {
                    Cache.cacheGenres(it.genres)
                    findUpcomingMovies(1)
                    findTopRatedMovies(1)
                }
    }

    private fun findUpcomingMovies(page: Long) {
        tmdbApi.upcomingMovies(page)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe {
                    moviesWithGenres.addAll(it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    })
                    updateHomeListEvent.call()
                }
    }

    private fun findTopRatedMovies(page: Long) {
        tmdbApi.topRatedMovies(page)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe {
                    topRatedMovies.addAll(it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    })
                    updateHomeListEvent.call()
                }
    }
}