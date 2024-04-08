package com.david.moviebrowser.ui.movieDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.david.moviebrowser.databinding.FragmentMovieDetailBinding
import com.david.moviebrowser.model.Movie
import com.david.moviebrowser.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentMovieDetailBinding
    lateinit var movie: Movie

    companion object {
        const val TAG = "MovieDetailFragment"
        const val ARG_MOVIE = "arg_movie"

        fun newInstance(movie: Movie): MovieDetailFragment {
            return MovieDetailFragment().apply {
                val bundle = Bundle()
                bundle.putParcelable(ARG_MOVIE, movie)
                arguments = bundle
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (arguments?.getParcelable(ARG_MOVIE) as? Movie)?.let {
            movie = it
            binding.movie = movie
        }

        setupActionbar()
    }

    private fun setupActionbar() {
//        (activity as AppCompatActivity).apply {
//            setSupportActionBar(binding.toolbar)
//            supportActionBar?.setDisplayHomeAsUpEnabled(true)
//            supportActionBar?.setDisplayShowHomeEnabled(true)
//            binding.toolbar.setNavigationOnClickListener { fragmentManager.popBackStack() }
//        }
    }
}
