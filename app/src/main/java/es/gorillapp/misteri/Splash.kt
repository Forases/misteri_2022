package es.gorillapp.misteri

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class Splash : AppCompatActivity() {
    private var _active = true
    private var _splashTime = 2000 // time to display the splash screen in ms


    /** Called when the activity is first created.  */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //set statusBarColor
        val window = this.window
        window.statusBarColor = this.resources.getColor(R.color.misteri_yellow_2)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.splash)

        // thread for displaying the SplashScreen
        val splashTread: Thread = object : Thread() {
            override fun run() {
                try {
                    var waited = 0
                    while (_active && waited < _splashTime) {
                        sleep(100)
                        if (_active) {
                            waited += 100
                        }
                    }
                } catch (e: InterruptedException) {
                    // do nothing
                } finally {
                    finish()
                    val intent = Intent()
                    val accountPrefs =
                        getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE)
                    val defaultLang = accountPrefs.getString(getString(R.string.lang), null)
                    if (defaultLang == null) {
                        intent.setClass(applicationContext, LangSettingActivity::class.java)
                    } else {
                        intent.setClass(applicationContext, MenuActivity::class.java)
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                }
            }
        }
        splashTread.start()
    }

    fun onTouchEvent(): Boolean {
        return true
    }
}