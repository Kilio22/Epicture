package com.epitech.epicture.ui.upload

/**
 * Image upload status
 *
 */
enum class UploadStatus {
    /**
     * The user must choose an image
     */
    CHOOSE_IMAGE,

    /**
     * The user must fill images informations fields
     */
    INFORMATIONS,

    /**
     * The image is uploading
     */
    UPLOADING
}