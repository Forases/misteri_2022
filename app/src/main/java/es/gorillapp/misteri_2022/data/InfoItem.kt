package es.gorillapp.misteri_2022.data

import androidx.annotation.DrawableRes

class InfoItem (
    val id: String?,
    @DrawableRes
    val image: Int?,
    val title: String,
    val subtitle: String?
)
