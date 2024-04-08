package com.david.moviebrowser.ui.movieDetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.david.moviebrowser.R
import com.david.moviebrowser.databinding.FragmentMovieDetailBinding
import com.david.moviebrowser.model.Movie
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : AppCompatActivity() {

    private lateinit var binding: FragmentMovieDetailBinding
    lateinit var movie: Movie

    companion object {
        const val ARG_MOVIE = "arg_movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_movie_detail)

        intent.getParcelableExtra<Movie>(ARG_MOVIE)?.let {
            movie = it
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
