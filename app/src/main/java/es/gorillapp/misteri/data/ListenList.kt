package es.gorillapp.misteri.data

import android.content.res.Resources
import es.gorillapp.misteri.R

/* Returns initial list of flowers. */
fun sceneVespraList(resources: Resources): List<SceneItem> {
    return listOf(
        SceneItem(
            numero = 1,
            nombre = resources.getString(R.string.scene_vespra1),
            image = R.drawable.escenas_maria
        ),
        SceneItem(
            numero = 8,
            nombre = resources.getString(R.string.scene_vespra2),
            image = R.drawable.escenas_angel
        ),
        SceneItem(
            numero = 12,
            nombre = resources.getString(R.string.scene_vespra3),
            image = R.drawable.escenas_sanjuan
        ),
        SceneItem(
            numero = 19,
            nombre = resources.getString(R.string.scene_vespra4),
            image = R.drawable.escenas_sanpedro
        ),
        SceneItem(
            numero = 20,
            nombre = resources.getString(R.string.scene_vespra5),
            image = R.drawable.escenas_ternario
        ),
        SceneItem(
            numero = 24,
            nombre = resources.getString(R.string.scene_vespra6),
            image = R.drawable.escenas_salve
        ),
        SceneItem(
            numero = 28,
            nombre = resources.getString(R.string.scene_vespra7),
            image = R.drawable.escenas_dormicion
        ),
        SceneItem(
            numero = 30,
            nombre = resources.getString(R.string.scene_vespra8),
            image = R.drawable.escenas_araceli
        )
    )
}

fun sceneFestaList(resources: Resources): List<SceneItem> {
    return listOf(
        SceneItem(
            numero = 31,
            nombre = resources.getString(R.string.scene_festa1),
            image = R.drawable.escenas_invitacion
        ),
        SceneItem(
            numero = 35,
            nombre = resources.getString(R.string.scene_festa2),
            image = R.drawable.escenas_sanpedrosanjuan
        ),
        SceneItem(
            numero = 37,
            nombre = resources.getString(R.string.scene_festa3),
            image = R.drawable.escenas_apostoles
        ),
        SceneItem(
            numero = 40,
            nombre = resources.getString(R.string.scene_festa4),
            image = R.drawable.escenas_judios
        ),
        SceneItem(
            numero = 46,
            nombre = resources.getString(R.string.scene_festa5),
            image = R.drawable.escenas_conversion
        ),
        SceneItem(
            numero = 52,
            nombre = resources.getString(R.string.scene_festa6),
            image = R.drawable.escenas_entierro
        ),
        SceneItem(
            numero = 58,
            nombre = resources.getString(R.string.scene_festa7),
            image = R.drawable.escenas_asuncion
        ),
        SceneItem(
            numero = 59,
            nombre = resources.getString(R.string.scene_festa8),
            image = R.drawable.escenas_santomas
        ),
        SceneItem(
            numero = 62,
            nombre = resources.getString(R.string.scene_festa9),
            image = R.drawable.escenas_coronacion
        )
    )
}