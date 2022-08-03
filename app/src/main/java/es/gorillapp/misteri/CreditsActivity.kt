package es.gorillapp.misteri

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView

class CreditsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set orientation of layout  based on if is tablet or smartphone
        if(isTablet(this))
            requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        //set statusBarColor
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