package es.gorillapp.misteri.sceneList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.gorillapp.misteri.R
import es.gorillapp.misteri.data.SceneItem

class SceneListAdapter(private val onClick: (SceneItem) -> Unit) :
    ListAdapter<SceneItem, SceneListAdapter.SceneViewHolder>(SceneDiffCallback) {

    /* ViewHolder for Scene, takes in the inflated view and the onClick behavior. */
    class SceneViewHolder(itemView: View, val onClick: (SceneItem) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val sceneId: TextView = itemView.findViewById(R.id.sceneitem_id)
        private val sceneImage: ImageView = itemView.findViewById(R.id.sceneitem_image)
        private var currentScene: SceneItem? = null

        init {
            itemView.setOnClickListener {
                currentScene?.let {
                    onClick(it)
                }
            }
        }

        /* Bind scene name and image. */
        fun bind(sceneItem: SceneItem) {
            currentScene = sceneItem

            sceneId.text = sceneItem.nombre
            sceneImage.setImageResource(sceneItem.image!!)
        }
    }

    /* Creates and inflates view and return FlowerViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SceneViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.scene_item, parent, false)
        return SceneViewHolder(view, onClick)
    }

    /* Gets current flower and uses it to bind view. */
    override fun onBindViewHolder(holder: SceneViewHolder, position: Int) {
        val scene = getItem(position)
        holder.bind(scene)

    }
}

object SceneDiffCallback : DiffUtil.ItemCallback<SceneItem>() {
    override fun areItemsTheSame(oldItem: SceneItem, newItem: SceneItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: SceneItem, newItem: SceneItem): Boolean {
        return oldItem.nombre == newItem.nombre
    }
}