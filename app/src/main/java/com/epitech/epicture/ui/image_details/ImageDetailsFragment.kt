package com.epitech.epicture.ui.image_details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.epitech.epicture.HomeActivityData
import com.epitech.epicture.R
import com.epitech.epicture.databinding.FragmentImageDetailsBinding
import com.epitech.epicture.service.ImgurService
import kotlinx.coroutines.launch

class ImageDetailsFragment : Fragment() {

    private lateinit var viewModel: ImageDetailsViewModel
    private lateinit var binding: FragmentImageDetailsBinding

    private var imageId = ""
    private var isFav = false
    private var isAlbum = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val arguments = ImageDetailsFragmentArgs.fromBundle(requireArguments())
        val viewModelFactory = ImageDetailsViewModelFactory(arguments.selectedImage, getString(R.string.comment_count))
        viewModel = ViewModelProvider(this, viewModelFactory).get(ImageDetailsViewModel::class.java)
        binding = FragmentImageDetailsBinding.inflate(layoutInflater, container, false)
        imageId = arguments.selectedImage.id!!
        isFav = arguments.selectedImage.isFavorite
        isAlbum = arguments.selectedImage.isAlbum

        viewModel.voteStatus.observe(viewLifecycleOwner, { newStatus: VoteStatus ->
            updateVoteStatus(imageId, newStatus)
        })
        viewModel.isFav.observe(viewLifecycleOwner, { favStatus: Boolean ->
            if (isFav != favStatus)
                updateFavStatus(imageId)
            isFav = favStatus
        })
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    private fun updateFavStatus(id: String) {
        lifecycleScope.launch {
            try {
                if (isAlbum) {
                    ImgurService.favAlbum(
                        HomeActivityData.imgurCredentials?.accessToken ?: "",
                        id
                    )
                } else {
                    ImgurService.favImage(
                        HomeActivityData.imgurCredentials?.accessToken ?: "",
                        id
                    )
                }
            } catch (exception: Exception) {
                println(exception)
            }
        }
    }

    private fun updateVoteStatus(id: String, newStatus: VoteStatus) {
        lifecycleScope.launch {
            try {
                ImgurService.vote(
                    HomeActivityData.imgurCredentials?.accessToken ?: "",
                    id,
                    newStatus.value
                )
            } catch (exception: Exception) {
                println(exception)
            }
        }
    }
}
