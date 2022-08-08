package es.gorillapp.misteri

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import es.gorillapp.misteri.castList.CastListActivity
import java.util.*

class LangSettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set orientation of layout  based on if is tablet or smartphone
        if(isTablet(this))
            requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        //set statusBarColor
        val window = this.window
        window.statusBarColor = this.resources.getColor(R.color.misteri_yellow_2)

        setContentView(R.layout.lang_settings)

        //Lang = ca  Button onClick
        val btn_ca = findViewById<View>(R.id.ca_layout) as LinearLayout
        btn_ca.setOnClickListener {
            setDefaultLang(Language.CA.langCode, false)
            navigateToMenu()
        }

        //Lang = es  Button onClick
        val btn_es = findViewById<View>(R.id.es_layout) as LinearLayout
        btn_es.setOnClickListener {
            setDefaultLang(Language.ES.langCode, false)
            navigateToMenu()
        }

        //Lang = es_audio  Button onClick
        val btn_es_audio = findViewById<View>(R.id.es_audio_layout) as LinearLayout
        btn_es_audio.setOnClickListener {
            setDefaultLang(Language.ES.langCode, true)
            navigateToMenu()
        }

        //Lang = en  Button onClick
        val btn_en = findViewById<View>(R.id.en_layout) as LinearLayout
        btn_en.setOnClickListener {
            setDefaultLang(Language.EN.langCode, false)
            navigateToMenu()
        }

        //Lang = de  Button onClick
        val btn_de = findViewById<View>(R.id.de_layout) as LinearLayout
        btn_de.setOnClickListener {
            setDefaultLang(Language.DE.langCode, false)
            navigateToMenu()
        }

        //Lang = fr  Button onClick
        val btn_fr = findViewById<View>(R.id.fr_layout) as LinearLayout
        btn_fr.setOnClickListener {
            setDefaultLang(Language.FR.langCode, false)
            navigateToMenu()
        }

        //Lang = it  Button onClick
        val btn_it = findViewById<View>(R.id.it_layout) as LinearLayout
        btn_it.setOnClickListener {
            setDefaultLang(Language.IT.langCode, false)
            navigateToMenu()
        }

        val backButton = findViewById<View>(R.id.back_language) as ImageView
        backButton.setOnClickListener {
            val intent = Intent()
            intent.setClass(applicationContext, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        val accountPrefs =
            getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE)
        val defaultLang = accountPrefs.getString(getString(R.string.lang), null)
        if (defaultLang == null) {
            val backButton = findViewById<ImageView>(R.id.back_language)
            backButton.isEnabled = false
        }
    }

    private fun setDefaultLang(lang: String?, audio: Boolean) {
        // In the app context
        val config = resources.configuration
        val locale = lang?.let { Locale(it) }
        if (locale != null) {
            Locale.setDefault(locale)
        }
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)

        // In the preferences for future times
        val accountPref = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE)
        val editor = accountPref.edit()
        editor.putString(getString(R.string.lang), lang)
        editor.putBoolean("audioDescription",audio)
        editor.apply()
    }

    private fun navigateToMenu() {
        val intent = Intent()
        intent.setClass(applicationContext, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Language ENUM
     */
    enum class Language(var langCode: String) {
        CA("ca"), ES("es"), EN("en"), DE("de" /*, IT("it")*/), FR("fr"),  IT("it");

    }
}