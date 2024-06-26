package com.bypriyan.friendshub

import android.content.Intent
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bypriyan.friendshub.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var webView: WebView
    lateinit var edOne: TextView
    lateinit var edtwo: TextView
    lateinit var edThree:TextView
    lateinit var edFour: TextView
    lateinit var logout: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        webView = findViewById(R.id.webview)

        edOne = findViewById(R.id.oneTv)
        edtwo = findViewById(R.id.twoTv)
        edThree = findViewById(R.id.threeTv)
        edFour = findViewById(R.id.fourTv)

        logout = findViewById(R.id.logoutBtn)

        logout.setOnClickListener {
            logout()
        }

        webView.visibility = View.GONE

        firebaseAuth = FirebaseAuth.getInstance()

        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
        reference.child(firebaseAuth.currentUser!!.uid).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val timestamp = snapshot.child("timestamp").value as String
                Log.d("TAG", "onDataChange: new timestamp is ${System.currentTimeMillis() + 300000}")
                if (System.currentTimeMillis() < timestamp.toLong()) {
                    webView.visibility = View.VISIBLE
                } else {
                    webView.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        webView.settings.javaScriptEnabled = true

        // Add JavaScript Interface
        webView.addJavascriptInterface(MyJavaScriptInterface(this), "Android")

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

                // Stop the refreshing animation
                swipeRefreshLayout.isRefreshing = false
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
        }

        webView.loadUrl("https://aviator-demo.spribegaming.com/?currency=USD&operator=demo&jurisdiction=CW&lang=EN&return_url=https:%2F%2Fspribe.co%2Fgames&user=21437&token=xKkYfxMpu2CGTMveNOzkH0VJNTMuAoi9")  // Replace with your URL

        swipeRefreshLayout.setOnRefreshListener {
            // Reload the WebView
            webView.reload()
        }

    }

    fun updatetv1(text: String) {
        runOnUiThread {
            edOne.text = text
        }
    }
    fun updatetv2(text: String) {
        runOnUiThread {
            edtwo.text = text
        }
    }
    fun updatetv3(text: String) {
        runOnUiThread {
            edThree.text = text
        }
    }

    fun updatetv4(text: String) {
        runOnUiThread {
            edFour.text = text
        }
    }

    fun logout(){
        var firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        var intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
