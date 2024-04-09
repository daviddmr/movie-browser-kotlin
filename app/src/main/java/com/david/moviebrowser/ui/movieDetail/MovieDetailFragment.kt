package com.david.moviebrowser.ui.movieDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.david.moviebrowser.databinding.FragmentMovieDetailBinding
import com.david.moviebrowser.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : BaseFragment() {

    private val args: MovieDetailFragmentArgs by navArgs()

    private lateinit var binding: FragmentMovieDetailBinding

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

        binding.movie = args.movie

        setupActionbar()
    }

    private fun setupActionbar() {
        (activity as? AppCompatActivity)?.let { activity ->
            activity.setSupportActionBar(binding.toolbar)
            activity.supportActionBar?.let { actionBar ->
                actionBar.setDisplayHomeAsUpEnabled(true)
                actionBar.setDisplayShowHomeEnabled(true)
                binding.toolbar.setNavigationOnClickListener { parentFragmentManager.popBackStack() }
            }
        }
    }
}
