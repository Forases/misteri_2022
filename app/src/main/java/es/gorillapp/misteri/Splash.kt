package es.gorillapp.misteri

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class Splash : AppCompatActivity() {
    private var _active = true
    private var _splashTime = 2000 // time to display the splash screen in ms


    /** Called when the activity is first created.  */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        downloadTask(this)

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

    fun downloadTask(context: Context){
        val url = "http://resources.gorilapp.com/misteri/representation_dates.php"
        var isRepresentacionDay: Boolean
        val queue = Volley.newRequestQueue(context)

        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                val data = response.toBoolean()
                isRepresentacionDay = data

                // In the preferences for future times
                if(isRepresentacionDay){
                    val accountPref = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE)
                    val editor = accountPref.edit()
                    editor.putBoolean(getString(R.string.isRepresentationDay), isRepresentacionDay)
                    editor.apply()
                }
            },
            {error->
                Toast.makeText(context, getVolleyError(error), Toast.LENGTH_LONG).show()})
        queue.add(request)
    }
}