package com.bypriyan.friendshub

import com.bypriyan.togocartstore.api.ApiService
import retrofit2.Response
import javax.inject.Inject

class WebsidteRepo @Inject constructor(private val apiService: ApiService) {

    suspend fun getPageResponce(pageUrl: String):String {
        var res = apiService.getPageResponce(pageUrl)
        if (res.isSuccessful) {
            return res.body()?:"null"
        }else{
            return "null"
        }
    }
}