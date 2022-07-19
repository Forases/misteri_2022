package es.gorillapp.misteri_2022.data

import es.gorillapp.misteri_2022.sceneList.SceneListActivity

data class SceneItem (
    val id: Int,
    val act: SceneListActivity.Act,
    val nbFirstSlide: Int,
    val thumbnail: String?,
    val name: String
)