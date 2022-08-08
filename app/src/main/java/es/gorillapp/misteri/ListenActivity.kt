package es.gorillapp.misteri

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import es.gorillapp.misteri.data.Slide
import es.gorillapp.misteri.sceneList.SceneListActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ListenActivity : AppCompatActivity() {
    private lateinit var buttonRewind: ImageButton
    private lateinit var buttonPause: ImageButton
    private lateinit var buttonPlay: ImageButton
    private lateinit var buttonForward: ImageButton
    private lateinit var imageView: ImageView
    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var cabecera: TextView
    private lateinit var title: TextView
    private lateinit var textoOriginal: TextView
    private lateinit var traduccion: TextView
    private lateinit var numDiapositiva: TextView
    private lateinit var info: TextView

    private lateinit var listenProgressBar: ProgressBar

    private lateinit var slidesDBHelper: DBManager

    private var audioNum: Int = 0

    private val currentSlide: MutableLiveData<Slide> by lazy {
        MutableLiveData<Slide>()
    }

    private lateinit var accountPrefs: SharedPreferences
    private var defaultLang: String? = null

    private var progressBarStatus = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set orientation of layout  based on if is tablet or smartphone
        if(isTablet(this))
            requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        //set statusBarColor
        val window = this.window
        window.statusBarColor = this.resources.getColor(R.color.misteri_yellow_2)

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

        val nameObserver = Observer<Slide> { newSlide ->
            //slide = newSlide
            cabecera = findViewById(R.id.sceneCabecera)
            title = findViewById(R.id.listen_title)

            imageView = findViewById(R.id.listen_image)

            numDiapositiva = findViewById(R.id.listenNumSlide)
            // Update the UI with the slide info
            title.text = newSlide.titulo

            imageView.setImageResource( resources.getIdentifier(
                "drawable/image${newSlide.nombreImagen!!.toInt()}",
                null,
                this.packageName))
            numDiapositiva.text = String.format(resources.getString(R.string.slideNum),newSlide.numDiapositiva.toString())

            val currentFragment = supportFragmentManager.findFragmentById(R.id.directo_fragment)
            if(currentFragment is DialogoFragment){
                textoOriginal = findViewById(R.id.dialogo_texto_original)
                traduccion = findViewById(R.id.dialogo_traduccion)
                textoOriginal.text = newSlide.textoOriginal
                traduccion.text = newSlide.traduccion
            }else{
                info = findViewById(R.id.escena_texto_info)
                info.text = newSlide.info
            }

            if(isTablet(this)){
                info = findViewById(R.id.escena_texto_info)
                info.text = newSlide.info
            }

            if(newSlide.numDiapositiva < 31){
                cabecera.text = resources.getString(R.string.scene_vespra)
            }else{
                cabecera.text = resources.getString(R.string.scene_festa)
            }
        }

        currentSlide.observe(this, nameObserver)

        currentSlide.value = slidesDBHelper.getNumberSlide(defaultLang, audioNum)

        mediaPlayer = MediaPlayer()

        buttonRewind = findViewById(R.id.button_rewind)
        buttonPause = findViewById(R.id.button_pause)
        buttonPlay = findViewById(R.id.button_play)
        buttonForward = findViewById(R.id.button_forward)
        listenProgressBar = findViewById(R.id.listenProgressBar)

        playAudio(mediaPlayer)
        updateProgressBar(mediaPlayer)

        setOnClickListeners()

        if(!isTablet(this)){
            val toggleButton = findViewById<View>(R.id.toggleListenButton) as ToggleButton

            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    val sceneFragment = EscenaFragment.newInstance(currentSlide.value!!.info.toString())
                    val fragmentTransaction1 = fragmentManager.beginTransaction()
                    fragmentTransaction1.replace(R.id.directo_fragment, sceneFragment)
                    fragmentTransaction1.commit()
                } else {

                    val dialogFragment = DialogoFragment.newInstance(
                        currentSlide.value!!.textoOriginal.toString(),
                        currentSlide.value!!.traduccion.toString())
                    // The toggle is disabled
                    val fragmentTransaction2 = fragmentManager.beginTransaction()
                    fragmentTransaction2.replace(R.id.directo_fragment, dialogFragment)
                    fragmentTransaction2.commit()
                }
            }
        }


        val backButton = findViewById<View>(R.id.back_listen) as ImageView
        backButton.setOnClickListener {
            val intent = Intent()
            intent.setClass(applicationContext, SceneListActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
        buttonPause.visibility = GONE
        buttonPlay.visibility = VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onResume() {
        super.onResume()
        if(buttonPause.visibility == VISIBLE)
        mediaPlayer.start()
    }

    private fun setOnClickListeners() {
        buttonRewind.setOnClickListener {
            audioNum--
            currentSlide.value = slidesDBHelper.getNumberSlide(defaultLang, audioNum)
            playAudio(mediaPlayer)
        }

        buttonPause.setOnClickListener {
            if(buttonPause.visibility == VISIBLE){
                mediaPlayer.pause()
                buttonPause.visibility = GONE
                buttonPlay.visibility = VISIBLE
            }
        }

        buttonPlay.setOnClickListener {
            if(buttonPlay.visibility == VISIBLE){
                mediaPlayer.start()
                buttonPause.visibility = VISIBLE
                buttonPlay.visibility = GONE
            }
        }

        buttonForward.setOnClickListener {
            audioNum++
            currentSlide.value = slidesDBHelper.getNumberSlide(defaultLang, audioNum)
            playAudio(mediaPlayer)
        }
    }

    private fun playAudio(mediaPlayer: MediaPlayer){

        currentSlide.value = slidesDBHelper.getNumberSlide(defaultLang, audioNum)

        if(mediaPlayer.isPlaying){
            mediaPlayer.pause()
            mediaPlayer.stop()
            mediaPlayer.reset()
        }else{
            mediaPlayer.reset()
            buttonPlay.visibility = GONE
            buttonPause.visibility = VISIBLE
        }

        val url = "https://resources.gorilapp.com/misteri/audio/audio_$audioNum.mp3"
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepare()

        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()

        }
        checkNumAudio(audioNum)
    }

    private fun checkNumAudio(numAudio: Int){
        if(numAudio == 1){
            //buttonRewind.background = resources.getDrawable(R.drawable.rounded_button_disabled)
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
        this.lifecycleScope.launch {
            withContext(Dispatchers.Default){
                listenProgressBar.max = mediaPlayer.duration

                while (progressBarStatus < listenProgressBar.max) {
                    // performing some dummy operation
                    try {
                        Thread.sleep(200)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                    if(this.isActive){
                        // Updating the progress bar
                        try{
                            listenProgressBar.progress = mediaPlayer.currentPosition
                        }catch (e: IllegalStateException){
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }
}