package es.gorillapp.misteri

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
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
import es.gorillapp.misteri.castList.CastListActivity
import es.gorillapp.misteri.data.DirectItem
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
    private lateinit var gradientImageView: ImageView
    private lateinit var directoTitle: TextView
    private lateinit var actoTitulo: TextView
    private lateinit var directoTextoOriginal: TextView
    private lateinit var directoTraduccion: TextView
    private lateinit var directoInfo: TextView

    private lateinit var mediaPlayer: MediaPlayer

    private var currentAudio: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set orientation of layout  based on if is tablet or smartphone
        if(isTablet(this))
            requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        //set statusBarColor
        val window = this.window
        window.statusBarColor = this.resources.getColor(R.color.misteri_yellow_2)

        val accountPrefs = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE)
        val defaultLang = accountPrefs.getString(getString(R.string.lang), "es")
        val isAudiodescrption = accountPrefs.getBoolean("audioDescription", false)
        val url = "https://resources.gorilapp.com/misteri/misteriVivo.php?lang=$defaultLang"
        setContentView(R.layout.activity_directo)

        mediaPlayer = MediaPlayer()

        val fragmentManager = supportFragmentManager

        val nameObserver = Observer<DirectItem> { newDirectItem ->
            directoTitle = findViewById(R.id.direct_title)
            actoTitulo = findViewById(R.id.actoTitulo)
            directoImageView = findViewById(R.id.directo_image)

            if(newDirectItem.audio > 30){
                actoTitulo.text = resources.getString(R.string.segundo_acto)
            }else{
                actoTitulo.text = resources.getString(R.string.primer_acto)
            }
            directoTitle.text = newDirectItem.titulo
            directoImageView.setImageResource( resources.getIdentifier(
                "drawable/image${newDirectItem.nombreImagen!!}",
                null,
                this.packageName))

            gradientImageView = findViewById(R.id.imageGradient)

            if(newDirectItem.nombreImagen!! == "interludio"){
                actoTitulo.visibility = GONE
                directoTitle.visibility = GONE
                gradientImageView.visibility = GONE
            }else{
                actoTitulo.visibility = VISIBLE
                directoTitle.visibility = VISIBLE
                gradientImageView.visibility = VISIBLE
            }


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

            if(isTablet(this)){
                directoInfo = findViewById(R.id.escena_texto_info)
                directoInfo.text = newDirectItem.textoInfo
            }

            if(isAudiodescrption){
                playAudio(mediaPlayer, itemDirecto.value!!.audio)
            }
        }

        itemDirecto.observe(this, nameObserver)

        updateItemDirecto(url)

        if(!isTablet(this)){
            val toggleButton = findViewById<View>(R.id.toggleButton) as ToggleButton

            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    updateItemDirecto(url)
                    val sceneFragment = EscenaFragment.newInstance(itemDirecto.value!!.textoInfo.toString())
                    val fragmentTransaction1 = fragmentManager.beginTransaction()
                    fragmentTransaction1.replace(R.id.directo_fragment, sceneFragment)
                    fragmentTransaction1.commit()

                } else {
                    updateItemDirecto(url)
                    val dialogFragment = DialogoFragment.newInstance(
                        itemDirecto.value!!.textoOriginal.toString(),
                        itemDirecto.value!!.traduccion.toString())
                    // The toggle is disabled
                    val fragmentTransaction2 = fragmentManager.beginTransaction()
                    fragmentTransaction2.replace(R.id.directo_fragment, dialogFragment)
                    fragmentTransaction2.commit()
                }
            }
        }else{

        }
        val backButton = findViewById<View>(R.id.back_directo) as ImageView
        backButton.setOnClickListener {
            val intent = Intent()
            intent.setClass(applicationContext, CastListActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
    }

    private fun downloadItemDirecto(url: String){

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
                    jsonData.getInt("audio")
                )

                itemDirecto.value = newItemDirecto
            },
            {error->
                Toast.makeText(this, getVolleyError(error), Toast.LENGTH_LONG).show()
                this.finish()})
        queue.add(request)

    }

    private fun updateItemDirecto(url: String){
        // task is run on a thread
        this.lifecycleScope.launch {
            withContext(Dispatchers.IO){
                while (this.isActive) {
                    // performing some dummy operation
                    try {
                        downloadItemDirecto(url)
                        Thread.sleep(2000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }


                }
            }
        }
    }

    private fun playAudio(mediaPlayer: MediaPlayer, audio: Int){
        if(currentAudio != audio){
            if(mediaPlayer.isPlaying){
                mediaPlayer.pause()
                mediaPlayer.stop()
                mediaPlayer.reset()
            }else if(itemDirecto.value?.nombreImagen == "interludio"){
                mediaPlayer.reset()
            }
            val url = "https://resources.gorilapp.com/misteri/audiodirect/audio_direct_$audio.mp3"

            try{
                mediaPlayer.setDataSource(url)
                mediaPlayer.prepare()

                mediaPlayer.setOnPreparedListener {
                    mediaPlayer.start()
                }
            }catch(e: Exception){
                e.printStackTrace()
            }finally{
                currentAudio = audio
            }


        }
    }
}