package com.epitech.epicture.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.epitech.epicture.R
import com.epitech.epicture.databinding.FragmentFavoritesBinding
import com.epitech.epicture.ui.ImageGridAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

/**
 * Favorite fragment
 */
class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: FavoritesViewModel
    private val adapter = ImageGridAdapter(ImageGridAdapter.ClickListener {
        viewModel.selectImage(it)
    })
    private var searchJob: Job? = null

    /**
     * Creates fragment
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)

        viewModel.selectedImage.observe(viewLifecycleOwner, { selectedImage ->
            selectedImage?.let {
                this.findNavController().navigate(FavoritesFragmentDirections.actionNavigationFavoritesToImageDetailsFragment(it.id))
                viewModel.selectImageDone()
            }
        })

        binding.lifecycleOwner = this
        binding.favoritesList.adapter = adapter
        initSpinner()
        initSearch()
        return binding.root
    }

    /**
     * Initializes search
     */
    private fun initSearch() {
        lifecycleScope.launch {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.favoritesList.scrollToPosition(0) }
        }
    }

    /**
     * Initializes spinner
     */
    private fun initSpinner() {
        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.fav_sort_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.favSortBySpinner.adapter = adapter
        }
        binding.favSortBySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val stringArray = resources.getStringArray(R.array.fav_sort_array)
                    viewModel.setSort(stringArray[position])
                    search()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    viewModel.setSort("newest")
                    search()
                }
            }
    }

    /**
     * Fetches favorites depending on query string
     */
    private fun search() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchFavorites(viewModel.sort.value ?: "newest").collectLatest {
                adapter.submitData(it)
            }
        }
    }
}
