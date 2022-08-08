package es.gorillapp.misteri.castList

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import es.gorillapp.misteri.*
import es.gorillapp.misteri.data.CastItem
import org.json.JSONArray

class CastListActivity : AppCompatActivity() {

    private val url = "https://resources.gorilapp.com/misteri/cast.php"
    var apostolsList = ArrayList<CastItem>()
    var jueusList = ArrayList<CastItem>()
    var escolanosList = ArrayList<CastItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set orientation of layout  based on if is tablet or smartphone
        if(isTablet(this))
            requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        //set statusBarColor
        val window = this.window
        window.statusBarColor = this.resources.getColor(R.color.misteri_yellow_2)

        setContentView(R.layout.activity_cast_list)

        val accountPrefs = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE)
        val isAudioDescription = accountPrefs.getBoolean("audioDescription", false)

        var showAdvice = false
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            showAdvice = bundle.getBoolean("showAdvice")
        }

        if(!isAudioDescription && showAdvice) muteDeviceAdvice(this)

        downloadTask()

        val btnStart = findViewById<View>(R.id.comienzo) as LinearLayout
        btnStart.setOnClickListener {
            val intent = Intent()
            intent.setClass(applicationContext, DirectoActivity::class.java)
            startActivity(intent)
        }

        val backButton = findViewById<View>(R.id.back_cast) as ImageView
        backButton.setOnClickListener {
            val intent = Intent()
            intent.setClass(applicationContext, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun downloadTask(){

        val queue = Volley.newRequestQueue(this)
        val progressBar: ProgressBar = findViewById(R.id.cast_progress)

        val request = StringRequest(
            Request.Method.GET, url,
            { response ->

                val data = response.toString()
                val jArray = JSONArray(data)
                val jsonData = jArray.getJSONObject(0)

                // joan-tomas-pere-ternari-...
                val strCastResponseKeys = getString(R.string.cast_response_keys)
                val castResponseKeys = strCastResponseKeys.split("-").toTypedArray()
                var id = 1

                for (i in castResponseKeys) {
                    val character = getString(resources.getIdentifier(
                        "cast$id", "string", this.packageName))
                    val actor = jsonData.getString(i)

                    when(id){
                        in 1..5 -> apostolsList.add(CastItem(id,character, actor))
                        in 6..10 -> jueusList.add(CastItem(id,character, actor))
                        in 11..18 -> escolanosList.add(CastItem(id,character, actor))
                    }
                    id ++
                }

                val apostolsRecyclerView: RecyclerView = findViewById(R.id.apostols_recycler_view)
                val jueusRecyclerView: RecyclerView = findViewById(R.id.jueus_recycler_view)
                val escolanosRecyclerView: RecyclerView = findViewById(R.id.escolanos_recycler_view)

                val apostolsLiveData = MutableLiveData(apostolsList)
                val jueusLiveData = MutableLiveData(jueusList)
                val escolanosLiveData = MutableLiveData(escolanosList)

                val apostolsListAdapter = CastListAdapter {}
                val jueusListAdapter = CastListAdapter {}
                val escolanosListAdapter = CastListAdapter {}

                apostolsRecyclerView.adapter = apostolsListAdapter
                jueusRecyclerView.adapter = jueusListAdapter
                escolanosRecyclerView.adapter = escolanosListAdapter

                apostolsLiveData.observe(this) {
                    it?.let {
                        apostolsListAdapter.submitList(it as MutableList<CastItem>)
                    }
                }

                jueusLiveData.observe(this) {
                    it?.let {
                        jueusListAdapter.submitList(it as MutableList<CastItem>)
                    }
                }

                escolanosLiveData.observe(this) {
                    it?.let {
                        escolanosListAdapter.submitList(it as MutableList<CastItem>)
                    }
                }
                progressBar.visibility = GONE
            },
            {error->
                progressBar.visibility = GONE
                Toast.makeText(this, getVolleyError(error), Toast.LENGTH_LONG).show()
                this.finish()
            })

        queue.add(request)
    }
}