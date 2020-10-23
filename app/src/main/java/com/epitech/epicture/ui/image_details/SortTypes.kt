package com.epitech.epicture.ui.image_details

/**
 * Comment sort type query parameter
 * @property value String value
 */
enum class SortTypes(val value: String) {
    /**
     * Sort by best comments
     */
    BEST("best"),

    /**
     * Sort by top comments
     */
    TOP("top"),

    /**
     * Sort by newest comments
     */
    NEW("new")
}
