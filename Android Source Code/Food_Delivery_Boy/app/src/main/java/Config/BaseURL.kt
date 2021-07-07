package Config

import com.fooddeliveryboy.BuildConfig

object BaseURL {

    /**App Base url for api call*/
    const val IMG_BASE_URL = BuildConfig.IMG_BASE_URL

    const val IMG_PROFILE_URL = IMG_BASE_URL + "uploads/profile/"

    const val ENCRYPTED_PASSWORD = BuildConfig.ENCRYPTED_PASSWORD

    const val ALLOW_LANGUAGE: Boolean = true

    var HEADER_LANG = "english"//dutch,english,arabic

    const val PREFS_NAME = "DeliveryBoyLoginPrefs"
    const val PERMANENT_PREFS_NAME = "DeliveryBoyPermanentPrefs"

    const val IS_LOGIN = "isLogin"
    const val KEY_ID = "delivery_boy_id"
    const val KEY_TYPE_ID = "user_type_id"
    const val KEY_NAME = "boy_name"
    const val KEY_EMAIL = "boy_email"
    const val KEY_MOBILE = "boy_phone"
    const val KEY_IMAGE = "boy_photo"

}