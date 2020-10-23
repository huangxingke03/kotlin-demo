package com.fitgreat.doctorface_top.model

import java.io.Serializable

open class ExpertListData(
    val type: String?,
    val msg: MutableList<Msg>
) {
    class Msg(
        val id: Int,
        val name: String?,
        val department: String?,
        val academic_title: String?,
        val specialty: String?,
        val image: String?,
        val synopsis: String?,
        val container: String?,
        val image_url: String?
    ):Serializable
}