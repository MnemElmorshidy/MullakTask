package com.example.mullak.pojo

import java.io.Serializable


data class BuildingRequest(
    val app_user_id: String="5c765510-41a7-11ea-8db4-353975648f74",
    var building_address: String="",
    var building_name: String="",
    var building_qty: Int=0,
    var building_type_id: String="",
    var building_unit_id: Int=0,
    var lat: Double=0.0,
    var lng: Double=0.0,
    var services: List<Service> = listOf()
):Serializable