package com.epitech.epicture.service

object AuthService {
    private var credentials: MutableMap<String, String> = mutableMapOf()
    var isLogged: Boolean = false
        get() = field
        set(value) {
            field = value
        }

    fun getCredentialValueByKey(key: String): String? {
        return this.credentials[key]
    }

    fun setCredentials(newCredentials: MutableMap<String, String>) {
        this.credentials = newCredentials
    }
}
