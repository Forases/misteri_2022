package es.gorillapp.misteri.infoList

import android.content.Intent
import android.content.pm.ActivityInfo
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
import es.gorillapp.misteri.data.firstColumnList
import es.gorillapp.misteri.data.secondColumnList
import es.gorillapp.misteri.isTablet

class InfoListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set orientation of layout  based on if is tablet or smartphone
        if(isTablet(this))
            requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        //set statusBarColor
        val window = this.window
        window.statusBarColor = this.resources.getColor(R.color.misteri_yellow_2)

        setContentView(R.layout.activity_info_list)

        val firstItemsList = firstColumnList(this.resources)
        val secondItemsList = secondColumnList(this.resources)
        val firstLiveData = MutableLiveData(firstItemsList)
        val secondLiveData = MutableLiveData(secondItemsList)

        /* Instantiates headerAdapter and flowersAdapter. Both adapters are added to concatAdapter.
        which displays the contents sequentially */
        val firstListAdapter = InfoListAdapter { infoItem -> adapterOnClick(infoItem) }
        val secondListAdapter = InfoListAdapter { infoItem -> adapterOnClick(infoItem) }


        val firstRecyclerView: RecyclerView = findViewById(R.id.first_info_recycler_view)
        val secondRecyclerView: RecyclerView = findViewById(R.id.second_info_recycler_view)
        firstRecyclerView.adapter = firstListAdapter
        secondRecyclerView.adapter = secondListAdapter


        firstLiveData.observe(this) {
            it?.let {
                firstListAdapter.submitList(it as MutableList<InfoItem>)
            }
        }

        secondLiveData.observe(this) {
            it?.let {
                secondListAdapter.submitList(it as MutableList<InfoItem>)
            }
        }

        val backButton = findViewById<View>(R.id.back_info) as ImageView
        backButton.setOnClickListener {
            val intent = Intent()
            intent.setClass(applicationContext, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /* Opens HistoriaActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(infoItem: InfoItem) {
        val intent = Intent(this, HistoriaActivity()::class.java)
        intent.putExtra("itemID", infoItem.id)
        startActivity(intent)
        finish()
    }
}