package com.epitech.epicture.model

/**
 * Data wrapper used when receiving imgur data response as a list
 */
data class ListDataResponse<T>(val data: List<T>)