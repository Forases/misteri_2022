package es.gorillapp.misteri_2022

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class BuyTicketsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.buy_tickets)

        val webView = findViewById<View>(R.id.webView) as WebView
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://www.elcorteingles.es/entradas/conciertos/entradas-la-festa-o-misteri-d-elx-elche-elx/")
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }
        }
    }
}