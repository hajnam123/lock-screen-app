package com.example.lockscreenapp.bottom_tools

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.example.lockscreenapp.common.BaseFragment
import com.example.lockscreenapp.R
import com.example.lockscreenapp.databinding.FragmentToolDrawSeekbarBinding
import com.google.android.material.slider.Slider

class SliderToolsFragment : BaseFragment<FragmentToolDrawSeekbarBinding>() {

    interface OnSliderChangedListener {
        fun onResumeValueSlider(value1: Int, value2: Int)
        fun onSliderFirstChanged(value: Int)
        fun onSliderSecondChanged(value: Int)

    }

    override val layoutId: Int
        get() = R.layout.fragment_tool_draw_seekbar
    private var listener: OnSliderChangedListener? = null
    private val viewModel: SliderToolViewModel by viewModels()
    fun setListener(listener: OnSliderChangedListener) {
        this.listener = listener
    }

    companion object {
        private const val TITLE1 = "TITLE1"
        private const val TITLE2 = "TITLE2"
        fun newInstance(
            title1: String,
            title2: String,
            listener: OnSliderChangedListener
        ): SliderToolsFragment {
            val fragment = SliderToolsFragment()
            fragment.listener = listener
            fragment.arguments = Bundle().apply {
                putString(TITLE1, title1)
                putString(TITLE2, title2)
            }
            return fragment
        }
    }

    override fun setupUI() {
        super.setupUI()
        binding.apply {
            textview1.text = viewModel.valueSlider1.toString()
            textview2.text = viewModel.valueSlider2.toString() + "%"
            listener?.onResumeValueSlider(viewModel.valueSlider1, viewModel.valueSlider2)
            arguments?.let { bundle ->
                bundle.getString(TITLE1).let { textviewTitle1.text = it }
                bundle.getString(TITLE2).let { textviewTitle2.text = it }
            }
            slider1.addOnChangeListener(Slider.OnChangeListener { _, value, fromUser ->
                if (fromUser) {
                    textview1.text = value.toInt().toString()
                    viewModel.valueSlider1 = value.toInt()
                    listener?.onSliderFirstChanged(value.toInt())
                }
            })
            slider2.addOnChangeListener(Slider.OnChangeListener { _, value, fromUser ->
                if (fromUser) {
                    textview2.text = "${((value / 255) * 100).toInt()} %"
                    viewModel.valueSlider2 = ((value / 255) * 100).toInt()
                    listener?.onSliderSecondChanged(value.toInt())
                }
            })
        }
    }

    override fun setupData() {
        super.setupData()
    }
}