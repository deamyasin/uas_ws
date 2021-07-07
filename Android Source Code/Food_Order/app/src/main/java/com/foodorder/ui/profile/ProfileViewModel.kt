package com.foodorder.ui.profile

import Config.BaseURL
import Config.GlobleVariable
import Interfaces.OnEditProfileSave
import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.foodorder.R
import com.foodorder.repository.ProjectRepository
import com.foodorder.response.CommonResponse
import com.foodorder.ui.profile.dialog.BottomSheetEditProfileDialog
import com.foodorder.ui.profile.dialog.ChooseImageDialog
import okhttp3.MultipartBody
import utils.ConnectivityReceiver
import utils.SessionManagement
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
 * Created on 05-05-2020.
 */
class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    val context = application.applicationContext

    val projectRepository = ProjectRepository()

    val CAMERA_CAPTURE_IMAGE_REQUEST_CODE1 = 101
    val GALLERY_REQUEST_CODE1 = 201

    val profileEditLiveData = MutableLiveData<Boolean>()

    fun makeUpdateName(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.updateName(params)
    }

    fun makeUpdateEmail(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.updateEmail(params)
    }

    fun makeUpdatePhone(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.updatePhone(params)
    }

    fun makeUploadProfilePicture(
        userId: String,
        image: MultipartBody.Part
    ): LiveData<CommonResponse?> {
        return projectRepository.uploadProfilePicture(userId, image)
    }

    var title = ""
    var newValue = ""
    var newValue2 = ""

    fun showEditDialog(context: Context, title: String, value: String) {
        this.title = ""
        this.newValue = ""
        this.newValue2 = ""

        val bottomSheetEditProfileDialog =
            BottomSheetEditProfileDialog(object : OnEditProfileSave {
                override fun onSave(title: String, newValue: String, newValue2: String) {
                    this@ProfileViewModel.title = title
                    this@ProfileViewModel.newValue = newValue
                    this@ProfileViewModel.newValue2 = newValue2
                    profileEditLiveData.value = true
                }
            })
        bottomSheetEditProfileDialog.contexts = context
        if (bottomSheetEditProfileDialog.isVisible) {
            bottomSheetEditProfileDialog.dismiss()
        } else {
            val args = Bundle()
            if (title == context.resources.getString(R.string.full_name)) {
                val fullName = SessionManagement.UserData.getSession(context, BaseURL.KEY_NAME)

                val firstLast = fullName.split(" ")

                args.putString("title", context.resources.getString(R.string.first_name))
                args.putString(
                    "value",
                    if (firstLast.size > 0) firstLast[0] else ""
                )
                args.putString("title2", context.resources.getString(R.string.last_name))
                args.putString(
                    "value2",
                    if (firstLast.size > 1) firstLast[1] else ""
                )
            } else {
                args.putString("title", title)
                args.putString("value", value)
            }
            bottomSheetEditProfileDialog.arguments = args
            bottomSheetEditProfileDialog.show(
                (context as FragmentActivity).supportFragmentManager,
                bottomSheetEditProfileDialog.tag
            )
        }

    }

    fun checkPremission(context: Context) {
        Dexter.withActivity(context as Activity)
            .withPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        chooseImageDialog(context)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    fun chooseImageDialog(context: Context) {
        val chooseImageDialog =
            ChooseImageDialog(context, object : ChooseImageDialog.OnClickListener {
                override fun cameraClick() {
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                        // Ensure that there's a camera activity to handle the intent
                        takePictureIntent.resolveActivity(context.packageManager)?.also {
                            // Create the File where the photo should go
                            val photoFile: File? = try {
                                createImageFile()
                            } catch (ex: IOException) {
                                null
                            }

                            // Continue only if the File was successfully created
                            photoFile?.also {
                                val photoURI: Uri = FileProvider.getUriForFile(
                                    context,
                                    context.packageName + ".fileprovider",
                                    it
                                )
                                takePictureIntent.putExtra(
                                    MediaStore.EXTRA_OUTPUT,
                                    photoURI
                                )
                                (context as Activity).startActivityForResult(
                                    takePictureIntent,
                                    CAMERA_CAPTURE_IMAGE_REQUEST_CODE1
                                )
                            }
                        }
                    }


                }

                override fun galleryClick() {
                    val galleryIntent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    galleryIntent.type = "image/*"
                    // Start the Intent
                    (context as Activity).startActivityForResult(
                        galleryIntent,
                        GALLERY_REQUEST_CODE1
                    )
                }

                override fun cancelClick() {
                    //cancelClick
                }
            })
        chooseImageDialog.show()

    }

    var currentPhotoPath: String = ""

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", GlobleVariable.LOCALE).format(Date())
        val storageDir: File = context.cacheDir
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

}