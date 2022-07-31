package es.gorillapp.misteri

import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import es.gorillapp.misteri.data.Slide


class ListenActivity : AppCompatActivity() {
    private lateinit var buttonRewind: ImageButton
    private lateinit var buttonPause: ImageButton
    private lateinit var buttonForward: ImageButton
    private lateinit var imageView: ImageView
    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var title: TextView
    private lateinit var textoOriginal: TextView
    private lateinit var traduccion: TextView
    private lateinit var info: TextView

    private lateinit var listenProgressBar: ProgressBar

    private lateinit var slidesDBHelper: DBManager

    private var audioNum: Int = 0
    private val dialogFragment = DialogoFragment()
    private val escenaFragment = EscenaFragment()

    private val currentSlide: MutableLiveData<Slide> by lazy {
        MutableLiveData<Slide>()
    }

    private lateinit var accountPrefs: SharedPreferences
    private var defaultLang: String? = null

    private var progressBarStatus = 0F
    var dummy:Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        slidesDBHelper = DBManager(this)


        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            audioNum = bundle.getInt("audioNum")
        }



        //We retrieve the language settings
        accountPrefs = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE)
        defaultLang = accountPrefs.getString(getString(R.string.lang), "es")



        setContentView(R.layout.activity_listen)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction1 = fragmentManager.beginTransaction()
        fragmentTransaction1.add(R.id.directo_fragment, dialogFragment)
        fragmentTransaction1.commit()
        //Titulo

        //imageView.setImageResource( resources.getIdentifier("drawable/image${slide.nombreImagen!!.toInt()}", null, this.packageName))

        val nameObserver = Observer<Slide> { newSlide ->
            title = findViewById(R.id.listen_title)
            textoOriginal = findViewById(R.id.dialogo_texto_original)
            traduccion = findViewById(R.id.dialogo_traduccion)
            imageView = findViewById(R.id.listen_image)
            //info = findViewById(R.id.direct_texto_info)
            // Update the UI with the slide info
            title.text = newSlide.titulo
            textoOriginal.text = newSlide.textoOriginal
            traduccion.text = newSlide.traduccion
            imageView.setImageResource( resources.getIdentifier("drawable/image${newSlide.nombreImagen!!.toInt()}", null, this.packageName))
        }

        currentSlide.observe(this, nameObserver)

        currentSlide.value = slidesDBHelper.getNumberSlide(defaultLang, audioNum)

        val url = "https://resources.gorilapp.com/misteri/audio/audio_$audioNum.mp3"


        mediaPlayer = MediaPlayer()

        buttonRewind = findViewById(R.id.button_rewind)
        buttonPause = findViewById(R.id.button_pause)
        buttonForward = findViewById(R.id.button_forward)
        listenProgressBar = findViewById(R.id.listenProgressBar)


        playAudio(mediaPlayer)






        setOnClickListeners()

        val toggleButton = findViewById<View>(R.id.toggleButton) as ToggleButton
        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val sceneFragment = EscenaFragment()
                // The toggle is enabled
                val fragmentTransaction1 = fragmentManager.beginTransaction()
                fragmentTransaction1.replace(R.id.directo_fragment, sceneFragment)
                fragmentTransaction1.commit()



            } else {
                val dialogFragment = DialogoFragment()
                // The toggle is disabled
                val fragmentTransaction2 = fragmentManager.beginTransaction()
                fragmentTransaction2.replace(R.id.directo_fragment, dialogFragment)
                fragmentTransaction2.commit()
            }
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

    private fun setOnClickListeners() {
        buttonRewind.setOnClickListener {
            audioNum--
            currentSlide.value = slidesDBHelper.getNumberSlide(defaultLang, audioNum)
            playAudio(mediaPlayer)
            //Toast.makeText(context, "Reproduciendo...", Toast.LENGTH_SHORT).show()
        }

        buttonPause.setOnClickListener {
            mediaPlayer.pause()
        }

        buttonForward.setOnClickListener {
            audioNum++
            currentSlide.value = slidesDBHelper.getNumberSlide(defaultLang, audioNum)
            playAudio(mediaPlayer)
            //Toast.makeText(context, "Parando...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playAudio(mediaPlayer: MediaPlayer){
        currentSlide.value = slidesDBHelper.getNumberSlide(defaultLang, audioNum)

        if(mediaPlayer.isPlaying){
            mediaPlayer.pause()
            mediaPlayer.stop()
            mediaPlayer.reset()
        }

        val url = "https://resources.gorilapp.com/misteri/audio/audio_$audioNum.mp3"
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepare()

        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            if(mediaPlayer.isPlaying){
                updateProgressBar(mediaPlayer)
            }
        }
        checkNumAudio(audioNum)
    }

    private fun checkNumAudio(numAudio: Int){
        if(numAudio == 1){
            buttonRewind.isEnabled = false
        }else if(numAudio == 62){
            buttonForward.isEnabled = false
        }else{
            buttonRewind.isEnabled = true
            buttonForward.isEnabled = true
        }
    }

    private fun updateProgressBar(mediaPlayer: MediaPlayer){
        // task is run on a thread
        Thread {
            listenProgressBar.max = mediaPlayer.duration
            // dummy thread mimicking some operation whose progress can be tracked
            while (progressBarStatus < listenProgressBar.max) {
                // performing some dummy operation
                try {
                    Thread.sleep(200)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                // Updating the progress bar
                listenProgressBar.progress = mediaPlayer.currentPosition
            }
        }.start()

    }
}