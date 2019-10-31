package com.livermor.plusminus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebSettings
import kotlinx.android.synthetic.main.activity_main3.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri


class Main3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        Log.i(TAG(), "onCreate: about to start wb")
        wb.visibility = View.GONE
        wb.settings.javaScriptEnabled = true
        wb.settings.loadWithOverviewMode = true
        wb.settings.useWideViewPort = true
        wb.settings.builtInZoomControls = true
        wb.settings.pluginState = WebSettings.PluginState.ON
        wb.webViewClient = HelloWebViewClient()
        CookieManager.getInstance().setAcceptThirdPartyCookies(wb, true)
        wb.loadUrl("https://plusminus.me")
        Log.i(TAG(), "onCreate: url just loaded")
        Runnable { wb.visibility = View.VISIBLE }.taskAfter(2000)
    }

    private inner class HelloWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

            if (url.equals("https://plusminus.me/oauth/init")) {
                // https://developers.google.com/identity/sign-in/android
                return true
            }
            /*
            if (url.startsWith(VimeoClientCredentials.API_OAUTH_REDIRECT)) {
            // https://stackoverflow.com/questions/51560171/android-handling-oauth2-authentication-cant-enable-cookies-on-webview
            webView.stopLoading()
            webView.loadUrl("about:blank")
            showSignInWebView(false)
            val code = Uri.parse(url).getQueryParameter("code")

            if (code != null) {
            mUserPresenter.getOauthToken(code, VimeoClientCredentials.API_OAUTH_REDIRECT)
            } else {
            showUnauthorizedError()
            }
            return true
            }
             */
            return false
        }
    }
}
