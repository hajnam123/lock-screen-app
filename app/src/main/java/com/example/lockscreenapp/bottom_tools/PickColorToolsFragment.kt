package com.example.lockscreenapp.bottom_tools

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.lockscreenapp.common.BaseFragment
import com.example.lockscreenapp.R
import com.example.lockscreenapp.databinding.FragmentPickColorToolsBinding
import com.example.lockscreenapp.ultis.setOnSingleClickListener
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener


class PickColorToolsFragment : BaseFragment<FragmentPickColorToolsBinding>() {
    interface PickColorListener {
        fun onColorPick(color: Int)
        fun onColorSelected(color: Int)
    }

    override val layoutId: Int
        get() = R.layout.fragment_pick_color_tools
    private lateinit var pickColorAdapter: PickColorAdapter
    private var pickColor: PickColorListener? = null
    private val viewModel: PickColorViewModel by viewModels()

    companion object {
        private const val TITLE = "TITLE"
        fun newInstance(
            title: String,
            listener: PickColorListener
        ): PickColorToolsFragment {
            val args = Bundle()
            args.putString(TITLE, title)
            val fragment = PickColorToolsFragment()
            fragment.pickColor = listener
            fragment.arguments = args
            return fragment
        }
    }

    fun setListener(listener: PickColorListener) {
        pickColor = listener
    }

    override fun setupUI() {
        super.setupUI()
        pickColorAdapter = PickColorAdapter().apply {
            updateData(viewModel.dataColor)
            onColorPick = { colorRes ->
                context?.let {
                    pickColor?.onColorPick(ContextCompat.getColor(it, colorRes))
                }
            }
        }
        binding.apply {
            recyclerViewColors.adapter = pickColorAdapter
            buttonColorPicker.setOnSingleClickListener {
                showColorPickerDialog()
            }
        }
    }

    private fun showColorPickerDialog() {
        ColorPickerDialog.Builder(context)
            .setTitle("ColorPicker Dialog")
            .setPreferenceName("MyColorPickerDialog")
            .setNegativeButton(
                "R.string.cancel"
            ) { dialogInterface, _ -> dialogInterface?.dismiss() }
            .setPositiveButton(
                "R.string.ok", object : ColorEnvelopeListener {
                    override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                        envelope?.color?.let {
                            pickColor?.onColorSelected(it)
                            viewModel.unSelect()
                            binding.recyclerViewColors.adapter?.notifyDataSetChanged()
                        }
                    }
                }
            )
            .attachAlphaSlideBar(true)
            .attachBrightnessSlideBar(true)
            .setBottomSpace(12)
            .show()
    }
}
