package es.gorillapp.misteri_2022.castList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.gorillapp.misteri_2022.R
import es.gorillapp.misteri_2022.data.CastItem

class CastListAdapter(private val onClick: (CastItem) -> Unit) :
    ListAdapter<CastItem, CastListAdapter.CastViewHolder>(CastDiffCallback) {

    /* ViewHolder for Cast, takes in the inflated view and the onClick behavior. */
    class CastViewHolder(itemView: View, val onClick: (CastItem) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val character: TextView = itemView.findViewById(R.id.cast_character)
        private val name: TextView = itemView.findViewById(R.id.cast_name)
        private var currentCast: CastItem? = null

        init {
            itemView.setOnClickListener {
                currentCast?.let {
                    onClick(it)
                }
            }
        }

        /* Bind cast name and image. */
        fun bind(castItem: CastItem) {
            currentCast = castItem

            character.text = castItem.character
            name.text = castItem.name

        }
    }

    /* Creates and inflates view and return FlowerViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cast_item, parent, false)
        return CastViewHolder(view, onClick)
    }

    /* Gets current flower and uses it to bind view. */
    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val cast = getItem(position)
        holder.bind(cast)

    }
}

object CastDiffCallback : DiffUtil.ItemCallback<CastItem>() {
    override fun areItemsTheSame(oldItem: CastItem, newItem: CastItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CastItem, newItem: CastItem): Boolean {
        return oldItem.id == newItem.id
    }
}