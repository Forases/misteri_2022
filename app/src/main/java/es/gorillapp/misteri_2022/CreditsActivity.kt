package es.gorillapp.misteri_2022

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView

class CreditsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = this.window
        window.statusBarColor = this.resources.getColor(R.color.misteri_yellow_2)

        setContentView(R.layout.activity_credits)

        val backButton = findViewById<View>(R.id.back_credits) as ImageView
        backButton.setOnClickListener {
            val intent = Intent()
            intent.setClass(applicationContext, MenuActivity::class.java)
            startActivity(intent)
        }

    }
}