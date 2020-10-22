package com.epitech.epicture.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * LoginViewModel contains the observable data used inside LoginActivity
 */
class LoginViewModel : ViewModel() {
    enum class LoginStatus {
        MUST_LOGIN,
        LOADING
    }

    private val _status = MutableLiveData(LoginStatus.LOADING)
    val status: LiveData<LoginStatus>
        get() = _status

    fun setStatus(newStatus: LoginStatus) {
        this._status.value = newStatus
    }

    fun setStatusAsync(newStatus: LoginStatus) {
        this._status.postValue(newStatus)
    }
}