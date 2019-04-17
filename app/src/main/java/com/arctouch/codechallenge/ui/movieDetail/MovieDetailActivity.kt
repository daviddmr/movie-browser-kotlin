package com.arctouch.codechallenge.ui.movieDetail

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.databinding.MovieDetailActivityBinding
import com.arctouch.codechallenge.model.Movie
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MovieDetailActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var binding: MovieDetailActivityBinding

    companion object {
        const val ARG_MOVIE = "arg_movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieDetailViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.movie_detail_activity)
        binding.viewModel = viewModel

        intent?.let {
            val movie = intent.getParcelableExtra<Movie>(ARG_MOVIE)
        }

        subscriber()
    }

    private fun subscriber() {

    }
}
