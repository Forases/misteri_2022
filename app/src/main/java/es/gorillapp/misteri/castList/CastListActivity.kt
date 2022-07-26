package es.gorillapp.misteri.castList

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import es.gorillapp.misteri.DirectoActivity
import es.gorillapp.misteri.data.CastItem
import es.gorillapp.misteri.getVolleyError
import org.json.JSONArray
import es.gorillapp.misteri.R


private const val TAG = "CastActivity"

class CastListActivity : AppCompatActivity() {

    private val url = "https://resources.gorilapp.com/misteri/cast.php"
    var castList = ArrayList<CastItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cast_list)

        castList = downloadTask()

        val btnStart = findViewById<View>(R.id.comienzo) as LinearLayout
        btnStart.setOnClickListener {
            val intent = Intent()
            intent.setClass(applicationContext, DirectoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun downloadTask(): ArrayList<CastItem>{

        val queue = Volley.newRequestQueue(this)

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
                    castList.add(CastItem(id,character, actor))
                    id ++
                }

                val recyclerView: RecyclerView = findViewById(R.id.cast_recycler_view)
                val castLiveData = MutableLiveData(castList)
                val castListAdapter = CastListAdapter {}

                recyclerView.adapter = castListAdapter

                castLiveData.observe(this) {
                    it?.let {
                        castListAdapter.submitList(it as MutableList<CastItem>)
                    }
                }

                val progressBar: ProgressBar = findViewById(R.id.cast_progress)
                progressBar.visibility = GONE

            },
            {error->
                Toast.makeText(this, getVolleyError(error), Toast.LENGTH_LONG).show()})
        queue.add(request)

        return castList
    }
}