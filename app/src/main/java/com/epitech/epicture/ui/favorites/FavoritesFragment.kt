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

    private lateinit var favoritesViewModel: FavoritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favoritesViewModel =
            ViewModelProvider(this).get(FavoritesViewModel::class.java)
        val binding: FragmentFavoritesBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        val textView: TextView = binding.textNotifications
        favoritesViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return binding.root
    }
}