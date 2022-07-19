package es.gorillapp.misteri_2022

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class LangSettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Remove the title
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.lang_settings)

        //Lang = ca  Button onClick
        val btn_ca = findViewById<View>(R.id.ca) as ImageView
        btn_ca.setOnClickListener {
            setDefaultLang(Language.CA.langCode)
            navigateToMenu()
        }

        //Lang = es  Button onClick
        val btn_es = findViewById<View>(R.id.es) as ImageView
        btn_es.setOnClickListener {
            setDefaultLang(Language.ES.langCode)
            navigateToMenu()
        }

        //Lang = en  Button onClick
        val btn_en = findViewById<View>(R.id.en) as ImageView
        btn_en.setOnClickListener {
            setDefaultLang(Language.EN.langCode)
            navigateToMenu()
        }

        //Lang = de  Button onClick
        val btn_de = findViewById<View>(R.id.de) as ImageView
        btn_de.setOnClickListener {
            setDefaultLang(Language.DE.langCode)
            navigateToMenu()
        }

        //Lang = fr  Button onClick
        val btn_fr = findViewById<View>(R.id.fr) as ImageView
        btn_fr.setOnClickListener {
            setDefaultLang(Language.FR.langCode)
            navigateToMenu()
        }

        //Lang = it  Button onClick
        val btn_it = findViewById<View>(R.id.it) as ImageView
        btn_it.setOnClickListener {
            setDefaultLang(Language.IT.langCode)
            navigateToMenu()
        }
    }

    fun setDefaultLang(lang: String?) {
        // In the app context
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        applicationContext.applicationContext.resources.updateConfiguration(config, null)

        // In the preferences for future times
        val accountPref = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE)
        val editor = accountPref.edit()
        editor.putString(getString(R.string.lang), lang)
        editor.commit()
    }

    fun navigateToMenu() {
        val intent = Intent()
        intent.setClass(applicationContext, MenuActivity::class.java)
        startActivity(intent)
    }

    /**
     * Language ENUM
     */
    enum class Language(var langCode: String) {
        CA("ca"), ES("es"), EN("en"), DE("de" /*, IT("it")*/), FR("fr"),  IT("it");

    }
}