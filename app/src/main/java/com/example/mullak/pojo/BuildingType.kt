package com.example.mullak.pojo

data class BuildingType(
    val building_type_units: List<BuildingTypeUnit>,
    val created_at: String,
    val description: String,
    val description_ar: String,
    val id: Int,
    val image: String,
    val name: String,
    val name_ar: String,
    val status: Int,
    val updated_at: String
)