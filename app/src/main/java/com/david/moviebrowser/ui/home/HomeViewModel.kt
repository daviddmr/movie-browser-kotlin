package com.david.moviebrowser.ui.home

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.david.moviebrowser.App
import com.david.moviebrowser.BR
import com.david.moviebrowser.R
import com.david.moviebrowser.api.response.Result
import com.david.moviebrowser.data.Cache
import com.david.moviebrowser.model.Movie
import com.david.moviebrowser.repository.MovieRepository
import com.david.moviebrowser.util.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding
import javax.inject.Inject

class HomeViewModel
@Inject
constructor(
        private val movieRepository: MovieRepository
) : ViewModel(), MovieAdapterOnItemClickListener {

    //Binds for view components
    val moviesBinding: ItemBinding<Movie> = ItemBinding
            .of<Movie>(BR.movie, R.layout.movie_item)
            .bindExtra(BR.listener, this)

    val topRatedMovies: ObservableList<Movie> = ObservableArrayList()
    val queriedMovies: ObservableList<Movie> = ObservableArrayList()
    val upcomingMovies: ObservableList<Movie> = ObservableArrayList()

    //Events
    private val _openMovieDetailActEvent = MutableLiveData<Event<Movie>>()
    val openMovieDetailActEvent: LiveData<Event<Movie>>
        get() = _openMovieDetailActEvent

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = _message

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
        val scope = CoroutineScope(Dispatchers.Main)

        scope.launch {
            when (val result = movieRepository.getGenres()) {
                is Result.Success -> {
                    Cache.cacheGenres(result.data.genres)
                    findTopRatedMovies(1)
                }
                is Result.Error -> {
                    _message.value = Event(App.res.getString(R.string.get_genre_error))
                }
            }
            loadingMovies.set(false)
        }
    }

    private fun findUpcomingMovies(page: Long) {
        loadingMovies.set(true)
        val scope = CoroutineScope(Dispatchers.Main)

        scope.launch {
            when (val result = movieRepository.getUpcomingMovies(page)) {
                is Result.Success -> {
                    if (result.data.page < result.data.totalPages) {
                        currentPageUpcomingMovies++
                    } else {
                        isLastPageOfUpcomingMovies.set(true)
                    }
                    upcomingMovies.addAll(Cache.filterMoviesWithGenres(result.data))
                }
                is Result.Error -> {
                    _message.value = Event(App.res.getString(R.string.get_upcoming_movies_error))
                }
            }
            loadingMovies.set(false)
        }
    }

    private fun findTopRatedMovies(page: Long) {
        loadingMovies.set(true)
        val scope = CoroutineScope(Dispatchers.Main)

        scope.launch {
            when (val result = movieRepository.getTopRatedMovies(page)) {
                is Result.Success -> {
                    if (result.data.page < result.data.totalPages) {
                        currentPageTopRatedMovies++
                    } else {
                        isLastPageOfTopRatedMovies.set(true)
                    }
                    topRatedMovies.addAll(Cache.filterMoviesWithGenres(result.data))
                }
                is Result.Error -> {
                    _message.value = Event(App.res.getString(R.string.get_top_rated_movies_error))
                }
            }
            loadingMovies.set(false)
        }
    }

    private fun findMoviesByText(page: Long) {
        loadingMovies.set(true)
        val scope = CoroutineScope(Dispatchers.Main)

        scope.launch {
            textToQueryMovie.get()?.let { query ->
                when (val result = movieRepository.findMoviesByText(query, page)) {
                    is Result.Success -> {
                        if (result.data.page < result.data.totalPages) {
                            currentPageQueriedMovies++
                        } else {
                            isLastPageOfQueriedMovies.set(true)
                        }
                        queriedMovies.addAll(Cache.filterMoviesWithGenres(result.data))
                    }
                    is Result.Error -> {
                        _message.value = Event(App.res.getString(R.string.get_queried_movies_error))
                    }
                }
                loadingMovies.set(false)
            }
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
        _openMovieDetailActEvent.value = Event(movie)
    }
}

interface MovieAdapterOnItemClickListener {
    fun openMovieDetailAct(movie: Movie)
}