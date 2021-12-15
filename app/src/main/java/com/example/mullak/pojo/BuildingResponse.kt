package com.example.mullak.pojo

import java.io.Serializable

data class BuildingResponse(
    val building_types: List<BuildingType>,
    val maintenance_types: List<MaintenanceType>,
    val payment_methods: List<Any>
):Serializable