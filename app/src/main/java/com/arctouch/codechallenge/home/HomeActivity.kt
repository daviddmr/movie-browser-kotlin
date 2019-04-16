package com.arctouch.codechallenge.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.databinding.HomeActivityBinding
import javax.inject.Inject

class HomeActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: HomeViewModel

    private lateinit var binding: HomeActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        setContentView(R.layout.home_activity)
        binding = DataBindingUtil.setContentView(this, R.layout.home_activity)
        binding.viewModel = viewModel

        subscriber()
    }

    private fun subscriber() {
        viewModel.updateHomeListEvent.observe(this, Observer { moviesWithGenres ->
            moviesWithGenres?.let { binding.recyclerView.adapter = HomeAdapter(it) }
            binding.progressBar.visibility = View.GONE
        })
    }
}
