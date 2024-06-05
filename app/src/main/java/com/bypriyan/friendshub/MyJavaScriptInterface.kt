package com.bypriyan.friendshub

import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast
import com.bypriyan.bustrackingsystem.utility.Constants
import org.jsoup.Jsoup

class MyJavaScriptInterface (private val activity: DataActivity) {
    @JavascriptInterface
    fun showHTML(html: String) {
        val doc = Jsoup.parse(html)
        val elements = doc.select("[style*=rgb]")

        var dataList = mutableListOf<ModelScrapedValues>()
        var recentData = FloatArray(9)
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
            if(count<9){
                recentData[count]=it.value.replace("x","").toFloat()
            }
            if(count==9){
                sixthData=it.value.replace("x","").toFloat()
            }

            if(it.rgb.equals(Constants.KEY_PINK_COLOR)){
                redColorAppeared=true
//            Log.d("lelo ", " color ${it.rgb} value ${it.value.replace("x","")}"
            }

            if(it.rgb.equals(Constants.KEY_PURPLE_BLUE_COLOR)){
                conservativePinkColorsList.add(it)
            }

            if(it.rgb.equals(Constants.KEY_BLUE_COLOR)){
                blueColorsList.add(it)
            }

            if(it.value.replace("x","").toFloat()<1.90){
                dataLessThan1p90List.add(it)
            }

            count++
        }

        recentData.forEach {
            Log.d("lelo ", "recentData $it")
        }

        //1st
        if (recentData.take(5).all { it < 7.00 } && recentData.take(5).any { it >= 1.00 } && sixthData < 8.00) {
            Log.d("lelo ", "1st good")
            activity.updatetv1("1st good")
        }else{
            activity.updatetv1("1st not good")
        }

        Log.d("lelo", "showHTML: red color $redColorAppeared")
        //2nd
        if (redColorAppeared) {
            val last6Data = recentData.take(6)
            if (last6Data.all { it < 8.00 }) {
                Log.d("lelo", "showHTML: 2nd GOOD")
                activity.updatetv2("2nd good")
                // Continue to number 3
            }else{
                activity.updatetv2("2nd not good")
            }
        }
        //3rd
        if (redColorAppeared) {
            val last9Data = dataList.take(9)
            if (last9Data.none { it.rgb.equals(Constants.KEY_PINK_COLOR)} && last9Data.count { it.value.replace("x","").toFloat() >= 4.00 } >= 3 && last9Data.any { it.value.replace("x","").toFloat()  >= 8.00 }) {
                activity.updatetv3("3rd good")
                // Continue to number 4
            }else{
                activity.updatetv3("3rd not good")
            }
        }
        //4th
        if (conservativePinkColorsList.size >= 2) {
            val sum = conservativePinkColorsList.map { (it.value.replace("x","").toFloat())}.sum()
            Log.d("lelo", "sum $sum ")

            if (sum <= 9.00 && blueColorsList.size >= 3 && dataLessThan1p90List.size >= 2) {
                Log.d("lolo", "showHTML: 4th good")
                activity.updatetv4("4th good")
                // Start fresh from number 1
            }else{
                activity.updatetv4("4th not good")
            }
        }

    }
}
