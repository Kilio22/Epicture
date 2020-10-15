package com.epitech.epicture.ui.upload

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class UploadBaseObservable : BaseObservable() {
    private var title = ""

    @Bindable
    fun getTitle(): String {
        return this.title
    }

    fun setTitle(value: String) {
        this.title = value
    }

    private var description = ""

    @Bindable
    fun getDescription(): String {
        return this.description
    }

    fun setDescription(value: String) {
        this.description = value
    }
}