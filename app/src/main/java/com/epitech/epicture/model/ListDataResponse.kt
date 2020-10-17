package com.epitech.epicture.model

/**
 * Data wrapper used when receiving a list of T object
 */
data class ListDataResponse<T>(val data: List<T>)