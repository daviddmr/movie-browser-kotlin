package com.arctouch.codechallenge.ui.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
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

        subscriber()
    }

    private fun subscriber() {
        viewModel.updateHomeListEvent.observe(this, Observer {
            binding.recyclerView.adapter = HomeAdapter(viewModel.moviesWithGenres, onMovieClickListener())
            binding.progressBar.visibility = View.GONE
        })
    }

    private fun onMovieClickListener(): View.OnClickListener {
        return View.OnClickListener { v ->
            val position: Int = v.tag as Int
            val movie = viewModel.moviesWithGenres[position]
            openMovieDetailAct(movie)
        }
    }

    private fun openMovieDetailAct(movie: Movie) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.ARG_MOVIE, movie)
        startActivity(intent)
    }
}
