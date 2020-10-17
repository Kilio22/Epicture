package com.epitech.epicture.ui.search

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
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

/**
 * Search fragment
 */
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchBaseObservable: SearchBaseObservable
    private val adapter = FavoriteImageGridAdapter(FavoriteImageGridAdapter.ClickListener {
    })
    private var searchJob: Job? = null

    /**
     * Creates fragment
     */
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
        binding.baseObservable = searchBaseObservable
        binding.viewModel = searchViewModel

        initSwitch()
        initSpinner()
        initSearch()
        return binding.root
    }

    private val onEditorActionListener = TextView.OnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_GO) {
            updateImageListFromQuery()
            true
        } else {
            false
        }
    }

    private val onKeyListener = View.OnKeyListener { _, keyCode, event ->
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            if (event.action == KeyEvent.ACTION_DOWN) {
                updateImageListFromQuery()
            }
            true
        } else {
            false
        }
    }

    /**
     * Initializes search
     */
    private fun initSearch() {
        binding.baseQueryInput.setOnEditorActionListener(onEditorActionListener)
        binding.baseQueryInput.setOnKeyListener(onKeyListener)
        binding.searchButton.setOnClickListener { updateImageListFromQuery() }
        updateImageListFromQuery()
        lifecycleScope.launch {
            adapter.loadStateFlow
                    .distinctUntilChangedBy { it.refresh }
                    .filter { it.refresh is LoadState.NotLoading }
                    .collect { binding.searchList.scrollToPosition(0) }
        }
    }

    /**
     * Makes an HTTP request to imgur api and updates displayed image list
     */
    private fun updateImageListFromQuery() {
        searchBaseObservable.getQuery().trim().let {
            view?.hideKeyboard()
            if (it.isNotEmpty() && searchViewModel.advancedSearch.value != true) {
                simpleSearch(it)
            } else if (it.isNotEmpty()) {
                advancedSearch(it)
            } else {
                adapter.submitData(lifecycle, PagingData.empty())
            }
        }
    }

    /**
     * Hides keyboard
     */
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    /**
     * Makes a simple search
     */
    private fun simpleSearch(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            searchViewModel.simpleSearch(query).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    /**
     * Makes an advanced search
     */
    private fun advancedSearch(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            searchViewModel.advancedSearch(
                    query,
                    searchBaseObservable.getQAny().trim(),
                    searchBaseObservable.getQExactly().trim(),
                    searchViewModel.fileType.value?.trim() ?: "all",
                    searchViewModel.sort.value?.trim() ?: "time"
            ).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    /**
     * Initializes search spinners
     */
    private fun initSpinner() {
        ArrayAdapter.createFromResource(
                this.requireContext(),
                R.array.type_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.fileTypeSpinner.adapter = adapter
        }
        binding.fileTypeSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                    ) {
                        val stringArray = resources.getStringArray(R.array.type_array)
                        searchViewModel.setFileType(stringArray[position])
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        searchViewModel.setFileType("all")
                    }
                }
        ArrayAdapter.createFromResource(
                this.requireContext(),
                R.array.search_sort_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.sortBySpinner.adapter = adapter
        }
        binding.sortBySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                val stringArray = resources.getStringArray(R.array.search_sort_array)
                searchViewModel.setSort(stringArray[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                searchViewModel.setSort("")
            }
        }
    }

    /**
     * Initializes search switch
     */
    private fun initSwitch() {
        binding.advancedSearchSwitch.setOnCheckedChangeListener { _, isChecked ->
            searchViewModel.setAdvancedSearch(isChecked)
        }
        binding.advancedSearchSwitch.isChecked = false
    }
}