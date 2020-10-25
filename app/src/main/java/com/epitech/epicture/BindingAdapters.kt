package com.epitech.epicture

import android.view.View
import android.widget.*
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.epitech.epicture.ui.login.LoginViewModel
import com.epitech.epicture.ui.upload.UploadStatus
import com.google.android.material.textfield.TextInputLayout

/**
 * Loads an image and binds it from a given URL
 * @param imgView The image view
 * @param imgUrlwin The image url
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()

        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.broken_image)
            )
            .into(imgView)
    }
}

/**
 * Checks if a star needs to be displayed or not
 *
 * @param imageView The image view on which a star must be displayed
 * @param isFav True if the star must be displayed, false otherwise
 */
@BindingAdapter("favoriteSrc")
fun bindImageFavorite(imageView: ImageView, isFav: Boolean?) {
    isFav?.let {
        if (isFav)
            imageView.setImageResource(R.drawable.star_favorite)
        else
            imageView.setImageResource(R.drawable.star_favorite_black)
    }
}

/**
 * Checks if the upward arrow must be displayed highlighted or not
 *
 * @param imageView The image view on which the arrow must be displayed
 * @param isUp True if the arrow must be highlighted, false otherwise
 */
@BindingAdapter("upvoteSrc")
fun bindImageUpvote(imageView: ImageView, isUp: Boolean?) {
    isUp?.let {
        if (isUp)
            imageView.setImageResource(R.drawable.ic_baseline_arrow_upward_highlight_24)
        else
            imageView.setImageResource(R.drawable.ic_baseline_arrow_upward_24)
    }
}

/**
 * Checks if the downward arrow must be displayed highlighted or not
 *
 * @param imageView The image view on which the arrow must be displayed
 * @param isDown True if the arrow must be highlighted, false otherwise
 */
@BindingAdapter("downvoteSrc")
fun bindImageDownvote(imageView: ImageView, isDown: Boolean?) {
    isDown?.let {
        if (isDown)
            imageView.setImageResource(R.drawable.ic_baseline_arrow_downward_highlight_24)
        else
            imageView.setImageResource(R.drawable.ic_baseline_arrow_downward_24)
    }
}

/**
 * Checks if the button must be displayed or not depending on the upload status
 *
 * @param button The button
 * @param status The upload status
 */
@BindingAdapter("chooseImageButtonStatus")
fun bindChooseImageButton(
    button: Button,
    status: UploadStatus?
) {
    when (status) {
        UploadStatus.CHOOSE_IMAGE -> {
            button.visibility = View.VISIBLE
        }
        UploadStatus.INFORMATIONS -> {
            button.visibility = View.GONE
        }
        UploadStatus.UPLOADING -> {
            button.visibility = View.GONE
        }
    }
}

/**
 * Checks if the image view must be displayed or not depending on the upload status
 *
 * @param imageView The image view
 * @param status The upload status
 */
@BindingAdapter("uploadStatus")
fun bindUploadTextView(
    imageView: ImageView,
    status: UploadStatus?
) {
    when (status) {
        UploadStatus.CHOOSE_IMAGE -> {
            imageView.visibility = View.GONE
        }
        UploadStatus.INFORMATIONS -> {
            imageView.visibility = View.VISIBLE
        }
        UploadStatus.UPLOADING -> {
            imageView.visibility = View.GONE
        }
    }
}

/**
 * Checks if the TextInputLayout must be displayed or not depending on the upload status
 *
 * @param textInputLayout The TextInputLayout
 * @param status The upload status
 */
@BindingAdapter("uploadStatus")
fun bindUploadTextInputLayout(
    textInputLayout: TextInputLayout,
    status: UploadStatus?
) {
    when (status) {
        UploadStatus.CHOOSE_IMAGE -> {
            textInputLayout.visibility = View.GONE
        }
        UploadStatus.INFORMATIONS -> {
            textInputLayout.visibility = View.VISIBLE
        }
        UploadStatus.UPLOADING -> {
            textInputLayout.visibility = View.GONE
        }
    }
}

/**
 * Checks if the button must be displayed or not depending on the upload status
 *
 * @param button The text input layout
 * @param status The upload status
 */
@BindingAdapter("uploadStatus")
fun bindUploadButton(
    button: Button,
    status: UploadStatus?
) {
    when (status) {
        UploadStatus.CHOOSE_IMAGE -> {
            button.visibility = View.GONE
        }
        UploadStatus.INFORMATIONS -> {
            button.visibility = View.VISIBLE
        }
        UploadStatus.UPLOADING -> {
            button.visibility = View.GONE
        }
    }
}

/**
 * Checks if the upload animation must be displayed or not depending on the upload status
 *
 * @param relativeLayout The upload animation
 * @param status The upload status
 */
@BindingAdapter("uploadStatus")
fun bindUploadAnimation(
    relativeLayout: RelativeLayout,
    status: UploadStatus?
) {
    when (status) {
        UploadStatus.CHOOSE_IMAGE -> {
            relativeLayout.visibility = View.GONE
        }
        UploadStatus.INFORMATIONS -> {
            relativeLayout.visibility = View.GONE
        }
        UploadStatus.UPLOADING -> {
            relativeLayout.visibility = View.VISIBLE
        }
    }
}

/**
 * Checks if the login animation must be displayed or not depending on the login status
 *
 * @param relativeLayout The login animation
 * @param status The login status
 */
@BindingAdapter("loginStatus")
fun bindLoginAnimation(
    relativeLayout: RelativeLayout,
    status: LoginViewModel.LoginStatus?
) {
    when (status) {
        LoginViewModel.LoginStatus.LOADING -> {
            relativeLayout.visibility = View.VISIBLE
        }
        LoginViewModel.LoginStatus.MUST_LOGIN -> {
            relativeLayout.visibility = View.GONE
        }
    }
}

/**
 * Checks if the login button must be displayed or not depending on the login status
 *
 * @param button The button
 * @param status The login status
 */
@BindingAdapter("loginStatus")
fun bindLoginButton(
    button: Button,
    status: LoginViewModel.LoginStatus?
) {
    when (status) {
        LoginViewModel.LoginStatus.LOADING -> {
            button.visibility = View.GONE
        }
        LoginViewModel.LoginStatus.MUST_LOGIN -> {
            button.visibility = View.VISIBLE
        }
    }
}

/**
 * Binds an image on an image view depending on the given image url (local stored images only)
 *
 * @param imgView The image view
 * @param imgUrl The url of the local stored image
 */
@BindingAdapter("chosenImageUrl")
fun bindChosenImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        Glide.with(imgView.context)
            .load(imgUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.broken_image)
            )
            .into(imgView)
    }
}

/**
 * Checks if the TextInputLayout must be displayed or not depending on advanced search status
 *
 * @param textInputLayout The TextInputLayout to display or not
 * @param value True if the layout must be displayed, false otherwise
 */
@BindingAdapter("advancedSearchStatus")
fun bindAdvancedSearchInputLayout(textInputLayout: TextInputLayout, value: Boolean) {
    when (value) {
        true -> textInputLayout.visibility = View.VISIBLE
        false -> textInputLayout.visibility = View.GONE
    }
}

/**
 * Checks if the TextView must be displayed or not depending on advanced search status
 *
 * @param textView The TextView to display or not
 * @param value True if the view must be displayed, false otherwise
 */
@BindingAdapter("advancedSearchStatus")
fun bindAdvancedSearchTextView(textView: TextView, value: Boolean) {
    when (value) {
        true -> textView.visibility = View.VISIBLE
        false -> textView.visibility = View.GONE
    }
}

/**
 * Checks if the Spinner must be displayed or not depending on advanced search status
 *
 * @param spinner The spinner to display or not
 * @param value True if the view must be displayed, false otherwise
 */
@BindingAdapter("advancedSearchStatus")
fun bindAdvancedSearchSpinner(spinner: Spinner, value: Boolean) {
    when (value) {
        true -> spinner.visibility = View.VISIBLE
        false -> spinner.visibility = View.GONE
    }
}

/**
 * Checks if the Button must be displayed or not depending on advanced search status
 *
 * @param button The button to display or not
 * @param value True if the view must be displayed, false otherwise
 */
@BindingAdapter("advancedSearchStatus")
fun bindAdvancedSearchButton(button: Button, value: Boolean) {
    when (value) {
        true -> button.visibility = View.VISIBLE
        false -> button.visibility = View.GONE
    }
}

/**
 * Checks if the loading animation must be displayed or not inside the ImageView depending on the loading status
 *
 * @param imageView The image view
 * @param status The loading status
 */
@BindingAdapter("loadingStatus")
fun bindLoadingAnimation(imageView: ImageView, status: LoadingStatus) {
    when (status) {
        LoadingStatus.LOADING -> {
            imageView.visibility = View.VISIBLE
            imageView.setImageResource(R.drawable.loading_animation)
        }
        LoadingStatus.ERROR -> {
            imageView.visibility = View.VISIBLE
            imageView.setImageResource(R.drawable.ic_connection_error)
        }
        LoadingStatus.DONE -> {
            imageView.visibility = View.GONE
        }
    }
}

/**
 * Checks if the view must be displayed or not depending on loading status
 *
 * @param view The view
 * @param status The loading status
 */
@BindingAdapter("visibleIfDone")
fun bindVisibleIfDone(view: View, status: LoadingStatus) {
    if (status == LoadingStatus.DONE)
        view.visibility = View.VISIBLE
    else
        view.visibility = View.GONE
}
