package com.epitech.epicture.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoritesViewModel : ViewModel() {
    private val _text = MutableLiveData<String>()
    val text: LiveData<String>
        get() = _text

    init {
        _text.value = "This is notifications Fragment"
    }
}