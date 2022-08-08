package es.gorillapp.misteri

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import es.gorillapp.misteri.castList.CastListActivity
import es.gorillapp.misteri.infoList.InfoListActivity
import es.gorillapp.misteri.sceneList.SceneListActivity
import java.util.*
import kotlin.math.roundToInt

class MenuActivity : AppCompatActivity() {
    var screenWidth = 0
    var screenHeight:Int = 0

    //	BroadcastReceiver downloadCompleteReceiver = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //Set orientation of layout  based on if is tablet or smartphone
        if(isTablet(this))
        requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        //set statusBarColor
        val window = this.window
        window.statusBarColor = this.resources.getColor(R.color.misteri_yellow_2)

        // Languages //
        //Retrieve default lang
        val accountPrefs = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE)
        val defaultLang = accountPrefs.getString(getString(R.string.lang), "es")
        val config = resources.configuration
        //Set default lang at the app context
        val locale = defaultLang?.let { Locale(it) }
        if (locale != null) {
            Locale.setDefault(locale)
        }
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)

        //Remove the title
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.menu)

        //Resize logo if device height is smaller then 500 pixels
        getScreenDimensions()
        if (screenHeight < 500) {
            val logo = findViewById<View>(R.id.logo) as ImageView

            //Set the margins. Top 15 pixels and center horizontal
            val lp = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL)
            lp.setMargins(0, 15, 0, 0)
            logo.layoutParams = lp

            // Scale the image
            val dr = logo.drawable
            val bitmap = (dr as BitmapDrawable).bitmap
            val imageWidth = bitmap.width
            val imageHeigth = bitmap.height
            val newImageHeight: Int = screenHeight / 6
            val ratio = newImageHeight.toFloat() / imageHeigth
            val newImageWidth = (imageWidth * ratio).roundToInt()
            val d: Drawable = BitmapDrawable(
                resources,
                Bitmap.createScaledBitmap(bitmap, newImageWidth, newImageHeight, true)
            )
            // Set your new, scaled drawable "d"
            logo.setImageDrawable(d)
        }

        //Language  Button onClick
        val langButton = findViewById<View>(R.id.lang)
        langButton.setOnClickListener {
            val intent = Intent()
            intent.setClass(applicationContext, LangSettingActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Credits  Button onClick
        val creditsButton = findViewById<View>(R.id.infoCredits) as LinearLayout
        creditsButton.setOnClickListener {
            val intent = Intent()
            intent.setClass(applicationContext, CreditsActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Live  Button onClick
        val live = findViewById<View>(R.id.live) as LinearLayout
        live.setOnClickListener {
            val intent = Intent()
            intent.setClass(applicationContext, CastListActivity::class.java)
            intent.putExtra("showAdvice", true)
            startActivity(intent)
            finish()
        }

        //Listen  Button onClick
        val listen = findViewById<View>(R.id.listen) as LinearLayout
        listen.setOnClickListener {
            val newIntent = Intent()
            newIntent.setClass(applicationContext, SceneListActivity::class.java)
            startActivity(newIntent)
        }

        //Info history  Button onClick
        val infohistory = findViewById<View>(R.id.infoHistory) as LinearLayout
        infohistory.setOnClickListener {
            val intent = Intent()
            intent.setClass(applicationContext, InfoListActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Buy tickets  Button onClick
        val buyTickets = findViewById<View>(R.id.buyTickets) as LinearLayout
        buyTickets.setOnClickListener {
            val intent = Intent()
            intent.setClass(applicationContext, BuyTicketsActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Visit Elche Button onClick
        val visitElche = findViewById<View>(R.id.visitElche) as LinearLayout
        visitElche.setOnClickListener {
            val intent = Intent()
            intent.setClass(applicationContext, VisitElcheActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getScreenDimensions() {
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        screenWidth = dm.widthPixels
        screenHeight = dm.heightPixels
    }
}