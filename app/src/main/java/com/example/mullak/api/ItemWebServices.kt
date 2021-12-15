package com.example.mullak.api

import com.example.mullak.pojo.AddBuildingResponse
import com.example.mullak.pojo.BuildingRequest
import com.example.mullak.pojo.BuildingResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ItemWebServices {

    @GET("basic-settings")
    suspend fun getData(): Response<BuildingResponse>

    @POST("building/create")
    suspend fun addBuilding(@Body request: BuildingRequest) : Response<AddBuildingResponse>

}