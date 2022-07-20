package es.gorillapp.misteri_2022.infoList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import es.gorillapp.misteri_2022.R
import es.gorillapp.misteri_2022.data.InfoItem
import es.gorillapp.misteri_2022.data.infoList

class InfoListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

    /* Opens FlowerDetailActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(infoItem: InfoItem) {
//        val intent = Intent(this, FlowerDetailActivity()::class.java)
//        intent.putExtra(FLOWER_ID, flower.id)
//        startActivity(intent)
    }
}