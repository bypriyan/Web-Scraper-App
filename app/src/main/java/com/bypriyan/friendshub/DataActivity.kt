package com.bypriyan.friendshub

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class DataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)
        val webView: WebView = findViewById(R.id.webview)

        webView.settings.javaScriptEnabled = true

        // Add JavaScript Interface
        webView.addJavascriptInterface(MyJavaScriptInterface(), "Android")

        webView.webChromeClient = WebChromeClient()

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Add a slight delay to ensure all JavaScript is executed and content is loaded
                webView.postDelayed({
                    // Inject JavaScript to extract content
                    webView.evaluateJavascript(
                        "(function() { " +
                                "var html = document.documentElement.outerHTML; " +
                                "window.Android.showHTML(html); " +
                                "})();"
                    ) { html ->
                        // This log might still be null if the evaluation happens asynchronously
                        Log.d("lemo", "onPageFinished: $html")
                    }
                }, 3000) // Adjust the delay as needed
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
        }

        webView.loadUrl("https://aviator-demo.spribegaming.com/?currency=USD&operator=demo&jurisdiction=CW&lang=EN&return_url=https:%2F%2Fspribe.co%2Fgames&user=21437&token=xKkYfxMpu2CGTMveNOzkH0VJNTMuAoi9")  // Replace with your URL
    }
}