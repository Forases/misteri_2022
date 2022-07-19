package es.gorillapp.misteri_2022

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.service.controls.actions.FloatAction
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import es.gorillapp.misteri_2022.castList.CastListActivity
import es.gorillapp.misteri_2022.infoList.InfoListActivity
import es.gorillapp.misteri_2022.sceneList.SceneListActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class MenuActivity : AppCompatActivity() {
    private val oropelRainINTERVAL = 7
    var mHandler = Handler()
    var screenWidth = 0
    var screenHeight:Int = 0
    var parameters: ArrayList<String>? = null

    //	BroadcastReceiver downloadCompleteReceiver = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Languages //
        //Retrieve default lang
        val accountPrefs = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE)
        val defaultLang = accountPrefs.getString(getString(R.string.lang), "es")

        //Set default lang at the app context
        val locale = Locale(defaultLang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        applicationContext.applicationContext.resources.updateConfiguration(config, null)

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
        //Set the correct language flag
        val flagId = resources.getIdentifier(
            defaultLang + "_flag", "drawable",
            packageName
        )
        //langButton.setBackgroundResource(flagId)
        langButton.setOnClickListener {
            val intent = Intent()
            intent.setClass(applicationContext, LangSettingActivity::class.java)
            startActivity(intent)
        }

        //Credits  Button onClick
//        val creditsButton = findViewById<View>(R.id.infoCredits) as LinearLayout
//        creditsButton.setOnClickListener {
//            val intent = Intent()
//            intent.setClass(applicationContext, CreditsActivity::class.java)
//            startActivity(intent)
//        }

        //Live  Button onClick
        val live = findViewById<View>(R.id.live) as LinearLayout
        live.setOnClickListener {
            val intent = Intent()
            intent.setClass(applicationContext, CastListActivity::class.java)
            startActivity(intent)
        }

        //Listen  Button onClick
        val listen = findViewById<View>(R.id.listen) as LinearLayout
        listen.setOnClickListener {
            // 		        SharedPreferences accountPrefs = getSharedPreferences(getString(R.string.sharedPreferences), Context.MODE_PRIVATE);
// 				boolean areAudiosDownloaded = accountPrefs.getBoolean(getString(R.string.are_audios_downloaded), false);
//
// 				if(!areAudiosDownloaded) {
//
//	 		        Builder alertDialog = new AlertDialog.Builder(MenuActivity.this);
//	 		        alertDialog.setTitle(getString(R.string.dialog_download_title));
//	 		        alertDialog.setMessage(getString(R.string.dialog_download_msg));
//	 		        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
//	 		        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//	 		        public void onClick(final DialogInterface dialogInterface, final int which) {
//	 		        	dialog = ProgressDialog.show(MenuActivity.this, getString(R.string.progressdialog_download_title), getString(R.string.progressdialog_download_msg), true);
//	 		        	dialog.show();
//	 		        	downloadAudios();
//	 		        }});
//
//	 		        alertDialog.setNegativeButton(getString(R.string.dialog_btn_cancel), new DialogInterface.OnClickListener() {
//	 		          public void onClick(DialogInterface dialog, int which) {
//	 		        	 dialog.cancel();
//	 		        } });
//	 		        alertDialog.show();
// 				}else{
            // Navigate to the next activity
            if (!isMisteriDay()) {
                val newIntent = Intent()
                newIntent.setClass(applicationContext, SceneListActivity::class.java)
                startActivity(newIntent)
            } else {
                val alertDialog = AlertDialog.Builder(this@MenuActivity)
                alertDialog.setTitle(getString(R.string.warning_no_listen_title))
                alertDialog.setMessage(getString(R.string.warning_no_listen_msg))
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert)
                alertDialog.setPositiveButton(
                    getString(R.string.dialog_btn_accept)
                ) { dialog, which -> dialog.cancel() }
                alertDialog.show()
            }
            // 				}
        }

        //Info history  Button onClick
        val infohistory = findViewById<View>(R.id.infoHistory) as LinearLayout
        infohistory.setOnClickListener {
            val intent = Intent()
            intent.setClass(applicationContext, InfoListActivity::class.java)
            val b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            startActivity(intent, b)
        }

        //Buy tickets  Button onClick
        val buyTickets = findViewById<View>(R.id.buyTickets) as LinearLayout
        buyTickets.setOnClickListener {
            val intent = Intent()
            intent.setClass(applicationContext, BuyTicketsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // If we have muted the user device, we reset his configuration
        val accountPrefs = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE)
        val userRingmode = accountPrefs.getString(getString(R.string.user_ringmode), null)
        if (userRingmode != null) {
            val audio = applicationContext.getSystemService(AUDIO_SERVICE) as AudioManager
            audio.ringerMode = userRingmode.toInt()
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun isMisteriDay(): Boolean {
        var isMisteriDay = false
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val formattedDate = sdf.format(calendar.time)
        val misteriDates = resources.getStringArray(R.array.misteri_dates)
        var i = 0
        while (i < misteriDates.size && !isMisteriDay) {
            if (misteriDates[i] == formattedDate) {
                isMisteriDay = true
            }
            i++
        }
        return isMisteriDay
    }


    fun getScreenDimensions() {
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        screenWidth = dm.widthPixels
        screenHeight = dm.heightPixels
    }

    fun bringButtonsToFront(menuBackground: RelativeLayout) {
        val languageSetting = findViewById<View>(R.id.lang) as Button
        menuBackground.bringChildToFront(languageSetting)
        val live = findViewById<View>(R.id.live) as LinearLayout
        menuBackground.bringChildToFront(live)
        val listen = findViewById<View>(R.id.listen) as LinearLayout
        menuBackground.bringChildToFront(listen)
        val infoHistory = findViewById<View>(R.id.infoHistory) as LinearLayout
        menuBackground.bringChildToFront(infoHistory)
        val buyTickets = findViewById<View>(R.id.buyTickets) as LinearLayout
        menuBackground.bringChildToFront(buyTickets)
    }

    var mHandlerTask: Runnable = object : Runnable {
        override fun run() {
            mHandler.postDelayed(this, oropelRainINTERVAL.toLong())
        }
    }
}