package com.epitech.epicture.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.epitech.epicture.R
import com.epitech.epicture.databinding.FragmentHomeBinding
import com.epitech.epicture.ui.ImageGridAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

/**
 * Home fragment
 */
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private val adapter = ImageGridAdapter(ImageGridAdapter.ClickListener {
        viewModel.selectImage(it)
    })
    private var searchJob: Job? = null

    /**
     * Creates fragment
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        viewModel.selectedImage.observe(viewLifecycleOwner, { selectedImage ->
            selectedImage?.let {
                this.findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToImageDetailsFragment(it.id))
                viewModel.selectImageDone()
            }
        })

        binding.lifecycleOwner = this
        binding.uploadList.adapter = adapter
        initSearch()
        return binding.root
    }

    /**
     * Initializes search
     */
    private fun initSearch() {
        search()
        lifecycleScope.launch {
            adapter.loadStateFlow
                    .distinctUntilChangedBy { it.refresh }
                    .filter { it.refresh is LoadState.NotLoading }
                    .collect { binding.uploadList.scrollToPosition(0) }
        }
    }

    /**
     * Starts search
     */
    private fun search() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchUploads().collectLatest {
                adapter.submitData(it)
            }
        }
    }
}