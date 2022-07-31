package es.gorillapp.misteri

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import es.gorillapp.misteri.data.DirectItem
import es.gorillapp.misteri.data.Slide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class DirectoActivity : AppCompatActivity() {
    //var itemDirecto: DirectItem? = null
    private val itemDirecto: MutableLiveData<DirectItem> by lazy {
        MutableLiveData<DirectItem>()
    }

    private lateinit var directoImageView: ImageView
    private lateinit var directoTitle: TextView
    private lateinit var directoTextoOriginal: TextView
    private lateinit var directoTraduccion: TextView
    private lateinit var numDiapositiva: TextView
    private lateinit var directoInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val accountPrefs = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE)
        val defaultLang = accountPrefs.getString(getString(R.string.lang), "es")
        val url = "https://resources.gorilapp.com/misteri/misteriVivo.php?lang=$defaultLang"
        setContentView(R.layout.activity_directo)

        val nameObserver = Observer<DirectItem> { newDirectItem ->
            directoTitle = findViewById(R.id.direct_title)
            directoImageView = findViewById(R.id.directo_image)

            directoTitle.text = newDirectItem.titulo
            directoImageView.setImageResource( resources.getIdentifier(
                "drawable/image${newDirectItem.nombreImagen!!.toInt()}",
                null,
                this.packageName))

            val currentFragment = supportFragmentManager.findFragmentById(R.id.directo_fragment)
            if(currentFragment is DialogoFragment){
                directoTextoOriginal = findViewById(R.id.dialogo_texto_original)
                directoTraduccion = findViewById(R.id.dialogo_traduccion)
                directoTextoOriginal.text = newDirectItem.textoOriginal
                directoTraduccion.text = newDirectItem.traduccion
            }else{
                directoInfo = findViewById(R.id.escena_texto_info)
                directoInfo.text = newDirectItem.textoInfo
            }
        }

        itemDirecto.observe(this, nameObserver)

        downloadDialogoTask(url)

        val fragmentManager = supportFragmentManager
        val toggleButton = findViewById<View>(R.id.toggleButton) as ToggleButton

        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                updateProgressBar(url)
                val sceneFragment = EscenaFragment()
                val fragmentTransaction1 = fragmentManager.beginTransaction()
                val mBundle = Bundle()
                mBundle.putString("infoText", itemDirecto.value!!.textoInfo)
                sceneFragment.arguments = mBundle
                fragmentTransaction1.replace(R.id.directo_fragment, sceneFragment)
                fragmentTransaction1.commit()



            } else {
                updateProgressBar(url)
                val dialogFragment = DialogoFragment.newInstance(
                    itemDirecto.value!!.textoOriginal.toString(),
                    itemDirecto.value!!.traduccion.toString())
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

                val newItemDirecto = DirectItem(
                    jsonData.getString("textoOriginal"),
                    jsonData.getString("traduccion"),
                    jsonData.getString("info"),
                    jsonData.getString("titulo"),
                    jsonData.getString("nombreImagen"),
                    jsonData.getString("audio")
                )

                itemDirecto.value = newItemDirecto
            },
            {error->
                Toast.makeText(this, getVolleyError(error), Toast.LENGTH_LONG).show()})
        queue.add(request)

    }

    private fun updateProgressBar(url: String){
        // task is run on a thread
        this.lifecycleScope.launch {
            withContext(Dispatchers.Default){
                while (true) {
                    // performing some dummy operation
                    try {
                        downloadDialogoTask(url)
                        Thread.sleep(2000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }


                }
            }
        }
    }
}