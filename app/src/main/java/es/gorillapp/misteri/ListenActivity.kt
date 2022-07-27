package es.gorillapp.misteri

import android.content.Context
import android.database.Cursor
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.gorillapp.misteri.data.Slide
import java.io.*


class ListenActivity : AppCompatActivity() {
    private lateinit var buttonRewind: ImageButton
    private lateinit var buttonPause: ImageButton
    private lateinit var buttonForward: ImageButton
    private lateinit var imageView: ImageView
    private lateinit var mediaPlayer: MediaPlayer

    //Titulo
    private lateinit var title: TextView
    //Texto original en valenciano
    private lateinit var textoOriginal: TextView
    //Traducción al castellamo
    private lateinit var traduccion: TextView
    private lateinit var slidesDBHelper: DBManager
    private var audioNum: Int = 0
    val dialogFragment = DialogoFragment()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        slidesDBHelper = DBManager(this)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            audioNum = bundle.getInt("audioNum")
        }

        val fragmentManager = supportFragmentManager
        val fragmentTransaction1 = fragmentManager.beginTransaction()
        fragmentTransaction1.add(R.id.directo_fragment, dialogFragment)
        fragmentTransaction1.commit()


        val accountPrefs = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE)
        val defaultLang = accountPrefs.getString(getString(R.string.lang), "es")

        val slide = slidesDBHelper.getNumberSlide(this, defaultLang, audioNum)
        setContentView(R.layout.activity_listen)
        //Titulo
        title = findViewById(R.id.listen_title)
        title.text = slide!!.titulo



        val url = "https://resources.gorilapp.com/misteri/audio/audio_$audioNum.mp3"


        mediaPlayer = MediaPlayer()

        buttonRewind = findViewById(R.id.button_rewind)
        buttonPause = findViewById(R.id.button_pause)
        buttonForward = findViewById(R.id.button_forward)
        imageView = findViewById(R.id.listen_image)

        mediaPlayer.setDataSource(url)
        mediaPlayer.prepare()
        mediaPlayer.start()

        setOnClickListeners(this)


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

    private fun setOnClickListeners(context: Context) {
        buttonRewind.setOnClickListener {
            audioNum--
            playAudio(mediaPlayer)
            Toast.makeText(context, "Reproduciendo...", Toast.LENGTH_SHORT).show()
        }

        buttonPause.setOnClickListener {
            mediaPlayer.pause()
            Toast.makeText(context, "En pausa...", Toast.LENGTH_SHORT).show()
        }

        buttonForward.setOnClickListener {
            audioNum++
            playAudio(mediaPlayer)
            Toast.makeText(context, "Parando...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playAudio(mediaPlayer: MediaPlayer){
        setImage()
        mediaPlayer.reset()
        val url = "https://resources.gorilapp.com/misteri/audio/audio_$audioNum.mp3"
        mediaPlayer.setDataSource(url)


        this.mediaPlayer.setOnPreparedListener {
            //buttonPause.isEnabled = false
            this.mediaPlayer.start()
        }
        mediaPlayer.prepareAsync()
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

    private fun setImage(){
        imageView.setImageResource( resources.getIdentifier("drawable/image$audioNum", null, this.packageName))
    }

    private fun getSlide(context: Context){
        //slidesDBHelper.open()

        slidesDBHelper.getNumberSlide(context, "es", 2)


    }
}