package com.epitech.epicture.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * UserViewModel contains the observable data used inside UserFragment
 */
class UserViewModel : ViewModel() {
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _avatarUrl = MutableLiveData<String>()
    val avatarUrl: LiveData<String> = _avatarUrl

    fun setUsername(username: String) {
        this._username.value = username
    }

    fun setAvatarUrl(newAvatarUrl: String) {
        this._avatarUrl.value = newAvatarUrl
    }
}