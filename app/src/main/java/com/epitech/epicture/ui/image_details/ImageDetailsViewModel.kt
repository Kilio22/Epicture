package com.epitech.epicture.ui.image_details

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epitech.epicture.HomeActivityData
import com.epitech.epicture.LoadingStatus
import com.epitech.epicture.config.Config
import com.epitech.epicture.model.Comment
import com.epitech.epicture.model.GalleryImage
import com.epitech.epicture.service.ImgurService
import kotlinx.coroutines.launch

/**
 * ImageDetailsViewModel contains the observable data used inside ImageDetailsFragment
 */
@SuppressLint("LogNotTimber")
class ImageDetailsViewModel(private val imageId: String, private val commentCountFormat: String) : ViewModel() {

    var image: GalleryImage? = null
        private set

    private val _loadingStatus = MutableLiveData<LoadingStatus>()
    val loadingStatus: LiveData<LoadingStatus>
        get() = _loadingStatus

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    private val _link = MutableLiveData<String>()
    val link: LiveData<String>
        get() = _link

    private val _desc = MutableLiveData<String>()
    val desc: LiveData<String>
        get() = _desc

    private val _commentCount = MutableLiveData<String>()
    val commentCount: LiveData<String>
        get() = _commentCount

    private val _isFav = MutableLiveData<Boolean>()
    val isFav: LiveData<Boolean>
        get() = _isFav

    private var originFavCount = -1
    private val _favs = MutableLiveData<String>()
    val favs: LiveData<String>
        get() = _favs

    private val _isUp = MutableLiveData<Boolean>()
    val isUp: LiveData<Boolean>
        get() = _isUp

    private var originUpCount = -1
    private val _ups = MutableLiveData<String>()
    val ups: LiveData<String>
        get() = _ups

    private val _isDown = MutableLiveData<Boolean>()
    val isDown: LiveData<Boolean>
        get() = _isDown

    private var originDownCount = -1
    private val _downs = MutableLiveData<String>()
    val downs: LiveData<String>
        get() = _downs

    private val _voteStatus = MutableLiveData<VoteStatus>()
    val voteStatus: LiveData<VoteStatus>
        get() = _voteStatus

    private val _sort = MutableLiveData(SortTypes.BEST)
    val sort: LiveData<SortTypes>
        get() = _sort

    private val _commentLoadingStatus = MutableLiveData<LoadingStatus>()
    val commentLoadingStatus: LiveData<LoadingStatus>
        get() = _commentLoadingStatus

    private val _commentList = MutableLiveData<List<Comment>>()
    val commentList: LiveData<List<Comment>>
        get() = _commentList

    init {
        viewModelScope.launch {
            try {
                _loadingStatus.value = LoadingStatus.LOADING
                Log.i("request", "requesting image with id: $imageId")
                fromImage(
                    ImgurService.getImageById(
                        HomeActivityData.imgurCredentials?.accessToken ?: "",
                        imageId
                    ).data
                )
                _loadingStatus.value = LoadingStatus.DONE
            } catch (e: Exception) {
                Log.i("error", e.toString())
                Log.e(null, "${e.message} ${e.localizedMessage}")
                _loadingStatus.value = LoadingStatus.ERROR
            }
        }
    }

    private fun fromImage(image: GalleryImage) {
        this.image = image
        _title.value = image.title ?: ""
        if (_title.value.isNullOrEmpty())
            _title.value = "[Missing or empty title]"
        _link.value = if (image.isAlbum && image.images != null) {
            image.images.first { it.id == image.cover && Config.FORMATS_EXTENSION.containsKey(it.type) }.link
        } else {
            image.link
        }
        _desc.value = image.description ?: ""
        _commentCount.value = commentCountFormat.format(image.commentCount)
        _isFav.value = image.isFavorite
        originFavCount = image.favoriteCount
        if (_isFav.value == true)
            originFavCount -= 1
        _favs.value = image.favoriteCount.toString()
        _isUp.value = image.vote == VoteStatus.UP.value
        originUpCount = image.ups
        _ups.value = image.ups.toString()
        _isDown.value = image.vote == VoteStatus.DOWN.value
        originDownCount = image.downs
        _downs.value = image.downs.toString()
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

    /**
     * Gets comments for the associated image
     * @param sort Comment sort query parameter
     */
    fun getImageComments(sort: SortTypes) {
        viewModelScope.launch {
            _commentLoadingStatus.value = LoadingStatus.LOADING
            try {
                _commentList.value = ImgurService.getComments(
                    HomeActivityData.imgurCredentials?.accessToken ?: "",
                    imageId,
                    sort.value
                ).data
                _commentLoadingStatus.value = LoadingStatus.DONE
            } catch (e: Exception) {
                _commentLoadingStatus.value = LoadingStatus.ERROR
                _commentList.value = ArrayList()
            }
        }
    }

    /**
     * Sets comment sort query parameter
     * @param sort Sort query parameter string
     */
    fun setCommentSort(sort: String) {
        when (sort) {
            SortTypes.TOP.value -> _sort.value = SortTypes.TOP
            SortTypes.NEW.value -> _sort.value = SortTypes.NEW
            else -> _sort.value = SortTypes.BEST
        }
    }

    /**
     * Called when the image's favorite button is clicked
     */
    fun onClickFavorite() {
        if (_isFav.value == true) {
            _isFav.value = false
            _favs.value = originFavCount.toString()
        } else {
            _isFav.value = true
            _favs.value = (originFavCount + 1).toString()
        }
    }

    /**
     * Called when the image's upvote button is clicked
     */
    fun onClickUpvote() {
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

    /**
     * Called when the image's downvote button is clicked
     */
    fun onClickDownvote() {
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
