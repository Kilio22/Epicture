package com.epitech.epicture.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.epitech.epicture.R
import com.epitech.epicture.databinding.FragmentSearchBinding
import com.epitech.epicture.ui.favorites.FavoriteImageGridAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchBaseObservable: SearchBaseObservable
    private val adapter = FavoriteImageGridAdapter(FavoriteImageGridAdapter.ClickListener {
    })
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)
        searchBaseObservable = SearchBaseObservable()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.lifecycleOwner = this
        binding.searchList.adapter = adapter
        binding.baseObservable = this.searchBaseObservable
        initSearch()
        return binding.root
    }

    private fun initSearch() {
        binding.queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateImageListFromQuery()
                true
            } else {
                false
            }
        }
        binding.queryInput.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateImageListFromQuery()
                true
            } else {
                false
            }
        }
        updateImageListFromQuery()
        lifecycleScope.launch {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.searchList.scrollToPosition(0) }
        }
    }

    private fun updateImageListFromQuery() {
        searchBaseObservable.getQuery().trim().let {
            if (it.isNotEmpty()) {
                search(it)
            } else {
                adapter.submitData(lifecycle, PagingData.empty())
            }
        }
    }

    private fun search(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            searchViewModel.simpleSearch(query).collectLatest {
                adapter.submitData(it)
            }
        }
    }
}