package com.epitech.epicture.ui.search

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

/**
 * SearchBaseObservable is used to handle two way databinding inside SearchFragment
 */
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
        return this.qAny
    }

    fun setQAny(value: String) {
        this.qAny = value
    }

    private var qExactly = ""

    @Bindable
    fun getQExactly(): String {
        return this.qExactly
    }

    fun setQExactly(value: String) {
        this.qExactly = value
    }
}