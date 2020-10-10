package com.epitech.epicture.service

object AuthService {
    var credentials: MutableMap<String, String> = mutableMapOf()
        get() = field
        set(value) {
            field = value
        }
    var isLogged: Boolean = false
        get() = field
        set(value) {
            field = value
        }
    
    fun getCredentialValueByKey(key: String): String? {
        return this.credentials[key]
    }
}
