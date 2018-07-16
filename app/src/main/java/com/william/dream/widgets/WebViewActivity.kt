package com.william.dream.widgets

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.Toast
import com.william.dream.R
import kotlinx.android.synthetic.main.activity_web_view.*
import java.util.*

class WebViewActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        initWebView()
        initWebSetting()

        wvHome.loadUrl("http://www.baidu.com")
    }


    private fun initWebView() {
        wvHome.requestFocusFromTouch()
        wvHome.addJavascriptInterface(JsBridge(), "android")

        wvHome.webViewClient = WebViewClienter()
        wvHome.webChromeClient = WebChromeClienter()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
    }

    private fun initWebSetting() {
        var settings = wvHome.settings
        settings.javaScriptEnabled = true
        settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
    }

    fun onClick(view: View) {
        when (view.id) {

            R.id.btnLoadUrlByInternet -> {
                wvHome.loadUrl("https://mobile.cib.com.cn/netbank/cn/index.html")
            }
            R.id.btnLoadUrlByLocal -> {
                wvHome.loadUrl("file:///android_asset/web.html")
            }

            R.id.btnCallJS -> {
                val num = Random().nextInt(25)
                wvHome.loadUrl("javascript:nativeCallJs('Hello William - $num')")
            }
            R.id.btnCallJSWithReturnValue -> {
                val num = Random().nextInt(25)
                wvHome.evaluateJavascript("nativeCallJsWithReturnValue($num)") {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
            }

            else -> {

            }
        }
    }


    inner class JsBridge {

        @JavascriptInterface
        fun toastMessage(message: String) {
            Toast.makeText(this@WebViewActivity, message, Toast.LENGTH_SHORT).show()
        }

    }

    inner class WebViewClienter : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

            Log.d("onPageStarted", url)
            return super.shouldOverrideUrlLoading(view, url)
        }

//        override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse {
//            return super.shouldInterceptRequest(view, url)
//
//        }

        override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
            super.onReceivedHttpError(view, request, errorResponse)

        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            // 断网或者网络连接超时
            val errorCode = error?.errorCode
            if (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_CONNECT || errorCode == ERROR_TIMEOUT) {
                Log.e("onReceivedError", """
                    >=23
                    errorCode = $errorCode
                    description = ${error?.description}
                    failingUrl = ${request?.url}
                """.trimIndent())
            }
        }

        override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
            super.onReceivedError(view, errorCode, description, failingUrl)
            // 断网或者网络连接超时
            if (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_CONNECT || errorCode == ERROR_TIMEOUT) {
                Log.e("onReceivedError", """
                    <23
                    errorCode = $errorCode
                    description = $description
                    failingUrl = $failingUrl
                """.trimIndent())
            }
        }

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
//            super.onReceivedSslError(view, handler, error)
            handler?.proceed()
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            Log.w("onPageStarted", url)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Log.w("onPageFinished", url)
        }

    }

    inner class WebChromeClienter : WebChromeClient() {

        override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
            return super.onJsAlert(view, url, message, result)

        }

        override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
            return super.onJsConfirm(view, url, message, result)

        }

        override fun onJsPrompt(view: WebView?, url: String?, message: String?, defaultValue: String?, result: JsPromptResult?): Boolean {
            return super.onJsPrompt(view, url, message, defaultValue, result)

        }


    }

}
