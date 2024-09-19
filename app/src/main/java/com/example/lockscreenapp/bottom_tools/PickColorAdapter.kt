package com.example.lockscreenapp.bottom_tools

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.lockscreenapp.R
import com.example.lockscreenapp.databinding.ItemColorBinding
import com.example.lockscreenapp.common.BaseRecyclerViewAdapter

class PickColorAdapter : BaseRecyclerViewAdapter<Color, ItemColorBinding>() {
    var onColorPick: (Int) -> Unit = {}
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_color
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ItemColorBinding, Color>, position: Int) {
        val item = items.getOrNull(position) ?: Color(0, R.color.color1)
        holder.binding.apply {
            viewColor.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(root.context, item.color))
            bgSelected.isVisible = item.isSelected == true
            container.clipToOutline = true
            root.setOnClickListener {
                onColorPick.invoke(item.color)
                items.forEach {
                    it.isSelected = it.color == item.color
                }
                notifyDataSetChanged()
            }
        }
    }
}
