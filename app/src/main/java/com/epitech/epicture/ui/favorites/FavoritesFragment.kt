package com.epitech.epicture.ui.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.epitech.epicture.R
import com.epitech.epicture.databinding.FragmentFavoritesBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: FavoritesViewModel
    private val adapter = FavoriteImageGridAdapter()
    private var searchJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)

        binding.lifecycleOwner = this
        binding.favoritesList.adapter = adapter
        initSearch()
        return binding.root
    }

    private fun initSearch() {
        search()
        lifecycleScope.launch {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.favoritesList.scrollToPosition(0) }
        }
    }

    private fun search() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchFavorites().collectLatest {
                adapter.submitData(it)
            }
        }
    }
}
