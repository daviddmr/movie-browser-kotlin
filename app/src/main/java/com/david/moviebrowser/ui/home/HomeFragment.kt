package com.david.moviebrowser.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.david.moviebrowser.R
import com.david.moviebrowser.databinding.FragmentHomeBinding
import com.david.moviebrowser.model.Movie
import com.david.moviebrowser.ui.BaseFragment
import com.david.moviebrowser.util.observeEvent
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment(), SearchView.OnQueryTextListener,
    MenuItem.OnActionExpandListener {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        binding.rvTopRatedMovies.addOnScrollListener(onScrollTopRatedMoviesListener())
        binding.rvQueriedMovies.addOnScrollListener(onScrollQueriedMoviesListener())

        (activity as? AppCompatActivity)?.let { activity ->
            activity.setSupportActionBar(binding.toolbar)
            activity.supportActionBar?.let { actionBar ->
                actionBar.title = getString(R.string.home_title)
            }
        }

        subscriber()
    }

    private fun subscriber() {
        viewModel.message.observeEvent(this) { message ->
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
        }

        viewModel.openMovieDetailActEvent.observeEvent(this) { movie ->
            navigateToMovieDetails(movie)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_movie_menu, menu)

        val searchItem = menu.findItem(R.id.search_movie_menu_item_filter)
        val searchView = searchItem.actionView as SearchView
        val closeButton =
            searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)

        searchView.setOnQueryTextListener(this)
        closeButton.setOnClickListener(onCloseButtonSearchViewListener(searchView))
        searchItem.setOnActionExpandListener(this)
        searchView.maxWidth = Integer.MAX_VALUE

        if (viewModel.isSearchViewExpanded.get()) {
            searchItem.expandActionView()
            searchView.setQuery(viewModel.textToQueryMovie.get(), false)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun navigateToMovieDetails(movie: Movie) {
        findNavController().navigate(
            HomeFragmentDirections.goToMovieDetails(movie)
        )
    }

    //Listeners
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
                val mLinearLayoutManager =
                    binding.rvTopRatedMovies.layoutManager as LinearLayoutManager
                viewModel.checkIfListItIsOverAndFindTopRatedMovies(mLinearLayoutManager)
            }
        }
    }

    private fun onScrollQueriedMoviesListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val mLinearLayoutManager =
                    binding.rvQueriedMovies.layoutManager as LinearLayoutManager
                viewModel.checkIfListItIsOverAndFindQueriedMovies(mLinearLayoutManager)
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query?.isNotEmpty() == true) {
            viewModel.submitSearchQuery(query)
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onMenuItemActionExpand(item: MenuItem): Boolean {
        viewModel.updateSearchViewExpandedState(true)
        return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
        viewModel.updateSearchViewExpandedState(false)
        return true
    }
}
