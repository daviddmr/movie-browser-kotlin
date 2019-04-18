package com.arctouch.codechallenge.ui.home

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.support.v7.widget.LinearLayoutManager
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
    val moviesBinding: ItemBinding<Movie> = ItemBinding
            .of<Movie>(BR.movie, R.layout.movie_item)
            .bindExtra(BR.listener, this)

    val topRatedMovies: ObservableList<Movie> = ObservableArrayList()
    val queriedMovies: ObservableList<Movie> = ObservableArrayList()
    val upcomingMovies: ObservableList<Movie> = ObservableArrayList()

    //Events
    val openMovieDetailActEvent = SingleLiveEvent<Movie>()

    //Actions
    var isLastPageOfTopRatedMovies = ObservableBoolean()
    var isLastPageOfQueriedMovies = ObservableBoolean()
    var isLastPageOfUpcomingMovies = ObservableBoolean()
    var loadingMovies = ObservableBoolean()
    var isSearchViewExpanded = ObservableBoolean()

    //Observables
    val textToQueryMovie = ObservableField<String>()

    //Local
    var currentPageTopRatedMovies: Long = 1L
    var currentPageQueriedMovies: Long = 1L
    var currentPageUpcomingMovies: Long = 1L

    init {
        getGenres()
    }

    //Requests
    private fun getGenres() {
        tmdbApi.genres(RetrofitModule.API_KEY, RetrofitModule.DEFAULT_LANGUAGE)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe {
                    Cache.cacheGenres(it.genres)
                    findTopRatedMovies(1)
                }
    }

    private fun findUpcomingMovies(page: Long) {
        tmdbApi.upcomingMovies(page)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe {
                    if (it.page < it.totalPages) {
                        currentPageUpcomingMovies++
                    } else {
                        isLastPageOfUpcomingMovies.set(true)
                    }
                    loadingMovies.set(false)
                    upcomingMovies.addAll(Cache.filterMoviesWithGenres(it))
                }
    }

    private fun findTopRatedMovies(page: Long) {
        tmdbApi.topRatedMovies(page)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe {
                    if (it.page < it.totalPages) {
                        currentPageTopRatedMovies++
                    } else {
                        isLastPageOfTopRatedMovies.set(true)
                    }
                    loadingMovies.set(false)
                    topRatedMovies.addAll(Cache.filterMoviesWithGenres(it))
                }
    }

    private fun findMoviesByText(page: Long) {
        textToQueryMovie.get()?.let { query ->
            tmdbApi.findMoviesByText(query, page)
                    .subscribeOn(scheduler.io())
                    .observeOn(scheduler.ui())
                    .subscribe {
                        if (it.page < it.totalPages) {
                            currentPageQueriedMovies++
                        } else {
                            isLastPageOfQueriedMovies.set(true)
                        }
                        loadingMovies.set(false)
                        queriedMovies.addAll(Cache.filterMoviesWithGenres(it))
                    }
        }
    }

    //Public Functions
    fun submitSearchQuery(query: String) {
        textToQueryMovie.set(query)
        loadingMovies.set(true)
        findMoviesByText(currentPageQueriedMovies)
    }

    fun checkIfListItIsOverAndFindTopRatedMovies(mLinearLayoutManager: LinearLayoutManager) {
        if (topRatedMovies.size == mLinearLayoutManager.findLastCompletelyVisibleItemPosition() + 1) {
            if (!isLastPageOfTopRatedMovies.get() && !loadingMovies.get()) {
                loadingMovies.set(true)
                findTopRatedMovies(currentPageTopRatedMovies)
            }
        }
    }

    fun checkIfListItIsOverAndFindQueriedMovies(mLinearLayoutManager: LinearLayoutManager) {
        if (queriedMovies.size == mLinearLayoutManager.findLastCompletelyVisibleItemPosition() + 1) {
            if (!isLastPageOfQueriedMovies.get() && !loadingMovies.get()) {
                loadingMovies.set(true)
                findMoviesByText(currentPageQueriedMovies)
            }
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