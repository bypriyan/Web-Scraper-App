package com.bypriyan.sharemarketcourseinhindi.utility

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.lifecycle.LiveData
import kotlin.contracts.Returns

class NetworkConnection(val context: Context): LiveData<Boolean>() {

    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private lateinit var networkConnectionCallback: ConnectivityManager.NetworkCallback

    override fun onActive() {
        super.onActive()
        updateConnection()
        when{
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                connectivityManager.registerDefaultNetworkCallback(connectionCallback())
            }
            else ->{
                context.registerReceiver(
                    networkReceiver,
                    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                )
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        try{
            connectivityManager.unregisterNetworkCallback(connectionCallback())
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun connectionCallback():ConnectivityManager.NetworkCallback{
      networkConnectionCallback = object : ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: android.net.Network) {
                postValue(true)
            }

          override fun onLost(network: Network) {
              super.onLost(network)
              postValue(false)
          }
        }
        return networkConnectionCallback
    }

    private fun updateConnection(){
        val activeNetwork = connectivityManager.activeNetwork
        if(activeNetwork == null){
            postValue(false)
        }
    }

    private val networkReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            updateConnection()
        }

    }

}