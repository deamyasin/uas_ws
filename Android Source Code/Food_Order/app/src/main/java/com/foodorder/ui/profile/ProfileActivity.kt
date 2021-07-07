package com.foodorder.ui.profile

import Config.BaseURL
import Dialogs.LoaderDialog
import Interfaces.OnEditProfileSave
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.rajesh.imagecompressor.ImageCompressor
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.response.CommonResponse
import com.foodorder.ui.change_reset_password.ChangeResetPasswordActivity
import com.foodorder.ui.otp.OtpActivity
import kotlinx.android.synthetic.main.activity_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import utils.ConnectivityReceiver
import utils.SessionManagement
import java.io.File

class ProfileActivity : CommonActivity(), View.OnClickListener {

    internal var imagefile1: File? = null

    lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        setContentView(R.layout.activity_profile)
        setHeaderTitle(resources.getString(R.string.profile))
        removeToolbarShadow()

        updateView()

        iv_profile_img.setOnClickListener(this)
        iv_profile_edit_full_name.setOnClickListener(this)
        iv_profile_edit_email.setOnClickListener(this)
        iv_profile_edit_phone.setOnClickListener(this)
        btn_profile_change_password.setOnClickListener(this)

        profileViewModel.profileEditLiveData.observe(this, Observer { isUpdate ->
            if (isUpdate) {
                if (ConnectivityReceiver.isConnected) {
                    when (profileViewModel.title) {
                        resources.getString(R.string.first_name) -> {
                            makeUpdateName(profileViewModel.newValue, profileViewModel.newValue2)
                        }
                        resources.getString(R.string.email_address) -> {
                            makeUpdateEmail(profileViewModel.newValue)
                        }
                        resources.getString(R.string.phone_number) -> {
                            makeUpdatePhone(profileViewModel.newValue)
                        }
                    }
                } else {
                    ConnectivityReceiver.showSnackbar(this@ProfileActivity)
                }
            }
        })

    }

    override fun onClick(v: View?) {
        var title = ""
        var value = ""
        when (v?.id) {
            R.id.iv_profile_img -> {
                profileViewModel.checkPremission(this)
            }
            R.id.iv_profile_edit_full_name -> {
                title = resources.getString(R.string.full_name)
                value = SessionManagement.UserData.getSession(this, BaseURL.KEY_NAME)
            }
            R.id.iv_profile_edit_email -> {
                title = resources.getString(R.string.email_address)
                value = SessionManagement.UserData.getSession(this, BaseURL.KEY_EMAIL)
            }
            R.id.iv_profile_edit_phone -> {
                title = resources.getString(R.string.phone_number)
                value = SessionManagement.UserData.getSession(this, BaseURL.KEY_MOBILE)
            }
            R.id.btn_profile_change_password -> {
                Intent(this, ChangeResetPasswordActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        if (title.isNotEmpty()) {
            profileViewModel.showEditDialog(this, title, value)
        }
    }

    private fun updateView() {
        val userImg = SessionManagement.UserData.getSession(this, BaseURL.KEY_IMAGE)
        Glide.with(this)
            .load(BaseURL.IMG_PROFILE_URL + userImg)
            .placeholder(R.color.colorGray)
            .error(R.color.colorGray)
            .into(iv_profile_img)

        tv_profile_full_name.text =
            SessionManagement.UserData.getSession(this, BaseURL.KEY_NAME)
        tv_profile_email.text = SessionManagement.UserData.getSession(this, BaseURL.KEY_EMAIL)
        tv_profile_email2.text = SessionManagement.UserData.getSession(this, BaseURL.KEY_EMAIL)
        tv_profile_phone.text = SessionManagement.UserData.getSession(this, BaseURL.KEY_MOBILE)
        tv_profile_phone2.text = SessionManagement.UserData.getSession(this, BaseURL.KEY_MOBILE)
    }

    private fun makeUpdateName(userFirstName: String, userLastName: String) {
        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)
        params["user_firstname"] = userFirstName
        params["user_lastname"] = userLastName

        val loaderDialog = LoaderDialog(this)
        loaderDialog.show()

        profileViewModel.makeUpdateName(params)
            .observe(this, Observer { response: CommonResponse? ->
                loaderDialog.dismiss()
                if (response != null) {
                    if (response.responce!!) {
                        SessionManagement.UserData.setSession(
                            this,
                            BaseURL.KEY_NAME,
                            "$userFirstName $userLastName"
                        )

                        updateView()
                    } else {
                        CommonActivity.showToast(this, response.message!!)
                    }
                }
            })
    }

    private fun makeUpdateEmail(userEmail: String) {
        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)
        params["user_email"] = userEmail

        val loaderDialog = LoaderDialog(this)
        loaderDialog.show()

        profileViewModel.makeUpdateEmail(params)
            .observe(this, Observer { response: CommonResponse? ->
                loaderDialog.dismiss()
                if (response != null) {
                    if (response.responce!!) {
                        SessionManagement.UserData.setSession(this, BaseURL.KEY_EMAIL, userEmail)
                        updateView()
                    } else {
                        CommonActivity.showToast(this, response.message!!)
                    }
                }
            })
    }

    private fun makeUpdatePhone(userPhone: String) {
        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)
        params["user_phone"] = userPhone

        val loaderDialog = LoaderDialog(this)
        loaderDialog.show()

        profileViewModel.makeUpdatePhone(params)
            .observe(this, Observer { response: CommonResponse? ->
                loaderDialog.dismiss()
                if (response != null) {
                    if (response.responce!!) {
                        Intent(this, OtpActivity::class.java).apply {
                            putExtra("user_phone", userPhone)
                            startActivityForResult(this, 6965)
                        }
                    } else {
                        CommonActivity.showToast(this, response.message!!)
                    }
                }
            })
    }

    private fun makeUpdateImage() {
        val requestFile: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), imagefile1!!)

        // MultipartBody.Part is used to send also the actual file name
        val imageBody: MultipartBody.Part =
            MultipartBody.Part.createFormData("user_image", imagefile1!!.name, requestFile)

        val loaderDialog = LoaderDialog(this)
        loaderDialog.show()

        profileViewModel.makeUploadProfilePicture(
            SessionManagement.UserData.getSession(
                this,
                BaseURL.KEY_ID
            ), imageBody
        ).observe(this,
            Observer { response: CommonResponse? ->
                loaderDialog.dismiss()
                if (response != null) {
                    if (response.responce!!) {
                        CommonActivity.showToast(this, response.message!!)
                        if (response.data != null) {
                            val jsonObject = JSONObject(response.data!!.toString())

                            SessionManagement.UserData.setSession(
                                this,
                                BaseURL.KEY_IMAGE,
                                jsonObject.getString("user_image")
                            )
                        }
                    } else {
                        CommonActivity.showToast(this, response.message!!)
                    }
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    6965 -> updateView()
                    profileViewModel.GALLERY_REQUEST_CODE1 -> {
                        setGalleryImage(data)
                    }
                    profileViewModel.CAMERA_CAPTURE_IMAGE_REQUEST_CODE1 -> {
                        setCameraImage()
                    }
                }
            }
            Activity.RESULT_CANCELED -> {
                when (requestCode) {
                    profileViewModel.CAMERA_CAPTURE_IMAGE_REQUEST_CODE1 -> {
                        // user cancelled Image capture
                        CommonActivity.showToast(this, "User cancelled image capture")
                    }
                }
            }
            else -> {
                when (requestCode) {
                    profileViewModel.CAMERA_CAPTURE_IMAGE_REQUEST_CODE1 -> {
                        // failed to capture image
                        CommonActivity.showToast(this, "Sorry! Failed to capture image")
                    }
                }
            }
        }
    }

    private fun setGalleryImage(data: Intent?) {
        if (data != null) {
            try {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                // Get the cursor
                val cursor = contentResolver.query(
                    selectedImage!!,
                    filePathColumn, null, null, null
                )
                // Move to first row
                cursor!!.moveToFirst()

                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                val imgDecodableString = cursor.getString(columnIndex)

                val file = File(imgDecodableString)
                cursor.close()

                if (file.exists()) {
                    ImageCompressor.compressImage(
                        this@ProfileActivity,
                        file,
                        object : ImageCompressor.OnCompressListener {
                            override fun onCompressCompleted(compressFile: File) {
                                imagefile1 = compressFile

                                Glide.with(this@ProfileActivity)
                                    .load(compressFile)
                                    .placeholder(R.color.colorGray)
                                    .error(R.color.colorGray)
                                    .into(iv_profile_img)

                                makeUpdateImage()
                            }
                        })
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }
    }

    private fun setCameraImage() {
        val file = File(profileViewModel.currentPhotoPath)
        if (file.exists()) {
            ImageCompressor.compressImage(
                this@ProfileActivity,
                file,
                object : ImageCompressor.OnCompressListener {
                    override fun onCompressCompleted(compressFile: File) {
                        imagefile1 = compressFile

                        Glide.with(this@ProfileActivity)
                            .load(compressFile)
                            .placeholder(R.color.colorGray)
                            .error(R.color.colorGray)
                            .into(iv_profile_img)

                        makeUpdateImage()
                    }
                })
        }
    }

}
