package com.example.lockscreenapp.bottom_tools

import com.example.lockscreenapp.R


data class Color(
    val id: Int,
    val color: Int,
    var isSelected: Boolean? = false
) {
    companion object {
        fun getListColor(): ArrayList<Color> = arrayListOf(
            Color(0, R.color.color1, true),
            Color(1, R.color.color2),
            Color(2, R.color.color3),
            Color(3, R.color.color4),
            Color(4, R.color.color5),
            Color(5, R.color.color6),
            Color(6, R.color.color7),
            Color(7, R.color.color8),
            Color(8, R.color.color9),
            Color(9, R.color.color10),
            Color(10, R.color.color11),
        )
    }
}
