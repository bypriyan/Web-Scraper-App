package com.bypriyan.friendshub

import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast
import org.jsoup.Jsoup

class MyJavaScriptInterface {
    @JavascriptInterface
    fun showHTML(html: String) {
        val doc = Jsoup.parse(html)
        val elements = doc.select("[style*=rgb]")

        var dataList = mutableListOf<ModelScrapedValues>()
        var recentData = FloatArray(5)
        var sixthData  = 0.0f
        var redColorAppeared = false
        var conservativePinkColorsList = mutableListOf<ModelScrapedValues>()
        var blueColorsList = mutableListOf<ModelScrapedValues>()
        var dataLessThan1p90List = mutableListOf<ModelScrapedValues>()

        for (element in elements) {
            val styleAttribute = element.attr("style")
            val rgbRegex = Regex("rgb\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)")
            val matchResult = rgbRegex.find(styleAttribute)
            if (matchResult != null) {
                val (r, g, b) = matchResult.destructured
                val value = element.text() // Extract the associated data
                if(value.length>0){
//                    Log.d("lelo ", "RGB: ($r, $g, $b), Value: $value")
                    dataList.add(ModelScrapedValues("RGB: ($r, $g, $b)","$value"))
                }
            }
        }//loop

        var count =0
            //25
        dataList.takeLast(25).forEach{
            if(count<5){
                recentData[count]=it.value.replace("x","").toFloat()
            }
            if(count==5){
                sixthData=it.value.replace("x","").toFloat()
            }
//            Log.d("lelo ", " color ${it.rgb} value ${it.value.replace("x","")}")
            count++
        }

        recentData.forEach {
            Log.d("lelo ", "recent $it")
        }
        Log.d("lelo ", "sixth $sixthData")

        if (recentData.all { it < 7.00 } && recentData.any { it >= 1.00 } && sixthData < 8.00) {
            Log.d("lelo ", "good")
        }else{
            Log.d("lelo ", "not good")

        }

    }
}
