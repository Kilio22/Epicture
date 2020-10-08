package com.epitech.epicture.service

object AuthService {
    private var credentials: MutableMap<String, String> = mutableMapOf()

    fun getCredentialValueByKey(key: String): String? {
        return this.credentials[key]
    }

    fun setCredentials(newCredentials: MutableMap<String, String>) {
        this.credentials = newCredentials
    }
}
