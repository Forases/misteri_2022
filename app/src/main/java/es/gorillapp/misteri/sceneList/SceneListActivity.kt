package es.gorillapp.misteri.sceneList

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import es.gorillapp.misteri.ListenActivity
import es.gorillapp.misteri.MenuActivity
import es.gorillapp.misteri.data.SceneItem
import es.gorillapp.misteri.data.sceneFestaList
import es.gorillapp.misteri.data.sceneVespraList
import es.gorillapp.misteri.R
import es.gorillapp.misteri.infoList.InfoListActivity
import es.gorillapp.misteri.isTablet

class SceneListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set orientation of layout  based on if is tablet or smartphone
        if(isTablet(this))
            requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

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

        val backButton = findViewById<View>(R.id.back_scene_list) as ImageView
        backButton.setOnClickListener {
            val intent = Intent()
            intent.setClass(applicationContext, MenuActivity::class.java)
            startActivity(intent)
        }
    }

    private fun adapterOnClick(sceneItem: SceneItem) {
        val intent = Intent(this, ListenActivity()::class.java)
        intent.putExtra("audioNum", sceneItem.numero)
        startActivity(intent)
    }
}