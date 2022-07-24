package es.gorillapp.misteri_2022.data

import android.content.res.Resources
import es.gorillapp.misteri_2022.R

/* Returns initial list of flowers. */
fun sceneVespraList(resources: Resources): List<SceneItem> {
    return listOf(
        SceneItem(
            id = resources.getString(R.string.scene_vespra1),
            image = R.drawable.escenas_maria
        ),
        SceneItem(
            id = resources.getString(R.string.scene_vespra2),
            image = R.drawable.escenas_angel
        ),
        SceneItem(
            id = resources.getString(R.string.scene_vespra3),
            image = R.drawable.escenas_sanjuan
        ),
        SceneItem(
            id = resources.getString(R.string.scene_vespra4),
            image = R.drawable.escenas_sanpedro
        ),
        SceneItem(
            id = resources.getString(R.string.scene_vespra5),
            image = R.drawable.escenas_ternario
        ),
        SceneItem(
            id = resources.getString(R.string.scene_vespra6),
            image = R.drawable.escenas_salve
        ),
        SceneItem(
            id = resources.getString(R.string.scene_vespra7),
            image = R.drawable.escenas_dormicion
        ),
        SceneItem(
            id = resources.getString(R.string.scene_vespra8),
            image = R.drawable.escenas_araceli
        )
    )
}

fun sceneFestaList(resources: Resources): List<SceneItem> {
    return listOf(
        SceneItem(
            id = resources.getString(R.string.scene_festa1),
            image = R.drawable.escenas_invitacion
        ),
        SceneItem(
            id = resources.getString(R.string.scene_festa2),
            image = R.drawable.escenas_sanpedrosanjuan
        ),
        SceneItem(
            id = resources.getString(R.string.scene_festa3),
            image = R.drawable.escenas_apostoles
        ),
        SceneItem(
            id = resources.getString(R.string.scene_festa4),
            image = R.drawable.escenas_judios
        ),
        SceneItem(
            id = resources.getString(R.string.scene_festa5),
            image = R.drawable.escenas_conversion
        ),
        SceneItem(
            id = resources.getString(R.string.scene_festa6),
            image = R.drawable.escenas_entierro
        ),
        SceneItem(
            id = resources.getString(R.string.scene_festa7),
            image = R.drawable.escenas_asuncion
        ),
        SceneItem(
            id = resources.getString(R.string.scene_festa8),
            image = R.drawable.escenas_santomas
        ),
        SceneItem(
            id = resources.getString(R.string.scene_festa9),
            image = R.drawable.escenas_coronacion
        )
    )
}