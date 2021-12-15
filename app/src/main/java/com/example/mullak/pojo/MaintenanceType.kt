package com.example.mullak.pojo

import java.io.Serializable

data class MaintenanceType(
    val created_at: String,
    val description: String,
    val description_ar: String,
    val id: Int,
    val image: String,
    val maintenance_type_option: Int,
    val maintenance_type_services: List<MaintenanceTypeService>,
    val name: String,
    val name_ar: String,
    val updated_at: String
):Serializable