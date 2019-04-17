package com.arctouch.codechallenge.ui.home

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import com.arctouch.codechallenge.BR
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.injection.module.RetrofitModule
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.SingleLiveEvent
import com.arctouch.codechallenge.util.schedulers.BaseScheduler
import me.tatarka.bindingcollectionadapter2.ItemBinding
import javax.inject.Inject

class HomeViewModel
@Inject
constructor(
        private val scheduler: BaseScheduler,
        private val tmdbApi: TmdbApi
) : ViewModel(), MovieAdapterOnItemClickListener {

    //Binds for view components
    val moviesWithGenres: ObservableList<Movie> = ObservableArrayList()
    val moviesWithGenresBinding: ItemBinding<Movie> = ItemBinding
            .of<Movie>(BR.movie, R.layout.movie_item)
            .bindExtra(BR.listener, this)

    //Events
    val openMovieDetailActEvent = SingleLiveEvent<Movie>()

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
                }
    }

    override fun openMovieDetailAct(movie: Movie) {
        openMovieDetailActEvent.value = movie
    }
}

interface MovieAdapterOnItemClickListener {
    fun openMovieDetailAct(movie: Movie)
}