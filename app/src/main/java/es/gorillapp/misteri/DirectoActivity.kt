package es.gorillapp.misteri

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import es.gorillapp.misteri.data.DirectItem
import org.json.JSONArray

class DirectoActivity : AppCompatActivity() {
    var itemDirecto: DirectItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val accountPrefs = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE)
        val defaultLang = accountPrefs.getString(getString(R.string.lang), "es")
        val url = "https://resources.gorilapp.com/misteri/misteriVivo.php?lang=$defaultLang"
        setContentView(R.layout.activity_directo)
        downloadDialogoTask(url)
        val fragmentManager = supportFragmentManager


        val toggleButton = findViewById<View>(R.id.toggleButton) as ToggleButton

        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                downloadEscenaTask(url)
                val sceneFragment = EscenaFragment()
                // The toggle is enabled
                val fragmentTransaction1 = fragmentManager.beginTransaction()
                fragmentTransaction1.replace(R.id.directo_fragment, sceneFragment)
                fragmentTransaction1.commit()



            } else {
                downloadDialogoTask(url)
                val dialogFragment = DialogoFragment()
                // The toggle is disabled
                val fragmentTransaction2 = fragmentManager.beginTransaction()
                fragmentTransaction2.replace(R.id.directo_fragment, dialogFragment)
                fragmentTransaction2.commit()
            }
        }

    }

    private fun downloadDialogoTask(url: String){

        val queue = Volley.newRequestQueue(this)


        val request = StringRequest(
            Request.Method.GET, url,
            { response ->

                val data = response.toString()
                val jArray = JSONArray(data)
                val jsonData = jArray.getJSONObject(0)

                //Titulo
                val title: TextView = findViewById(R.id.direct_title)
                title.text = jsonData.getString("titulo")

                //Texto original en valenciano
                val textoOriginal: TextView = findViewById(R.id.dialogo_texto_original)
                textoOriginal.text = jsonData.getString("textoOriginal")

                //Traducción al castellamo
                val traduccion: TextView = findViewById(R.id.dialogo_traduccion)
                traduccion.text = jsonData.getString("traduccion")

            },
            {error->
                Toast.makeText(this, getVolleyError(error), Toast.LENGTH_LONG).show()})
        queue.add(request)

    }

    private fun downloadEscenaTask(url: String){

        val queue = Volley.newRequestQueue(this)


        val request = StringRequest(
            Request.Method.GET, url,
            { response ->

                val data = response.toString()
                val jArray = JSONArray(data)
                val jsonData = jArray.getJSONObject(0)

                //Titulo
                val title: TextView = findViewById(R.id.direct_title)
                title.text = jsonData.getString("titulo")

                //Información de la escena
                val textoInfo: TextView = findViewById(R.id.direct_texto_info)
                textoInfo.text = jsonData.getString("info")

            },
            {error->
                Toast.makeText(this, getVolleyError(error), Toast.LENGTH_LONG).show()})
        queue.add(request)
    }
}