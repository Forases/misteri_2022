package es.gorillapp.misteri

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import es.gorillapp.misteri.infoList.InfoListActivity

class HistoriaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var currentItemId: String? = null
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            currentItemId = bundle.getString("itemID")
        }

        when(currentItemId){
            "history" -> setContentView(R.layout.activity_historia)
            "text" -> setContentView(R.layout.activity_texto)
            "music" -> setContentView(R.layout.activity_musica)
            "tramoya" -> setContentView(R.layout.activity_escenarios)
            "celebration" -> setContentView(R.layout.activity_fiesta)
            "recognition" -> setContentView(R.layout.activity_reconocimientos)
        }



        val backButton = findViewById<View>(R.id.back_history) as ImageView
        backButton.setOnClickListener {
            val intent = Intent()
            intent.setClass(applicationContext, InfoListActivity::class.java)
            startActivity(intent)
        }
    }


}