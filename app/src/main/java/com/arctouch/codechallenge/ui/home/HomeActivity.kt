package com.arctouch.codechallenge.ui.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.databinding.HomeActivityBinding
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.ui.movieDetail.MovieDetailActivity
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class HomeActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: HomeViewModel

    private lateinit var binding: HomeActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.home_activity)
        binding.viewModel = viewModel
        binding.rvUpcomingMovies.addOnScrollListener(onScrollUpcomingMoviesListener())
        binding.rvQueriedMovies.addOnScrollListener(onScrollQueriedMoviesListener())

        subscriber()
    }

    private fun subscriber() {
        viewModel.openMovieDetailActEvent.observe(this, Observer { movie ->
            movie?.let { openMovieDetailAct(it) }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_movie_menu, menu)

        val searchItem = menu.findItem(R.id.search_movie_menu_item_filter)
        val searchView = searchItem.actionView as SearchView
        val closeButton = searchView.findViewById<ImageView>(R.id.search_close_btn)

        searchView.setOnQueryTextListener(onQueryTextListener())
        closeButton.setOnClickListener(onCloseButtonSearchViewListener(searchView))
        searchItem.setOnActionExpandListener(onSearchViewCollapseListener())
        searchView.maxWidth = Integer.MAX_VALUE

        if(viewModel.isSearchViewExpanded.get()) {
            searchItem.expandActionView()
            searchView.setQuery(viewModel.textToQueryMovie.get(), false)
        }

        return true
    }

    private fun onQueryTextListener(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    viewModel.textToQueryMovie.set(query)
                    viewModel.findMoviesByText(viewModel.currentPageQueriedMovies)
                }
                return false
            }
        }
    }

    private fun onSearchViewCollapseListener(): MenuItem.OnActionExpandListener {
        return object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                viewModel.isSearchViewExpanded.set(true)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                viewModel.isSearchViewExpanded.set(false)
                viewModel.textToQueryMovie.set("")
                viewModel.queriedMovies.clear()
                return true
            }
        }
    }

    private fun onCloseButtonSearchViewListener(searchView: SearchView): View.OnClickListener {
        return View.OnClickListener {
            searchView.setQuery("", false)
            viewModel.textToQueryMovie.set("")
            viewModel.queriedMovies.clear()
        }
    }

    private fun onScrollUpcomingMoviesListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val mLinearLayoutManager = binding.rvUpcomingMovies.layoutManager as LinearLayoutManager
                if (viewModel.upcomingMovies.size == mLinearLayoutManager.findLastCompletelyVisibleItemPosition() + 1) {
                    if (!viewModel.isLastPageOfUpcomingMovies.get() && !viewModel.loadingMovies.get()) {
                        viewModel.loadingMovies.set(true)
                        viewModel.findUpcomingMovies(viewModel.currentPageUpcomingMovies)
                    }
                }
            }
        }
    }

    private fun onScrollQueriedMoviesListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val mLinearLayoutManager = binding.rvQueriedMovies.layoutManager as LinearLayoutManager
                if (viewModel.queriedMovies.size == mLinearLayoutManager.findLastCompletelyVisibleItemPosition() + 1) {
                    if (!viewModel.isLastPageOfQueriedMovies.get() && !viewModel.loadingMovies.get()) {
                        viewModel.loadingMovies.set(true)
                        viewModel.findMoviesByText(viewModel.currentPageQueriedMovies)
                    }
                }
            }
        }
    }

    private fun openMovieDetailAct(movie: Movie) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.ARG_MOVIE, movie)
        startActivity(intent)
    }
}
