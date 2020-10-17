package com.epitech.epicture.model

/**
 * Basic data wrapper used when receiving only one T object
 */
data class BasicDataResponse<T>(val data: T)