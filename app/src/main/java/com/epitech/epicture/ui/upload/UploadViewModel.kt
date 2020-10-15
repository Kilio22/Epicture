package com.epitech.epicture.ui.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UploadViewModel : ViewModel() {
    enum class UploadStatus {
        CHOOSE_IMAGE,
        INFORMATIONS,
        UPLOADING
    }

    private val _status = MutableLiveData(UploadStatus.CHOOSE_IMAGE)
    val status: LiveData<UploadStatus>
        get() = _status

    private val _filePath = MutableLiveData<String>()
    val filePath: LiveData<String>
        get() = _filePath

    fun setFilePath(newFilePath: String) {
        this._filePath.value = newFilePath
    }

    fun setStatus(newStatus: UploadStatus) {
        this._status.value = newStatus
    }
}