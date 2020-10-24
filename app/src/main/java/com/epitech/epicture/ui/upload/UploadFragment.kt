package com.epitech.epicture.ui.upload

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.epitech.epicture.HomeActivityData
import com.epitech.epicture.R
import com.epitech.epicture.databinding.FragmentUploadBinding
import com.epitech.epicture.service.ImgurService
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File

/**
 * Upload fragment
 */
class UploadFragment : Fragment() {
    private lateinit var uploadViewModel: UploadViewModel
    private lateinit var uploadBaseObservable: UploadBaseObservable
    private lateinit var binding: FragmentUploadBinding

    /**
     * Create fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        uploadViewModel = ViewModelProvider(this).get(UploadViewModel::class.java)
        uploadBaseObservable = UploadBaseObservable()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_upload, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = uploadViewModel
        binding.baseObservable = uploadBaseObservable
        binding.chooseImageButton.setOnClickListener { this.checkPermission() }
        binding.uploadButton.setOnClickListener { this.uploadImage() }
        return binding.root
    }

    /**
     * Checks if the app has the authorization to access gallery
     */
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            this.getContent.launch("image/*")
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    /**
     * Hides keyboard
     */
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                this.getContent.launch("image/*")
            }
        }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val filePath = this.getImagePath(uri)
                this.uploadViewModel.setFilePath(filePath)
                this.uploadViewModel.setStatus(UploadStatus.INFORMATIONS)
            }
        }

    /**
     * Uploads an images
     */
    private fun uploadImage() {
        view?.hideKeyboard()
        if (this.uploadBaseObservable.getTitle().trim().isEmpty()) {
            binding.titleInputLayout.error = "You must provide a title"
            return
        }
        binding.titleInputLayout.error = null
        this.uploadViewModel.setStatus(UploadStatus.UPLOADING)

        val file = File(this.uploadViewModel.filePath.value ?: "")
        val requestImage: RequestBody =
            file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val requestMultipartImageBody =
            MultipartBody.Part.createFormData("image", file.name, requestImage)
        val requestTitle: RequestBody =
            this.uploadBaseObservable.getTitle().trim()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val requestDescription: RequestBody =
            this.uploadBaseObservable.getDescription().trim()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val requestType: RequestBody =
            "file".toRequestBody("multipart/form-data".toMediaTypeOrNull())

        lifecycleScope.launch {
            try {
                val response = ImgurService.uploadImage(
                    HomeActivityData.imgurCredentials?.accessToken ?: "",
                    requestMultipartImageBody,
                    requestTitle,
                    requestDescription,
                    requestType
                )
                ImgurService.shareImage(
                    HomeActivityData.imgurCredentials?.accessToken ?: "",
                    response.data.id,
                    requestTitle,
                    1,
                    0
                )
                Toast.makeText(requireContext(), "Image uploaded and published", Toast.LENGTH_LONG)
                    .show()
            } catch (exception: Exception) {
                Toast.makeText(requireContext(), "Couldn't upload image", Toast.LENGTH_LONG).show()
                Timber.tag("Upload fragment").e(exception.toString())
            }
            resetFragment()
        }
    }

    /**
     * Gets image real path from given uri
     */
    private fun getImagePath(uri: Uri): String {
        val id = DocumentsContract.getDocumentId(uri).split(":".toRegex()).toTypedArray()[1]
        val cursor = requireContext().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf("_data"), MediaStore.Images.Media._ID + "=?", arrayOf(id), null
        )
        var filePath = ""

        if (cursor != null) {
            val columnIndex: Int = cursor.getColumnIndex("_data")
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex)
            }
            cursor.close()
        }
        return filePath
    }

    /**
     * Resets chosen informations
     */
    private fun resetFragment() {
        uploadViewModel.setStatusAsync(UploadStatus.CHOOSE_IMAGE)
        uploadViewModel.setFilePathAsync("")
        uploadBaseObservable.setDescription("")
        uploadBaseObservable.setTitle("")
    }
}