package com.arctouch.codechallenge.ui.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
        binding.recyclerView.addOnScrollListener(onScrollListener())

        subscriber()
    }

    private fun subscriber() {
        viewModel.openMovieDetailActEvent.observe(this, Observer { movie ->
            movie?.let { openMovieDetailAct(it) }
        })
    }

    private fun onScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val mLinearLayoutManager = binding.recyclerView.layoutManager as LinearLayoutManager
                if (viewModel.upcomingMovies.size == mLinearLayoutManager.findLastCompletelyVisibleItemPosition() + 1) {
                    if (!viewModel.isLastPage.get() && !viewModel.loadingMovies.get()) {
                        viewModel.loadingMovies.set(true)
                        viewModel.findUpcomingMovies(viewModel.currentPage)
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
