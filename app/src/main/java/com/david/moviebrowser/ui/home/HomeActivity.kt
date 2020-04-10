package com.david.moviebrowser.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.david.moviebrowser.R
import com.david.moviebrowser.databinding.HomeActivityBinding
import com.david.moviebrowser.model.Movie
import com.david.moviebrowser.ui.movieDetail.MovieDetailActivity
import com.david.moviebrowser.util.observeEvent
import com.google.android.material.snackbar.Snackbar
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

        binding.rvTopRatedMovies.addOnScrollListener(onScrollTopRatedMoviesListener())
        binding.rvQueriedMovies.addOnScrollListener(onScrollQueriedMoviesListener())

        subscriber()
    }

    private fun subscriber() {
        viewModel.message.observeEvent(this) { message ->
            Snackbar.make(window.decorView, message, Snackbar.LENGTH_LONG).show()
        }

        viewModel.openMovieDetailActEvent.observeEvent(this) { movie ->
            openMovieDetailAct(movie)
        }
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

        if (viewModel.isSearchViewExpanded.get()) {
            searchItem.expandActionView()
            searchView.setQuery(viewModel.textToQueryMovie.get(), false)
        }

        return true
    }

    private fun openMovieDetailAct(movie: Movie) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.ARG_MOVIE, movie)
        startActivity(intent)
    }

    //Listeners
    private fun onQueryTextListener(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    viewModel.submitSearchQuery(query)
                }
                return false
            }
        }
    }

    private fun onSearchViewCollapseListener(): MenuItem.OnActionExpandListener {
        return object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                viewModel.updateSearchViewExpandedState(true)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                viewModel.updateSearchViewExpandedState(false)
                return true
            }
        }
    }

    private fun onCloseButtonSearchViewListener(searchView: SearchView): View.OnClickListener {
        return View.OnClickListener {
            searchView.setQuery("", false)
            viewModel.clearQueryTextAndQueriedMoviesList()
        }
    }

    private fun onScrollTopRatedMoviesListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val mLinearLayoutManager = binding.rvTopRatedMovies.layoutManager as LinearLayoutManager
                viewModel.checkIfListItIsOverAndFindTopRatedMovies(mLinearLayoutManager)
            }
        }
    }

    private fun onScrollQueriedMoviesListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val mLinearLayoutManager = binding.rvQueriedMovies.layoutManager as LinearLayoutManager
                viewModel.checkIfListItIsOverAndFindQueriedMovies(mLinearLayoutManager)
            }
        }
    }
}
