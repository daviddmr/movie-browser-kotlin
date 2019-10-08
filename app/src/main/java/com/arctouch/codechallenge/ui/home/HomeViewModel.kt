package com.arctouch.codechallenge.ui.home

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.support.v7.widget.LinearLayoutManager
import com.arctouch.codechallenge.App
import com.arctouch.codechallenge.BR
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.injection.module.RetrofitModule
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.SingleLiveEvent
import com.arctouch.codechallenge.util.SnackbarMessage
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
    val moviesBinding: ItemBinding<Movie> = ItemBinding
            .of<Movie>(BR.movie, R.layout.movie_item)
            .bindExtra(BR.listener, this)

    val topRatedMovies: ObservableList<Movie> = ObservableArrayList()
    val queriedMovies: ObservableList<Movie> = ObservableArrayList()
    val upcomingMovies: ObservableList<Movie> = ObservableArrayList()

    //Events
    val openMovieDetailActEvent = SingleLiveEvent<Movie>()
    val message = SnackbarMessage()

    //Actions
    private var isLastPageOfTopRatedMovies = ObservableBoolean()
    private var isLastPageOfQueriedMovies = ObservableBoolean()
    private var isLastPageOfUpcomingMovies = ObservableBoolean()
    var loadingMovies = ObservableBoolean()
    var isSearchViewExpanded = ObservableBoolean()

    //Observables
    val textToQueryMovie = ObservableField<String>()

    //Local
    private var currentPageTopRatedMovies: Long = 1L
    private var currentPageQueriedMovies: Long = 1L
    private var currentPageUpcomingMovies: Long = 1L

    init {
        getGenres()
    }

    //Requests
    private fun getGenres() {
        loadingMovies.set(true)
        tmdbApi.genres(RetrofitModule.API_KEY, RetrofitModule.DEFAULT_LANGUAGE)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe({
                    Cache.cacheGenres(it.genres)
                    loadingMovies.set(false)
                    findTopRatedMovies(1)
                }, {
                    loadingMovies.set(false)
                    message.value = App.res.getString(R.string.get_genre_error)
                })
    }

    // I didn't use the request find upcoming movies because the result brought only one movie, so I preferred change to request find top rated movies because there were more movies to show.
    private fun findUpcomingMovies(page: Long) {
        loadingMovies.set(true)
        tmdbApi.upcomingMovies(page)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe({
                    if (it.page < it.totalPages) {
                        currentPageUpcomingMovies++
                    } else {
                        isLastPageOfUpcomingMovies.set(true)
                    }
                    upcomingMovies.addAll(Cache.filterMoviesWithGenres(it))
                    loadingMovies.set(false)
                }, {
                    loadingMovies.set(false)
                    message.value = App.res.getString(R.string.get_upcoming_movies_error)
                })
    }

    private fun findTopRatedMovies(page: Long) {
        loadingMovies.set(true)
        tmdbApi.topRatedMovies(page)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe({
                    if (it.page < it.totalPages) {
                        currentPageTopRatedMovies++
                    } else {
                        isLastPageOfTopRatedMovies.set(true)
                    }
                    topRatedMovies.addAll(Cache.filterMoviesWithGenres(it))
                    loadingMovies.set(false)
                }, {
                    loadingMovies.set(false)
                    message.value = App.res.getString(R.string.get_top_rated_movies_error)
                })
    }

    private fun findMoviesByText(page: Long) {
        loadingMovies.set(true)
        textToQueryMovie.get()?.let { query ->
            tmdbApi.findMoviesByText(query, page)
                    .subscribeOn(scheduler.io())
                    .observeOn(scheduler.ui())
                    .subscribe({
                        if (it.page < it.totalPages) {
                            currentPageQueriedMovies++
                        } else {
                            isLastPageOfQueriedMovies.set(true)
                        }
                        queriedMovies.addAll(Cache.filterMoviesWithGenres(it))
                        loadingMovies.set(false)
                    }, {
                        loadingMovies.set(false)
                        message.value = App.res.getString(R.string.get_queried_movies_error)
                    })
        }
    }

    //Public Functions
    fun submitSearchQuery(query: String) {
        textToQueryMovie.set(query)
        findMoviesByText(currentPageQueriedMovies)
    }

    fun checkIfListItIsOverAndFindTopRatedMovies(mLinearLayoutManager: LinearLayoutManager) {
        if (topRatedMovies.size == mLinearLayoutManager.findLastCompletelyVisibleItemPosition() + 1
                && !isLastPageOfTopRatedMovies.get() && !loadingMovies.get()) {
            findTopRatedMovies(currentPageTopRatedMovies)
        }
    }

    fun checkIfListItIsOverAndFindQueriedMovies(mLinearLayoutManager: LinearLayoutManager) {
        if (queriedMovies.size == mLinearLayoutManager.findLastCompletelyVisibleItemPosition() + 1
                && !isLastPageOfQueriedMovies.get() && !loadingMovies.get()) {
            findMoviesByText(currentPageQueriedMovies)
        }
    }

    fun clearQueryTextAndQueriedMoviesList() {
        textToQueryMovie.set("")
        queriedMovies.clear()
    }

    fun updateSearchViewExpandedState(searchViewExpanded: Boolean) {
        isSearchViewExpanded.set(false)
        if (!searchViewExpanded) {
            textToQueryMovie.set("")
            queriedMovies.clear()
        }
    }

    //Override functions
    override fun openMovieDetailAct(movie: Movie) {
        openMovieDetailActEvent.value = movie
    }
}

interface MovieAdapterOnItemClickListener {
    fun openMovieDetailAct(movie: Movie)
}