package com.bypriyan.friendshub

import android.util.Log
import android.webkit.JavascriptInterface
import org.jsoup.Jsoup

class MyJavaScriptInterface {
    @JavascriptInterface
    fun showHTML(html: String) {
        val doc = Jsoup.parse(html)
        val elements = doc.select("[style*=rgb]")

        for (element in elements) {
            val styleAttribute = element.attr("style")
            val rgbRegex = Regex("rgb\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)")
            val matchResult = rgbRegex.find(styleAttribute)
            if (matchResult != null) {
                val (r, g, b) = matchResult.destructured
                val value = element.text() // Extract the associated data
                Log.d("lelo ", "RGB: ($r, $g, $b), Value: $value")
            }
        }
    }
}
