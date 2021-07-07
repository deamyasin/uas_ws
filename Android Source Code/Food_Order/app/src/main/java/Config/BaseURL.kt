package Config

import com.foodorder.BuildConfig

object BaseURL {

    const val BASE_URL = BuildConfig.BASE_URL
    const val IMG_BASE_URL = BuildConfig.IMG_BASE_URL

    const val IMG_PROFILE_URL = IMG_BASE_URL + "uploads/profile/"
    const val IMG_PRODUCT_URL = IMG_BASE_URL + "uploads/products/"
    const val IMG_CATEGORIES_URL = IMG_BASE_URL + "uploads/categories/crop/small/"
    const val IMG_BANNER_URL = IMG_BASE_URL + "uploads/banners/"

    const val CONTACT_URL = BASE_URL + "index.php/apppages/contact/"
    const val ABOUT_URL = BASE_URL + "index.php/apppages/about/"
    const val POLICY_URL = BASE_URL + "index.php/apppages/policy/"
    const val TNC_URL = BASE_URL + "index.php/apppages/tnc/"


    const val ENCRYPTED_PASSWORD = BuildConfig.ENCRYPTED_PASSWORD

    var HEADER_LANG = "english"//dutch,english,arabic

    const val ALLOW_LANGUAGE: Boolean = true

    const val PREFS_NAME = "FoodOrderLoginPrefs"
    const val PERMANENT_PREFS_NAME = "FoodOrderPermanentPrefs"


    const val IS_LOGIN = "isLogin"
    const val KEY_ID = "user_id"
    const val KEY_TYPE_ID = "user_type_id"
    const val KEY_NAME = "user_fullname"
    const val KEY_EMAIL = "user_email"
    const val KEY_MOBILE = "user_phone"
    const val KEY_IMAGE = "user_image"

}