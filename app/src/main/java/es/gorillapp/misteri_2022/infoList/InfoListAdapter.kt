package es.gorillapp.misteri_2022.infoList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.gorillapp.misteri_2022.R
import es.gorillapp.misteri_2022.data.InfoItem

class InfoListAdapter(private val onClick: (InfoItem) -> Unit) :
    ListAdapter<InfoItem, InfoListAdapter.InfoItemViewHolder>(InfoItemDiffCallback) {

    /* ViewHolder for InfoItem, takes in the inflated view and the onClick behavior. */
    class InfoItemViewHolder(itemView: View, val onClick: (InfoItem) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val infoItemTitle: TextView = itemView.findViewById(R.id.infoitem_title)
        private val infoItemSubtitle: TextView = itemView.findViewById(R.id.infoitem_subtitle)
        private val infoItemImage: ImageView = itemView.findViewById(R.id.infoitem_image)
        private var currentInfoItem: InfoItem? = null

        init {
            itemView.setOnClickListener {
                currentInfoItem?.let {
                    onClick(it)
                }
            }
        }

        /* Bind InfoItem name and image. */
        fun bind(InfoItem: InfoItem) {
            currentInfoItem = InfoItem

            infoItemTitle.text = InfoItem.title
            infoItemSubtitle.text = InfoItem.subtitle
            infoItemImage.setImageResource(InfoItem.image!!)

        }
    }

    /* Creates and inflates view and return InfoItemViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.info_item, parent, false)
        return InfoItemViewHolder(view, onClick)
    }

    /* Gets current InfoItem and uses it to bind view. */
    override fun onBindViewHolder(holder: InfoItemViewHolder, position: Int) {
        val infoItem = getItem(position)
        holder.bind(infoItem)

    }
}

object InfoItemDiffCallback : DiffUtil.ItemCallback<InfoItem>() {
    override fun areItemsTheSame(oldItem: InfoItem, newItem: InfoItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: InfoItem, newItem: InfoItem): Boolean {
        return oldItem.id == newItem.id
    }
}