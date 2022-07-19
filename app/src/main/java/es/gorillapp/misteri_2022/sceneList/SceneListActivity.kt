package es.gorillapp.misteri_2022.sceneList

import android.app.ProgressDialog
import android.content.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import es.gorillapp.misteri_2022.R
import es.gorillapp.misteri_2022.data.SceneItem

class SceneListActivity : AppCompatActivity() {
    var sceneItemList: ArrayList<SceneItem>? = null
    var indexPosicion = 0
    var topPosicion: Int = 0
    var nb_scenes_vespra = 0
    var nb_scenes_festa: Int = 0
    var dialog: ProgressDialog? = null
    var downloadCompleteReceiver: BroadcastReceiver? = null
    var areAudiosDownloaded = false

    private val newFlowerActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Remove the title
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.scene_list)

        /* Instantiates headerAdapter and flowersAdapter. Both adapters are added to concatAdapter.
        which displays the contents sequentially */
        val sceneAdapter = SceneAdapter { scene -> adapterOnClick(scene) }

        val recyclerView: RecyclerView = findViewById(R.id.scene_recycler_view)
        recyclerView.adapter = sceneAdapter

//        flowersListViewModel.flowersLiveData.observe(this, {
//            it?.let {
//                sceneAdapter.submitList(it as MutableList<Flower>)
//                headerAdapter.updateFlowerCount(it.size)
//            }
//        })
    }

    /* Opens FlowerDetailActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(sceneItem: SceneItem) {
//        val intent = Intent(this, FlowerDetailActivity()::class.java)
//        intent.putExtra(FLOWER_ID, flower.id)
//        startActivity(intent)
    }


    /**
     * Act ENUM
     */
    enum class Act(var id: String, var title: String) {
        vespra("vespra", "La Vespra"), festa("festa", "La Festa");

        fun id(): String {
            return id
        }

        override fun toString(): String {
            return id
        }
    }
}