package es.gorillapp.misteri

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class BuyTicketsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set orientation of layout  based on if is tablet or smartphone
        requestedOrientation = if(isTablet(this)){
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }else{
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        //set statusBarColor
        val window = this.window
        window.statusBarColor = this.resources.getColor(R.color.misteri_yellow_2)

        setContentView(R.layout.buy_tickets)

        val webView = findViewById<View>(R.id.webView) as WebView
        val progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://www.elcorteingles.es/entradas/conciertos/entradas-la-festa-o-misteri-d-elx-elche-elx/")
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = GONE

            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                progressBar.visibility = GONE
            }
        }

        val backButton = findViewById<View>(R.id.cancel_tickets) as ImageView
        backButton.setOnClickListener {
            val intent = Intent()
            intent.setClass(applicationContext, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}