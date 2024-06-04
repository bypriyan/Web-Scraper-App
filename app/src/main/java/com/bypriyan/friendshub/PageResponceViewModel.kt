package com.bypriyan.friendshub

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PageResponceViewModel @Inject constructor(private val repository: WebsidteRepo):ViewModel(){

    private val _responce = MutableLiveData<String>()
    val responce: LiveData<String> = _responce

    fun getResponce(pageUrl:String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val responce = repository.getPageResponce(pageUrl)
                _responce.postValue(responce)
            }
        }
    }

}