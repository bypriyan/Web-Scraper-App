package com.bypriyan.friendshub

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bypriyan.friendshub.ui.theme.FriendsHubTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var pageResponceViewModel: PageResponceViewModel
    // ?currency=USD&operator=demo&jurisdiction=CW&lang=EN&return_url=https:%2F%2Fspribe.co%2Fgames&user=45948&token=QFFK3M67JYe6hyOABEDSYdQGYpUK64Qj
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pageResponceViewModel = ViewModelProvider(this)[PageResponceViewModel::class.java]

        setContent {
        }

        pageResponceViewModel.getResponce("/?currency=USD&operator=demo&jurisdiction=CW&lang=EN&return_url=https:%2F%2Fspribe.co%2Fgames&user=45948&token=QFFK3M67JYe6hyOABEDSYdQGYpUK64Qj")

        pageResponceViewModel.responce.observe(this, Observer {
            Log.d("TAG", "onCreate: $it")
        } )
    }
}