package com.example.mullak.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mullak.api.RetrofitHandler
import com.example.mullak.pojo.AddBuildingResponse
import com.example.mullak.pojo.BuildingRequest
import com.example.mullak.utils.Resource
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {
    private var _addBuildingLiveData = MutableLiveData<Resource<AddBuildingResponse>>(Resource.loading(null))
    val addBuildingLiveData: LiveData<Resource<AddBuildingResponse>> get() = this._addBuildingLiveData

    fun addBuilding(request: BuildingRequest) {
        viewModelScope.launch {
            _addBuildingLiveData.value = Resource.loading(null)
            val response = RetrofitHandler.getItemWebService().addBuilding(request)
            if (response.isSuccessful) {
                if (response.body() != null) {
                    _addBuildingLiveData.value = Resource.success(response.body()!!)
                }
            } else {
                _addBuildingLiveData.value = Resource.error(response.code().toString())
            }
        }
    }


}