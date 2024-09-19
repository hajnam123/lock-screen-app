package com.example.lockscreenapp.bottom_tools

import androidx.lifecycle.ViewModel

class PickColorViewModel : ViewModel() {
    var dataColor = Color.getListColor()
    fun unSelect(){
        dataColor.forEach {
            it.isSelected = false
        }
    }
}