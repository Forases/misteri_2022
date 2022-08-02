package es.gorillapp.misteri.data

import android.content.res.Resources
import es.gorillapp.misteri.R

/* Returns initial list of flowers. */
fun firstColumnList(resources: Resources): List<InfoItem> {
    return listOf(
        InfoItem(
            id = resources.getString(R.string.info_history_id),
            image = R.drawable.historia_thumb,
            title = resources.getString(R.string.info_history_title),
            subtitle = resources.getString(R.string.info_history_subtitle)
        ),
        InfoItem(
            id = resources.getString(R.string.info_text_id),
            image = R.drawable.texto_thumb,
            title = resources.getString(R.string.info_text_title),
            subtitle = resources.getString(R.string.info_text_subtitle)
        ),
        InfoItem(
            id = resources.getString(R.string.info_music_id),
            image = R.drawable.musica_thumb,
            title = resources.getString(R.string.info_music_title),
            subtitle = resources.getString(R.string.info_music_subtitle)
        )
    )
}

fun secondColumnList(resources: Resources): List<InfoItem> {
    return listOf(
        InfoItem(
            id = resources.getString(R.string.info_tramoya_id),
            image = R.drawable.tramoya_thumb,
            title = resources.getString(R.string.info_tramoya_title),
            subtitle = resources.getString(R.string.info_tramoya_subtitle)
        ),
        InfoItem(
            id = resources.getString(R.string.info_celebration_id),
            image = R.drawable.festividad_thumb,
            title = resources.getString(R.string.info_celebration_title),
            subtitle = resources.getString(R.string.info_celebration_subtitle)
        ),
        InfoItem(
            id = resources.getString(R.string.info_recognition_id),
            image = R.drawable.reconocimientos_thumb,
            title = resources.getString(R.string.info_recognition_title),
            subtitle = resources.getString(R.string.info_recognition_subtitle)
        )
    )
}