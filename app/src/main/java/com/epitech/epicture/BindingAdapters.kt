package com.epitech.epicture

import android.view.View
import android.widget.*
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.epitech.epicture.ui.login.LoginViewModel
import com.epitech.epicture.ui.upload.UploadViewModel

@BindingAdapter("textStr")
fun bindTextString(textView: TextView, string: String?) {
    string?.let {
        textView.text = it
    }
}

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

@BindingAdapter("imageFavorite")
fun bindImageFavorite(imageView: ImageView, isFav: Boolean?)
{
    isFav?.let {
        if (isFav)
            imageView.setImageResource(R.drawable.star_favorite)
        else
            imageView.setImageResource(R.drawable.star_favorite_black)
    }
}

@BindingAdapter("imageUpvote")
fun bindImageUpvote(imageView: ImageView, isUp: Boolean?)
{
    isUp?.let {
        if (isUp)
            imageView.setImageResource(R.drawable.ic_baseline_arrow_upward_highlight_24)
        else
            imageView.setImageResource(R.drawable.ic_baseline_arrow_upward_24)
    }
}

@BindingAdapter("imageDownvote")
fun bindImageDownvote(imageView: ImageView, isUp: Boolean?)
{
    isUp?.let {
        if (isUp)
            imageView.setImageResource(R.drawable.ic_baseline_arrow_downward_highlight_24)
        else
            imageView.setImageResource(R.drawable.ic_baseline_arrow_downward_24)
    }
}

@BindingAdapter("chooseImageButtonStatus")
fun chooseImageButtonStatus(
    button: Button,
    status: UploadViewModel.UploadStatus?
) {
    when (status) {
        UploadViewModel.UploadStatus.CHOOSE_IMAGE -> {
            button.visibility = View.VISIBLE
        }
        UploadViewModel.UploadStatus.INFORMATIONS -> {
            button.visibility = View.GONE
        }
        UploadViewModel.UploadStatus.UPLOADING -> {
            button.visibility = View.GONE
        }
    }
}

@BindingAdapter("uploadFieldStatus")
fun uploadFieldStatus(
    imageView: ImageView,
    status: UploadViewModel.UploadStatus?
) {
    when (status) {
        UploadViewModel.UploadStatus.CHOOSE_IMAGE -> {
            imageView.visibility = View.GONE
        }
        UploadViewModel.UploadStatus.INFORMATIONS -> {
            imageView.visibility = View.VISIBLE
        }
        UploadViewModel.UploadStatus.UPLOADING -> {
            imageView.visibility = View.GONE
        }
    }
}

@BindingAdapter("uploadFieldStatus")
fun uploadFieldStatus(
    editText: EditText,
    status: UploadViewModel.UploadStatus?
) {
    when (status) {
        UploadViewModel.UploadStatus.CHOOSE_IMAGE -> {
            editText.visibility = View.GONE
        }
        UploadViewModel.UploadStatus.INFORMATIONS -> {
            editText.visibility = View.VISIBLE
        }
        UploadViewModel.UploadStatus.UPLOADING -> {
            editText.visibility = View.GONE
        }
    }
}

@BindingAdapter("uploadFieldStatus")
fun uploadFieldStatus(
    button: Button,
    status: UploadViewModel.UploadStatus?
) {
    when (status) {
        UploadViewModel.UploadStatus.CHOOSE_IMAGE -> {
            button.visibility = View.GONE
        }
        UploadViewModel.UploadStatus.INFORMATIONS -> {
            button.visibility = View.VISIBLE
        }
        UploadViewModel.UploadStatus.UPLOADING -> {
            button.visibility = View.GONE
        }
    }
}

@BindingAdapter("uploadAnimationStatus")
fun uploadAnimationStatus(
    imageView: RelativeLayout,
    status: UploadViewModel.UploadStatus?
) {
    when (status) {
        UploadViewModel.UploadStatus.CHOOSE_IMAGE -> {
            imageView.visibility = View.GONE
        }
        UploadViewModel.UploadStatus.INFORMATIONS -> {
            imageView.visibility = View.GONE
        }
        UploadViewModel.UploadStatus.UPLOADING -> {
            imageView.visibility = View.VISIBLE
        }
    }
}

@BindingAdapter("loginAnimationStatus")
fun loginAnimationStatus(
    relativeLayout: ProgressBar,
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

@BindingAdapter("loginAnimationStatus")
fun loginAnimationStatus(
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