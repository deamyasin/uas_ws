package com.foodorder.ui.webview

import Config.BaseURL
import Dialogs.LoaderDialog
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.foodorder.CommonActivity
import com.foodorder.R
import kotlinx.android.synthetic.main.activity_common_web_view.*

class CommonWebViewActivity : CommonActivity() {

    lateinit var loaderDialog: LoaderDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_web_view)

        val title = intent.getStringExtra("title")
        val url = intent.getStringExtra("url")
        setHeaderTitle(title)

        loaderDialog = LoaderDialog(this)
        loaderDialog.show()

        val webSettings: WebSettings = web_common.getSettings()
        webSettings.javaScriptEnabled = true
        webSettings.setAppCacheEnabled(true)
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.setSupportMultipleWindows(true)
        web_common.webChromeClient = UriWebChromeClient()
        web_common.webViewClient = UriWebViewClient()

        web_common.loadUrl(url + BaseURL.HEADER_LANG)

    }

    inner class UriWebChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            if (newProgress == 100) {
                loaderDialog.dismiss()
            } else {
                if (!this@CommonWebViewActivity.isFinishing && !loaderDialog.isShowing)
                    loaderDialog.show()
            }
        }
    }

    inner class UriWebViewClient : WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            if (!this@CommonWebViewActivity.isFinishing && !loaderDialog.isShowing)
                loaderDialog.show()
        }

        override fun onPageFinished(view: WebView, url: String) {
            loaderDialog.dismiss()
        }
    }

}
