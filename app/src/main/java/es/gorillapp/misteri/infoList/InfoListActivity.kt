package es.gorillapp.misteri.infoList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import es.gorillapp.misteri.HistoriaActivity
import es.gorillapp.misteri.MenuActivity
import es.gorillapp.misteri.R
import es.gorillapp.misteri.data.InfoItem
import es.gorillapp.misteri.data.infoList

class InfoListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //set statusBarColor
        val window = this.window
        window.statusBarColor = this.resources.getColor(R.color.misteri_yellow_2)

        setContentView(R.layout.activity_info_list)

        val infoItemsList = infoList(this.resources)
        val infoLiveData = MutableLiveData(infoItemsList)

        /* Instantiates headerAdapter and flowersAdapter. Both adapters are added to concatAdapter.
        which displays the contents sequentially */
        val infoListAdapter = InfoListAdapter { infoItem -> adapterOnClick(infoItem) }

        val recyclerView: RecyclerView = findViewById(R.id.info_recycler_view)
        recyclerView.adapter = infoListAdapter

        infoLiveData.observe(this) {
            it?.let {
                infoListAdapter.submitList(it as MutableList<InfoItem>)
            }
        }

        val backButton = findViewById<View>(R.id.back_info) as ImageView
        backButton.setOnClickListener {
            val intent = Intent()
            intent.setClass(applicationContext, MenuActivity::class.java)
            startActivity(intent)
        }
    }

    /* Opens HistoriaActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(infoItem: InfoItem) {
        val intent = Intent(this, HistoriaActivity()::class.java)
        intent.putExtra("itemID", infoItem.id)
        startActivity(intent)
    }
}