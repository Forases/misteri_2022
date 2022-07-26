package es.gorillapp.misteri

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.*


class ListenActivity : AppCompatActivity() {
    private lateinit var buttonRewind: ImageButton
    private lateinit var buttonPause: ImageButton
    private lateinit var buttonForward: ImageButton
    private lateinit var imageView: ImageView
    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var slidesDBHelper: DBManager

    private var audioNum: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        slidesDBHelper = DBManager(this)
        getSlide(this)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            audioNum = bundle.getInt("audioNum")
        }
        val url = "https://resources.gorilapp.com/misteri/audio/audio_$audioNum.mp3"



        setContentView(R.layout.activity_listen)

        mediaPlayer = MediaPlayer()
        buttonRewind = findViewById(R.id.button_rewind)
        buttonPause = findViewById(R.id.button_pause)
        buttonForward = findViewById(R.id.button_forward)
        imageView = findViewById(R.id.listen_image)

        checkNumAudio(audioNum)
        playAudio(mediaPlayer)
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

    fun getSlide(context: Context){
        //slidesDBHelper.open()
        copyDataBaseFromAssets(context)
    }



    private fun copyDataBaseFromAssets(context: Context) {

        val DB_PATH = "/data/data/es.gorilapp.es.gorillap.es.gorillap.misteri/databases/"
        val DB_NAME = "Slides.db"

        var myInput: InputStream? = null
        var myOutput: OutputStream? = null
        try {

            val folder = context.getDatabasePath("databases")

            if (!folder.exists())
                if (folder.mkdirs()) folder.delete()

            myInput = context.assets.open(DB_NAME)

            val outFileName = DB_PATH + DB_NAME

            val f = File(outFileName)

            if (f.exists())
                return

            myOutput = FileOutputStream(outFileName)

            //transfer bytes from the inputfile to the outputfile
            val buffer = ByteArray(1024)
            var length: Int = myInput.read(buffer)

            while (length > 0) {
                myOutput.write(buffer, 0, length)
                length = myInput.read(buffer)
            }

            //Close the streams
            myOutput.flush()
            myOutput.close()
            myInput.close()


        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}