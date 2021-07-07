package com.foodorder

import Config.GlobleVariable
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.foodorder.models.ProductModel
import com.foodorder.R
import kotlinx.android.synthetic.main.custom_actionbar.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import utils.ContextWrapper
import utils.LanguagePrefs
import utils.MyBounceInterpolator
import utils.SessionManagement
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created on 17-02-2020.
 */
open class CommonActivity : AppCompatActivity() {

    var menu_like: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguagePrefs(this)
        changeStatusBarColor(this, true)
        if (supportActionBar != null) {
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        }
    }

    fun setHeaderTitle(title: String) {
        val mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar.title = title
            mActionBar.setDisplayShowTitleEnabled(false)
            val mInflater = LayoutInflater.from(this)

            val mCustomView = mInflater.inflate(R.layout.custom_actionbar, null)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.gravity = Gravity.CENTER_VERTICAL
            //mCustomView.layoutParams = layoutParams
            val mTitleTextView = mCustomView.tv_actionbar_title
            mTitleTextView!!.text = title
            mActionBar.customView = mCustomView
            mActionBar.setDisplayShowCustomEnabled(true)
        }
    }

    fun removeToolbarShadow() {
        val mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar.elevation = 0F
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition()
            } else {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        var toast: Toast? = null
        fun showToast(context: Context, message: CharSequence) {
            if (toast != null) {
                toast!!.cancel()
            }
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
            toast!!.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 0)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val linearLayoutMain = LinearLayout(context)
            linearLayoutMain.layoutParams = layoutParams
            linearLayoutMain.setPadding(30, 10, 30, 10)
            val linearLayout = LinearLayout(context)
            linearLayout.layoutParams = layoutParams
            linearLayout.orientation = LinearLayout.HORIZONTAL
            linearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorGreenLight
                )
            )
            linearLayout.setPadding(50, 50, 50, 50)
            val textView = TextView(context)
            textView.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorWhite
                )
            )
            textView.text = message
            linearLayout.addView(textView)
            linearLayoutMain.addView(linearLayout)
            toast!!.view = linearLayoutMain
            toast!!.duration = Toast.LENGTH_LONG
            toast!!.show()
        }

        fun isEmailValid(email: String): Boolean {
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            return email.matches(emailPattern.toRegex())
        }

        fun isPhoneValid(phone: String): Boolean {
            return phone.length >= 8
        }

        fun isPasswordValid(password: String): Boolean {
            return password.length >= 6
        }

        fun isValidPassword(password: String?): Boolean {
            password?.let {
                val passwordMatcher = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$")
                return passwordMatcher.find(password) != null
            } ?: return false
        }


        fun dpToPx(context: Context, dp: Float): Int {
            return Math.round(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dp,
                    context.resources.displayMetrics
                )
            )
        }

        fun spToPx(context: Context, sp: Float): Int {
            return Math.round(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP,
                    sp,
                    context.resources.displayMetrics
                )
            )
        }

        fun changeStatusBarColor(activity: Activity, isLight: Boolean) {
            val w: Window = activity.window
            if (isLight) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    var flags: Int = w.decorView.systemUiVisibility
                    flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    w.decorView.systemUiVisibility = flags
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    var flags: Int = w.decorView.systemUiVisibility
                    flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    w.clearFlags(flags)
                }
            }
        }

        fun setTransparentStatusBar(activity: Activity) {
            val window: Window = activity.window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor =
                    ContextCompat.getColor(activity, android.R.color.transparent)
            }
        }

        /**
         * animType 1=up to down, 2=down to up, 3=left to right, 4=right to left
         * */
        fun runLayoutAnimation(recyclerView: RecyclerView, animType: Int) {

            val animArray = arrayOf(
                R.anim.layout_animation_up_to_down,
                R.anim.layout_animation_down_to_up,
                R.anim.layout_animation_left_to_right,
                R.anim.layout_animation_right_to_left,
                R.anim.layout_animation_fall_down
            )

            val finaPosition = if (animType > animArray.size) {
                animArray.size
            } else {
                animType
            }

            val context = recyclerView.context
            val controller =
                AnimationUtils.loadLayoutAnimation(context, animArray[finaPosition - 1])
            recyclerView.layoutAnimation = controller
            recyclerView.adapter!!.notifyDataSetChanged()
            recyclerView.scheduleLayoutAnimation()
        }

        fun runBounceAnimation(context: Context, view: View) {
            val animation =
                AnimationUtils.loadAnimation(
                    context,
                    R.anim.bounce
                )
            animation.interpolator = MyBounceInterpolator(0.1, 20.0)
            view.startAnimation(animation)
        }

        fun getCurrentDateTime(isDateOnly: Boolean): String {
            val inputPattern = "yyyy-MM-dd HH:mm:ss"
            val inputPattern2 = "yyyy-MM-dd"
            val inputFormat = SimpleDateFormat(inputPattern, GlobleVariable.LOCALE)
            val inputFormat2 = SimpleDateFormat(inputPattern2, GlobleVariable.LOCALE)
            return if (isDateOnly) {
                inputFormat2.format(Date().time)
            } else {
                inputFormat.format(Date().time)
            }
        }

        fun getConvertDate(dateTime: String, step: Int): String {
            val inputPattern = "yyyy-MM-dd"
            val outputPattern1 = "dd-MMM-yyyy"
            val inputFormat = SimpleDateFormat(inputPattern, GlobleVariable.LOCALE)
            val outputFormat1 = SimpleDateFormat(outputPattern1, GlobleVariable.LOCALE)
            return outputFormat1.format(inputFormat.parse(dateTime)!!)
        }

        fun getConvertDateTime(dateTime: String, step: Int): String {
            val inputPattern = "yyyy-MM-dd HH:mm:ss"
            val outputPattern1 = "dd MMM yyyy hh:mm a"
            val outputPattern2 = "dd MMM yyyy"
            val outputPattern3 = "hh:mm a"
            val inputFormat = SimpleDateFormat(inputPattern, GlobleVariable.LOCALE)
            val outputFormat1 = SimpleDateFormat(outputPattern1, GlobleVariable.LOCALE)
            val outputFormat2 = SimpleDateFormat(outputPattern2, GlobleVariable.LOCALE)
            val outputFormat3 = SimpleDateFormat(outputPattern3, GlobleVariable.LOCALE)
            return when (step) {
                1 -> outputFormat1.format(inputFormat.parse(dateTime)!!)
                2 -> outputFormat2.format(inputFormat.parse(dateTime)!!)
                3 -> outputFormat3.format(inputFormat.parse(dateTime)!!)
                else -> outputFormat1.format(inputFormat.parse(dateTime)!!)
            }
        }

        fun getConvertTime(dateTime: String, step: Int): String {
            val inputPattern = "HH:mm:ss"
            val outputPattern1 = "hh:mm a"
            val outputPattern2 = "HH:mm"
            val inputFormat = SimpleDateFormat(inputPattern, GlobleVariable.LOCALE)
            val outputFormat1 = SimpleDateFormat(outputPattern1, GlobleVariable.LOCALE)
            val outputFormat2 = SimpleDateFormat(outputPattern2, GlobleVariable.LOCALE)
            return when (step) {
                1 -> outputFormat1.format(inputFormat.parse(dateTime)!!)
                2 -> outputFormat2.format(inputFormat.parse(dateTime)!!)
                else -> outputFormat1.format(inputFormat.parse(dateTime)!!)
            }
        }

        fun hideShowKeyboard(activity: Activity, hide: Boolean) {
            val imm: InputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            if (view == null) {
                view = View(activity)
            }
            if (hide) {
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            } else {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        fun hideShowKeyboard(activity: Activity, view: View, hide: Boolean) {
            val imm: InputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            if (hide) {
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            } else {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        fun getStringByLanguage(
            context: Context,
            textEN: String?,
            textAR: String?
        ): String? {
            return if (LanguagePrefs.getLang(context).equals("ar")
                && !textAR.isNullOrEmpty()
            ) {
                textAR
            } else {
                return if (textEN.isNullOrEmpty()) {
                    textAR
                } else {
                    textEN
                }
            }
        }

        fun getPriceWithCurrency(price: String): String {
            val context = AppController.instance?.applicationContext!!
            return "$price ${SessionManagement.PermanentData.getSession(context, "currency")}"
        }

        fun getPriceWithCurrency(price: String, isRight: Boolean): String {
            val context = AppController.instance?.applicationContext!!
            return if (isRight) {
                "$price ${SessionManagement.PermanentData.getSession(context, "currency")}"
            } else {
                "${SessionManagement.PermanentData.getSession(context, "currency")} $price"
            }
        }

        fun getPriceFormat(price: String?): String {
            return if (price.isNullOrEmpty()) {
                "0"
            } else {
                String.format(
                    GlobleVariable.LOCALE,
                    "%.2f",
                    price.toDouble()
                ).replace(".00", "")
            }
        }

        // get discount price by discount amount and price
        fun getDiscountPrice(
            discount: String,
            price: String,
            getEffectedprice: Boolean,
            getPointValue: Boolean
        ): Double {
            val discount1 = java.lang.Double.parseDouble(discount)
            val price1 = java.lang.Double.parseDouble(price)
            val discountAmount = discount1 * price1 / 100

            if (getEffectedprice) {
                val effectedPrice = price1 - discountAmount
                return if (!getPointValue) {
                    String.format(GlobleVariable.LOCALE, "%.0f", effectedPrice).toDouble()
                } else {
                    effectedPrice
                }
            } else {
                return if (!getPointValue) {
                    String.format(GlobleVariable.LOCALE, "%.0f", discountAmount).toDouble()
                } else {
                    discountAmount
                }
            }
        }

        fun getCartProductsList(context: Context): ArrayList<ProductModel> {
            val cartModelList = ArrayList<ProductModel>()
            val cartData = SessionManagement.UserData.getSession(context, "cartData")
            if (cartData.isNotEmpty()) {
                val jsonObject = JSONObject(cartData)
                val gson = Gson()
                val typeToken = object : TypeToken<ArrayList<ProductModel>>() {}.type
                cartModelList.addAll(
                    gson.fromJson<ArrayList<ProductModel>>(
                        jsonObject.getString("products"),
                        typeToken
                    )
                )
            }
            return cartModelList
        }

        fun getCartProductsQty(context: Context, productId: String): Int {
            val cartData = SessionManagement.UserData.getSession(context, "cartData")
            if (cartData.isNotEmpty()) {
                val jsonObject = JSONObject(cartData)
                val gson = Gson()
                val typeToken = object : TypeToken<ArrayList<ProductModel>>() {}.type
                val productModelList = gson.fromJson<ArrayList<ProductModel>>(
                    jsonObject.getString("products"),
                    typeToken
                )

                for (productModel in productModelList) {
                    if (productModel.product_id == productId) {
                        return productModel.qty!!.toInt()
                    }
                }
            }
            return 0
        }

        fun getCartDetailByProductId(context: Context, productId: String): ProductModel? {
            val cartModelList = ArrayList<ProductModel>()
            val cartData = SessionManagement.UserData.getSession(context, "cartData")
            if (cartData.isNotEmpty()) {
                val jsonObject = JSONObject(cartData)
                val gson = Gson()
                val typeToken = object : TypeToken<ArrayList<ProductModel>>() {}.type
                cartModelList.addAll(
                    gson.fromJson<ArrayList<ProductModel>>(
                        jsonObject.getString("products"),
                        typeToken
                    )
                )

                for (productModel in cartModelList) {
                    if (productModel.product_id == productId) {
                        return productModel
                    }
                }
            }
            return null
        }

        fun getCartTotalAmount(context: Context): Double {
            val cartData = SessionManagement.UserData.getSession(context, "cartData")
            if (cartData.isNotEmpty()) {
                val jsonObject = JSONObject(cartData)
                return jsonObject.getDouble("total_amount")
            }
            return 0.0
        }

        fun getCartNetAmount(context: Context): Double {
            val cartData = SessionManagement.UserData.getSession(context, "cartData")
            if (cartData.isNotEmpty()) {
                val jsonObject = JSONObject(cartData)
                return jsonObject.getDouble("net_paid_amount")
            }
            return 0.0
        }


    }

    override fun attachBaseContext(newBase: Context?) {
        val newLocale = Locale(LanguagePrefs.getLang(newBase!!)!!)
        // .. create or get your new Locale object here.
        val context = ContextWrapper.wrap(newBase, newLocale)
        super.attachBaseContext(context)
    }

}
