package com.example.lockscreenapp

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.example.lockscreenapp.common.BaseFragment
import com.example.lockscreenapp.bottom_tools.PickColorToolsFragment
import com.example.lockscreenapp.bottom_tools.SliderToolsFragment
import com.example.lockscreenapp.bottom_tools.TypeTool
import com.example.lockscreenapp.databinding.FragmentDrawingBinding
import com.example.lockscreenapp.ultis.gone
import com.example.lockscreenapp.ultis.setOnSingleClickListener
import com.example.lockscreenapp.ultis.visible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DrawingFragment : BaseFragment<FragmentDrawingBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_drawing

    companion object {
        fun newInstance() = DrawingFragment()
    }
    private var penFragment: SliderToolsFragment? = null
    private var penColorFragment: PickColorToolsFragment? = null
    private var eraserFragment: SliderToolsFragment? = null
    private var bgColorFragment: PickColorToolsFragment? = null

    private var currentColor: Int? = context?.getColor(R.color.color1)
    private var currentSizePaint = 9

    override fun setupUI() {
        super.setupUI()
        binding.buttonPen.isSelected = true
        createFragmentTool()
        showToolFragment(penFragment)
        context?.let {
            binding.paintView.paintColor(
                ContextCompat.getColor(
                    it,
                    R.color.color1
                )
            )
        }
    }

    private fun createFragmentTool() {
        penFragment = SliderToolsFragment.newInstance(
            title1 = getString(R.string.pen_tools),
            title2 = getString(R.string.opacity),
            object : SliderToolsFragment.OnSliderChangedListener {
                override fun onResumeValueSlider(value1: Int, value2: Int) {
                    binding.paintView.paintStroke(value1.toInt())
                    currentSizePaint = value1
                }

                override fun onSliderFirstChanged(value: Int) {
                    binding.paintView.paintStroke(value)
                    binding.paintView.setSizeCircleTouch(value.toFloat())
                }

                override fun onSliderSecondChanged(value: Int) {
                    binding.paintView.paintAlpha(value)
                }

            }
        )
        penColorFragment = PickColorToolsFragment.newInstance(
            title = getString(R.string.pen_color),
            object : PickColorToolsFragment.PickColorListener {
                override fun onColorPick(color: Int) {
                    currentColor = color
                    context?.let {
                        binding.paintView.paintColor(color)
                    }
                }

                override fun onColorSelected(color: Int) {
                    currentColor = color
                    context?.let {
                        binding.paintView.paintColor(color)
                    }
                }

            }
        )
        eraserFragment = SliderToolsFragment.newInstance(
            title1 = getString(R.string.eraser),
            title2 = getString(R.string.opacity),
            object : SliderToolsFragment.OnSliderChangedListener {
                override fun onResumeValueSlider(value1: Int, value2: Int) {
                    binding.paintView.paintStroke(value1)
                }

                override fun onSliderFirstChanged(value: Int) {
                    context?.getColor(R.color.white)
                        ?.let { binding.paintView.paintColor(it) }
                    binding.paintView.paintStroke(value)
                    binding.paintView.setSizeCircleTouchEraser(value.toFloat())
                }

                override fun onSliderSecondChanged(value: Int) {
                    binding.paintView.paintAlpha(value)
                }

            }
        )
        bgColorFragment = PickColorToolsFragment.newInstance(
            title = getString(R.string.background_color),
            object : PickColorToolsFragment.PickColorListener {
                override fun onColorPick(color: Int) {

                }

                override fun onColorSelected(color: Int) {

                }
            }
        )
    }

    override fun setupListener() {
        super.setupListener()
        binding.apply {
            buttonClose.setOnSingleClickListener {

            }
            buttonPen.setOnSingleClickListener {
                showToolFragment(penFragment)
                currentColor?.let { it1 -> paintView.paintColor(it1) }
                selectTool(TypeTool.PEN)
            }
            buttonPenColor.setOnSingleClickListener {
                showToolFragment(penColorFragment)
                currentColor?.let { it1 -> paintView.paintColor(it1) }
                selectTool(TypeTool.PEN_COLOR)
            }
            buttonErase.setOnSingleClickListener {
                showToolFragment(eraserFragment)
                paintView.paintStroke(currentSizePaint)
                selectTool(TypeTool.ERASER)
                context?.getColor(R.color.white)
                    ?.let { binding.paintView.paintColor(it) }
            }
            buttonBGColor.setOnSingleClickListener {
                showToolFragment(bgColorFragment)
            }
            buttonUndo.setOnClickListener {
                paintView.onClickUndo()
                if (paintView.getSizePath() <= 0) {
                    buttonUndo.setImageResource(R.drawable.img_undo_disable)
                }
                updateButtonRedo()
                updateUIButtonSave()
            }
            buttonRedo.setOnClickListener {
                paintView.onClickRedo()
                if (paintView.getSizePath() > 0) {
                    buttonUndo.setImageResource(R.drawable.img_undo)
                }
                updateButtonRedo()
                updateUIButtonSave()
            }
            buttonCollapse.setOnSingleClickListener {
                if (containerToolbox.isGone) {
                    containerToolbox.visible()
                    buttonCollapse.setImageResource(R.drawable.ic_collap_draw)
                } else {
                    containerToolbox.gone()
                    buttonCollapse.setImageResource(R.drawable.ic_expand_draw)
                }
            }
            paintView.setOnDraw {
                updateUIButtonSave()
                if (paintView.getSizePath() > 0) {
                    buttonUndo.setImageResource(R.drawable.img_undo)
                } else {
                    buttonUndo.setImageResource(R.drawable.img_undo_disable)
                }
            }
        }
    }

    private fun updateUIButtonSave() {
        binding.apply {
            if (paintView.getSizePath() > 0) {
                //Change State Button Undo
                buttonSave.setTextColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            buttonSave.context, R.color.white
                        )
                    )
                )
                buttonSave.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(buttonSave.context, R.color.primaryBlue50C)
                )
            } else {
                buttonSave.setTextColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            buttonSave.context, R.color.neutral40
                        )
                    )
                )
                buttonSave.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(buttonSave.context, R.color.neutral10)
                )
            }
        }
    }

    private fun updateButtonRedo() {
        binding.apply {
            if (paintView.getSizeUndonePath() > 0) {
                buttonRedo.setImageResource(R.drawable.img_redo)
            } else {
                buttonRedo.setImageResource(R.drawable.img_redo_disable)
            }
        }
    }

    private fun showToolFragment(fragment: Fragment?) {
        fragment?.let {
            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.container_toolbox, it)
            transaction.commit()
        }
    }

    private fun selectTool(type: TypeTool) {
        binding.apply {
            when (type) {
                TypeTool.PEN -> {
                    buttonPen.isSelected = true
                    buttonPenColor.isSelected = false
                    buttonErase.isSelected = false
                }

                TypeTool.PEN_COLOR -> {
                    buttonPen.isSelected = false
                    buttonPenColor.isSelected = true
                    buttonErase.isSelected = false
                }

                TypeTool.ERASER -> {
                    buttonPen.isSelected = false
                    buttonPenColor.isSelected = false
                    buttonErase.isSelected = true
                }
            }
        }
    }
}
