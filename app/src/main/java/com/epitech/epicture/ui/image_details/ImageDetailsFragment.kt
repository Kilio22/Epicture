package com.epitech.epicture.ui.image_details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.epitech.epicture.databinding.FragmentImageDetailsBinding

class ImageDetailsFragment : Fragment() {

    private lateinit var viewModel: ImageDetailsViewModel
    private lateinit var binding: FragmentImageDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val arguments = ImageDetailsFragmentArgs.fromBundle(requireArguments())
        val viewModelFactory = ImageDetailsViewModelFactory(arguments.selectedImage)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ImageDetailsViewModel::class.java)
        binding = FragmentImageDetailsBinding.inflate(layoutInflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }
}
