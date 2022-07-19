package es.gorillapp.misteri_2022.castList

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import es.gorillapp.misteri_2022.R
import es.gorillapp.misteri_2022.data.CastItem
import org.json.JSONArray


private const val TAG = "CastActivity"

class CastListActivity : AppCompatActivity() {

    val url = "http://resources.gorilapp.com/misteri/cast.php"
    var castList = ArrayList<CastItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cast_list)

        val recyclerView: RecyclerView = findViewById(R.id.cast_recycler_view)
        val castLiveData = MutableLiveData(downloadTask())
        val castListAdapter = CastListAdapter {}

        recyclerView.adapter = castListAdapter

        castLiveData.observe(this) {
            it?.let {
                castListAdapter.submitList(it as MutableList<CastItem>)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        Thread.sleep(3000)

        val recyclerView: RecyclerView = findViewById(R.id.cast_recycler_view)
        val castLiveData = MutableLiveData(castList)
        val castListAdapter = CastListAdapter {}

        recyclerView.adapter = castListAdapter

        castLiveData.observe(this) {
            it?.let {
                castListAdapter.submitList(it as MutableList<CastItem>)
            }
        }
    }

    private fun downloadTask(): ArrayList<CastItem>{

        val queue = Volley.newRequestQueue(this)


        val request = StringRequest(
            Request.Method.GET, url,
            { response ->

                val data = response.toString()
                var jArray = JSONArray(data)
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

            }, {  })
        queue.add(request)

        return castList
    }

}