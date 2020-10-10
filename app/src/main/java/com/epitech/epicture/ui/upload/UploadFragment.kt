package com.epitech.epicture.ui.upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.epitech.epicture.R
import com.epitech.epicture.databinding.FragmentUploadBinding

class UploadFragment : Fragment() {
    private lateinit var uploadViewModel: UploadViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        uploadViewModel =
            ViewModelProvider(this).get(UploadViewModel::class.java)
        val binding: FragmentUploadBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_upload, container, false)
        return binding.root
    }
}