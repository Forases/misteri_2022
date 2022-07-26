package es.gorillapp.misteri_2022.sceneList

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import es.gorillapp.misteri_2022.R
import es.gorillapp.misteri_2022.data.SceneItem
import es.gorillapp.misteri_2022.data.sceneFestaList
import es.gorillapp.misteri_2022.data.sceneVespraList

class SceneListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = this.window
        window.statusBarColor = this.resources.getColor(R.color.misteri_yellow_2)
        setContentView(R.layout.scene_list)

        val sceneVesprasList = sceneVespraList(this.resources)
        val sceneFestaList = sceneFestaList(this.resources)

        val sceneVespraLiveData = MutableLiveData(sceneVesprasList)
        val sceneFestaLiveData = MutableLiveData(sceneFestaList)

        /* Instantiates headerAdapter and flowersAdapter. Both adapters are added to concatAdapter.
        which displays the contents sequentially */
        val vespraListAdapter = SceneListAdapter { sceneItem -> adapterOnClick(sceneItem) }
        val festaListAdapter = SceneListAdapter { sceneItem -> adapterOnClick(sceneItem) }

        val vespraRecyclerView: RecyclerView = findViewById(R.id.vespra_recyclerview)
        val festaRecyclerView: RecyclerView = findViewById(R.id.festa_recyclerview)
        vespraRecyclerView.adapter = vespraListAdapter
        festaRecyclerView.adapter = festaListAdapter

        sceneVespraLiveData.observe(this) {
            it?.let {
                vespraListAdapter.submitList(it as MutableList<SceneItem>)
            }
        }

        sceneFestaLiveData.observe(this) {
            it?.let {
                festaListAdapter.submitList(it as MutableList<SceneItem>)
            }
        }
    }

    /* Opens FlowerDetailActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(sceneItem: SceneItem) {
//        val intent = Intent(this, FlowerDetailActivity()::class.java)
//        intent.putExtra(FLOWER_ID, flower.id)
//        startActivity(intent)
    }
}