package com.example.mullak.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mullak.api.RetrofitHandler
import com.example.mullak.pojo.BuildingResponse
import com.example.mullak.utils.Resource
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _data = MutableLiveData<Resource<BuildingResponse>>()
    val data: MutableLiveData<Resource<BuildingResponse>> get() = _data


    fun getData() = viewModelScope.launch {
        _data.value = Resource.loading(null)
        val response = RetrofitHandler.getItemWebService().getData()
        if (response.isSuccessful) {
            if (response.body() != null)
                _data.value = Resource.success(response.body()!!)
        } else {
            _data.value = Resource.error(response.errorBody().toString())
        }
    }

}