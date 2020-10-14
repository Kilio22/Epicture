package com.epitech.epicture.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.epitech.epicture.R
import com.epitech.epicture.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val favoritesViewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentFavoritesBinding>(inflater, R.layout.fragment_favorites, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = favoritesViewModel
        binding.favoritesList.adapter = FavoriteImageGridAdapter()
        return binding.root
    }
}