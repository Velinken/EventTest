package com.dinadurykina.eventtest.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dinadurykina.eventtest.util.Event

class FragmentViewModel : ViewModel() {

    private val _toast =  MutableLiveData<Event<String?>>()
    val toast: LiveData<Event<String?>>
        get() = _toast

    fun onToast(text:String){
        _toast.value = Event(text)
    }

    private val _keyBoard =  MutableLiveData<Event<Boolean?>>()
    val keyBoard: LiveData<Event<Boolean?>>
        get() = _keyBoard

    fun keybooardON(){
        _keyBoard.value = Event(true)
    }

    fun keybooardOFF(){
        _keyBoard.value = Event(false)
    }

    private val _snackbar =  MutableLiveData<Event<String?>>()
    val snackbar: LiveData<Event<String?>>
        get() = _snackbar

    fun onSnackbar(){
        _snackbar.value = Event("Snackbar text")
    }
}