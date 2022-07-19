package es.gorillapp.misteri_2022.sceneList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.gorillapp.misteri_2022.R
import es.gorillapp.misteri_2022.data.SceneItem

class SceneAdapter(private val onClick: (SceneItem) -> Unit) :
    ListAdapter<SceneItem, SceneAdapter.SceneViewHolder>(SceneDiffCallback) {

    /* ViewHolder for Flower, takes in the inflated view and the onClick behavior. */
    class SceneViewHolder(itemView: View, val onClick: (SceneItem) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val flowerTextView: TextView = itemView.findViewById(R.id.scenelistitem_name)
        private val flowerImageView: ImageView = itemView.findViewById(R.id.scenelistitem_image)
        private var currentFlower: SceneItem? = null

        init {
            itemView.setOnClickListener {
                currentFlower?.let {
                    onClick(it)
                }
            }
        }

        /* Bind flower name and image. */
        fun bind(sceneItem: SceneItem) {
            currentFlower = sceneItem

            flowerTextView.text = sceneItem.name
            if (sceneItem.thumbnail != null) {
                flowerImageView.setImageResource(R.drawable.btn_creditos)
            } else {
                flowerImageView.setImageResource(R.drawable.btn_creditos)
            }
        }
    }

    /* Creates and inflates view and return FlowerViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SceneViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.scene_list_item, parent, false)
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
        return oldItem.id == newItem.id
    }
}