package com.example.mullak.pojo

import java.io.Serializable


data class AddBuildingResponse(
    val msg: String,
    val quotation: String,
    val status: Int
):Serializable