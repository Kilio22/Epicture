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

    private var qAny = ""

    @Bindable
    fun getQAny(): String {
        return this.query
    }

    fun setQAny(value: String) {
        this.query = value
    }

    private var qExactly = ""

    @Bindable
    fun getQExactly(): String {
        return this.query
    }

    fun setQExactly(value: String) {
        this.query = value
    }
}