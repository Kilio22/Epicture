package com.epitech.epicture

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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

@BindingAdapter("chooseImageButtonStatus")
fun chooseImageButtonStatus(button: Button,
                            status: UploadViewModel.UploadStatus?) {
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

@BindingAdapter("informationFieldsStatus")
fun informationFieldsStatus(imageView: ImageView,
                            status: UploadViewModel.UploadStatus?) {
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

@BindingAdapter("informationFieldsStatus")
fun informationFieldsStatus(editText: EditText,
                            status: UploadViewModel.UploadStatus?) {
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

@BindingAdapter("informationFieldsStatus")
fun informationFieldsStatus(button: Button,
                            status: UploadViewModel.UploadStatus?) {
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

@BindingAdapter("loadingAnimationStatus")
fun loadingAnimationStatus(layout: ImageView,
                           status: UploadViewModel.UploadStatus?) {
    when (status) {
        UploadViewModel.UploadStatus.CHOOSE_IMAGE -> {
            layout.visibility = View.GONE
        }
        UploadViewModel.UploadStatus.INFORMATIONS -> {
            layout.visibility = View.GONE
        }
        UploadViewModel.UploadStatus.UPLOADING -> {
            layout.visibility = View.VISIBLE
        }
    }
}

@BindingAdapter("choosedImageUrl")
fun bindChoosedImage(imgView: ImageView, imgUrl: String?) {
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