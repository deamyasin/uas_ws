package com.foodorder.ui.checkout

import Config.BaseURL
import Dialogs.LoaderDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.webkit.*
import android.webkit.WebSettings.PluginState
import android.webkit.WebView.WebViewTransport
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.foodorder.BuildConfig
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.OrderModel
import com.foodorder.ui.checkout.PaymentActivity
import com.foodorder.ui.thanks.ThanksActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import utils.ContextWrapper
import utils.LanguagePrefs
import utils.SessionManagement
import utils.observeOnce
import java.io.Serializable
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.HashMap


class PaymentActivity : AppCompatActivity() {

    companion object {
        val TAG = PaymentActivity::class.java.simpleName
    }

    lateinit var loaderDialog: LoaderDialog

    var isSuccess = false

    var webMessage: String = ""

    lateinit var checkoutViewModel: CheckoutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkoutViewModel = ViewModelProvider(this)[CheckoutViewModel::class.java]
        LanguagePrefs(this)
        CommonActivity.changeStatusBarColor(this, true)
        setContentView(R.layout.activity_payment)

        webMessage =
            "<html><body><h3>${resources.getString(R.string.please_wait_until_finish_transaction)}</h3></body></html>"

        val url = intent.getStringExtra("paymentUrl")

        Log.d(TAG, "PaymentUrl:$url")

        loaderDialog = LoaderDialog(this)
        loaderDialog.show()

        val webSettings: WebSettings = wv_payment.getSettings()
        webSettings.javaScriptEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.setSupportMultipleWindows(true)
        wv_payment.webChromeClient = UriWebChromeClient()
        wv_payment.webViewClient = UriWebViewClient()

        wv_payment.loadUrl(url)

    }

    inner class UriWebChromeClient : WebChromeClient() {

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            if (newProgress == 100) {
                loaderDialog.dismiss()
            } else {
                if (!this@PaymentActivity.isFinishing && !loaderDialog.isShowing)
                    loaderDialog.show()
            }
        }
    }

    inner class UriWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            url: String
        ): Boolean {
            view.loadUrl(url)
            checkUrl(view, url)
            return true
        }

        override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
            checkUrl(view, url!!)
            return super.shouldInterceptRequest(view, url)
        }

        override fun onReceivedSslError(
            view: WebView, handler: SslErrorHandler,
            error: SslError
        ) {
            Log.d("onReceivedSslError", "onReceivedSslError")
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            if (!this@PaymentActivity.isFinishing && !loaderDialog.isShowing)
                loaderDialog.show()
        }

        override fun onPageFinished(view: WebView, url: String) {
            if (!this@PaymentActivity.isFinishing)
                loaderDialog.dismiss()
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onReceivedError: ${error.toString()}")
            }
            super.onReceivedError(view, request, error)
        }

    }

    fun checkUrl(view: WebView?, url: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "shouldOverrideUrlLoading: $url")
        }

        if (URLUtil.isNetworkUrl(url)) {
            val p: Pattern = Pattern.compile(".*payment/paymentSuccess.*")
            val m: Matcher = p.matcher(url)
            if (m.matches()) {
                Log.d(TAG, "shouldOverrideUrlLoading: success")
                isSuccess = true
                Handler(Looper.getMainLooper()).post(Runnable {
                    makeGetPaymentData(url)
                })
                wv_payment.loadDataWithBaseURL("", webMessage, "text/html", "UTF-8", "")
            }
        }
    }

    private fun makeGetPaymentData(paymentUrl: String) {
        if (!this@PaymentActivity.isFinishing && !loaderDialog.isShowing)
            loaderDialog.show()

        checkoutViewModel.makeGetPaymentData(paymentUrl)
            .observeOnce(this) { response ->
                loaderDialog.dismiss()
                if (response != null) {
                    val jsonObject = JSONObject(response)
                    val jsonObjectData = jsonObject.getJSONObject("data")
                    val url = jsonObjectData.getString("url")

                    makeGetPaymentDataFinal(url.replace("\\\\", ""))
                }
            }
    }

    private fun makeGetPaymentDataFinal(paymentUrl: String) {
        if (!this@PaymentActivity.isFinishing && !loaderDialog.isShowing)
            loaderDialog.show()

        checkoutViewModel.makeGetPaymentDataFinal(paymentUrl)
            .observeOnce(this) { response ->
                loaderDialog.dismiss()

                if (response != null) {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("responce")) {
                        val jsonObjectData = jsonObject.getJSONObject("data")
                        val gson = Gson()
                        val type = object : TypeToken<OrderModel>() {}.type
                        val orderModel = gson.fromJson<OrderModel>(
                            jsonObjectData.toString(),
                            type
                        )

                        SessionManagement.UserData.setSession(
                            this@PaymentActivity,
                            "cartData",
                            ""
                        )

                        Intent(this@PaymentActivity, ThanksActivity::class.java).apply {
                            putExtra("orderData", orderModel as Serializable)
                            startActivity(this)
                            finish()
                        }
                    }
                }
            }
    }

    override fun attachBaseContext(newBase: Context?) {
        val newLocale = Locale(LanguagePrefs.getLang(newBase!!)!!)
        // .. create or get your new Locale object here.
        val context = ContextWrapper.wrap(newBase, newLocale)
        super.attachBaseContext(context)
    }

}