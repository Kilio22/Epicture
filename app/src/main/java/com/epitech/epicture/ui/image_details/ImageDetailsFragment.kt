package com.epitech.epicture.ui.image_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.epitech.epicture.HomeActivityData
import com.epitech.epicture.R
import com.epitech.epicture.databinding.FragmentImageDetailsBinding
import com.epitech.epicture.service.ImgurService
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Image details fragment
 */
class ImageDetailsFragment : Fragment() {

    private lateinit var viewModel: ImageDetailsViewModel
    private lateinit var binding: FragmentImageDetailsBinding
    private val adapter = CommentGridAdapter()
    private var searchJob: Job? = null

    /**
     * Creates fragment
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val arguments = ImageDetailsFragmentArgs.fromBundle(requireArguments())
        val viewModelFactory = ImageDetailsViewModelFactory(arguments.imageId, getString(R.string.comment_count))
        viewModel = ViewModelProvider(this, viewModelFactory).get(ImageDetailsViewModel::class.java)
        binding = FragmentImageDetailsBinding.inflate(layoutInflater, container, false)

        binding.detailsFavoriteIc.setOnClickListener {
            viewModel.onClickFavorite()
            updateFavStatus()
        }
        binding.detailsUpvoteIc.setOnClickListener {
            viewModel.onClickUpvote()
            viewModel.voteStatus.value?.let {
                updateVoteStatus(it)
            }
        }
        binding.detailsDownvoteIc.setOnClickListener {
            viewModel.onClickDownvote()
            viewModel.voteStatus.value?.let {
                updateVoteStatus(it)
            }
        }
//        viewModel.voteStatus.observe(viewLifecycleOwner, { newStatus: VoteStatus ->
//            updateVoteStatus(imageId, newStatus)
//        })
//        viewModel.isFav.observe(viewLifecycleOwner, { favStatus: Boolean ->
//            if (isFav != favStatus)
//                updateFavStatus(imageId)
//            isFav = favStatus
//        })
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.detailsComments.adapter = adapter
        binding.detailsComments.addItemDecoration(DividerItemDecoration(binding.detailsComments.context, DividerItemDecoration.VERTICAL))
        search()
        initSpinner()
        return binding.root
    }

    /**
     * Initializes spinner
     */
    private fun initSpinner() {
        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.comment_sort_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.detailsSortBySpinner.adapter = adapter
        }
        binding.detailsSortBySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val stringArray = resources.getStringArray(R.array.comment_sort_array)

                viewModel.setCommentSort(stringArray[position])
                search()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.setCommentSort(SortTypes.BEST.value)
                search()
            }
        }
    }

    /**
     * Starts search depending on query string
     */
    private fun search() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.sort.value?.let {
                viewModel.getImageComments(it)
            }
        }
    }

    /**
     * Updates favorite status of an image / an album
     */
    private fun updateFavStatus() {
        lifecycleScope.launch {
            try {
                Log.i(null, "Fav $id")
                viewModel.image?.let {
                    if (it.isAlbum) {
                        ImgurService.favAlbum(
                            HomeActivityData.imgurCredentials?.accessToken ?: "",
                            it.id
                        )
                    } else {
                        ImgurService.favImage(
                            HomeActivityData.imgurCredentials?.accessToken ?: "",
                            it.id
                        )
                    }
                }
            } catch (exception: Exception) {
                println(exception)
            }
        }
    }

    /**
     * Updates vote status of an image / an album
     */
    private fun updateVoteStatus(newStatus: VoteStatus) {
        lifecycleScope.launch {
            try {
                Log.i(null, "Vote $id: $newStatus")
                viewModel.image?.let {
                    ImgurService.vote(
                        HomeActivityData.imgurCredentials?.accessToken ?: "",
                        it.id,
                        newStatus.value
                    )
                }
            } catch (exception: Exception) {
                println(exception)
            }
        }
    }
}
