package com.arctouch.codechallenge.ui.movieDetail

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.databinding.MovieDetailActivityBinding
import com.arctouch.codechallenge.model.Movie
import dagger.android.support.DaggerAppCompatActivity

class MovieDetailActivity : DaggerAppCompatActivity() {

    private lateinit var binding: MovieDetailActivityBinding
    lateinit var movie: Movie

    companion object {
        const val ARG_MOVIE = "arg_movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.movie_detail_activity)

        intent?.let {
            movie = intent.getParcelableExtra(ARG_MOVIE)
            binding.movie = movie
        }

        setupActionbar()
    }

    private fun setupActionbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }
}
