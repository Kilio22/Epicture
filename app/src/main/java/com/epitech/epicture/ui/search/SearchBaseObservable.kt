package com.epitech.epicture.ui.search

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class SearchBaseObservable : BaseObservable() {
    private var query = ""

    @Bindable
    fun getQuery(): String {
        return this.query
    }

    fun setQuery(value: String) {
        this.query = value
    }
}