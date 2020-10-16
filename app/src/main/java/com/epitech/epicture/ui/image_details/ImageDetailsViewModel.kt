package com.epitech.epicture.ui.image_details

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.epitech.epicture.model.Image

@SuppressLint("LogNotTimber")
class ImageDetailsViewModel(private val image: Image, private val commentCountFormat: String) : ViewModel() {

    val title: String
        get() = image.title ?: "[Missing title]"

    val link: String?
        get() = image.link

    val desc: String
        get() = image.description ?: ""

    val commentCount: String
        get () = commentCountFormat.format(image.commentCount)

    private var originFavCount = image.favoriteCount
    private val _favs = MutableLiveData(image.favoriteCount.toString())
    val favs: LiveData<String>
        get() = _favs

    private val _isFav = MutableLiveData(image.isFavorite)
    val isFav: LiveData<Boolean>
        get() = _isFav

    private var originUpCount = image.ups
    private val _isUp = MutableLiveData(image.vote == VoteStatus.UP.value)
    val isUp: LiveData<Boolean>
        get() = _isUp

    private val _ups = MutableLiveData(image.ups.toString())
    val ups: LiveData<String>
        get() = _ups

    private var originDownCount = image.downs
    private val _isDown = MutableLiveData(image.vote == VoteStatus.DOWN.value)
    val isDown: LiveData<Boolean>
        get() = _isDown

    private val _downs = MutableLiveData(image.downs.toString())
    val downs: LiveData<String>
        get() = _downs

    private val _voteStatus = MutableLiveData<VoteStatus>()
    val voteStatus: LiveData<VoteStatus>
        get() = _voteStatus

    init {
        if (_isFav.value == true)
            originFavCount -= 1
        when (image.vote) {
            VoteStatus.UP.value -> {
                _voteStatus.value = VoteStatus.UP
                originUpCount -= 1
            }
            VoteStatus.DOWN.value -> {
                _voteStatus.value = VoteStatus.DOWN
                originDownCount -= 1
            }
            else -> _voteStatus.value = VoteStatus.VETO
        }
    }

    fun onClickFavorite() {
        Log.i("onClickFavorite", "Favorite button clicked.")
        if (_isFav.value == true) {
            _isFav.value = false
            _favs.value = originFavCount.toString()
        } else {
            _isFav.value = true
            _favs.value = (originFavCount + 1).toString()
        }
    }

    fun onClickUpvote() {
        Log.i("onClickUpvote", "Upvote button clicked.")
        if (_isUp.value == true) {
            _isUp.value = false
            _ups.value = originUpCount.toString()
            _voteStatus.value = VoteStatus.VETO
        } else {
            _isUp.value = true
            _ups.value = (originUpCount + 1).toString()
            _voteStatus.value = VoteStatus.UP
        }
        _isDown.value = false
        _downs.value = originDownCount.toString()
    }

    fun onClickDownvote() {
        Log.i("onClickDownvote", "Downvote button clicked.")
        if (_isDown.value == true) {
            _isDown.value = false
            _downs.value = originDownCount.toString()
            _voteStatus.value = VoteStatus.VETO
        } else {
            _isDown.value = true
            _downs.value = (originDownCount + 1).toString()
            _voteStatus.value = VoteStatus.DOWN
        }
        _isUp.value = false
        _ups.value = originUpCount.toString()
    }
}
