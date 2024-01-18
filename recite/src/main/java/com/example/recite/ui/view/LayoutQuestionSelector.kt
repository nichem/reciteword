package com.example.recite.ui.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.blankj.utilcode.util.ConvertUtils
import com.example.recite.databinding.LayoutQuestionSelectorBinding
import com.xuexiang.xui.utils.DrawableUtils

class LayoutQuestionSelector : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var binding: LayoutQuestionSelectorBinding =
        LayoutQuestionSelectorBinding.inflate(LayoutInflater.from(context), this, true)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setClicks(binding.ll1, binding.ll2, binding.ll3, binding.ll4)
        setSelect(-1)
    }

    private fun setClicks(vararg layout: LinearLayout) {
        layout.forEachIndexed { index, linearLayout ->
            linearLayout.setOnClickListener {
                if (selectEnable) callback(index)
            }
        }
    }

    var callback: (Int) -> Unit = {}

    fun setAnswers(vararg answer: String) {
        binding.tv1.text = answer[0]
        binding.tv2.text = answer[1]
        binding.tv3.text = answer[2]
        binding.tv4.text = answer[3]
    }

    fun setSelect(selectIndex: Int) {
        val tvs = listOf(binding.tv1, binding.tv2, binding.tv3, binding.tv4)
        val images = listOf(binding.img1, binding.img2, binding.img3, binding.img4)
        val texts = listOf("A", "B", "C", "D")
        tvs.forEachIndexed { index, textView ->
            textView.setTextColor(if (selectIndex == index) selectColor else fontColor)
        }
        images.forEachIndexed { index, imageView ->
            imageView.setImageDrawable(
                createOption(
                    texts[index],
                    if (index == selectIndex) selectColor else Color.WHITE,
                    if (index == selectIndex) Color.WHITE else fontColor
                )
            )
        }
    }

    fun setSelectWithRightIndex(selectIndex: Int, rightIndex: Int) {
        val tvs = listOf(binding.tv1, binding.tv2, binding.tv3, binding.tv4)
        val images = listOf(binding.img1, binding.img2, binding.img3, binding.img4)
        val texts = listOf("A", "B", "C", "D")
        tvs.forEachIndexed { index, textView ->
            val textColor = if (rightIndex == index) {
                correctColor
            } else if (selectIndex != rightIndex && selectIndex == index) {
                errorColor
            } else {
                fontColor
            }
            textView.setTextColor(textColor)
        }
        images.forEachIndexed { index, imageView ->
            val bgColor = if (rightIndex == index) {
                correctColor
            } else if (selectIndex != rightIndex && selectIndex == index) {
                errorColor
            } else {
                Color.WHITE
            }

            imageView.setImageDrawable(
                createOption(
                    texts[index],
                    bgColor,
                    if (index == selectIndex || index == rightIndex) Color.WHITE else fontColor
                )
            )
        }
    }

    private var selectEnable = true
    fun setSelectEnable(enable: Boolean) {
        selectEnable = enable
    }

    private val fontColor =
        resources.getColor(com.xuexiang.xui.R.color.xui_config_color_middle_blue_gray)
    private val selectColor =
        resources.getColor(com.xuexiang.xui.R.color.xui_config_color_main_theme)
    private val errorColor = resources.getColor(com.xuexiang.xui.R.color.xui_config_color_red)

    private val correctColor =
        resources.getColor(com.xuexiang.xui.R.color.xui_btn_green_normal_color)

    private fun createOption(a: String, bgColor: Int, fontColor: Int): BitmapDrawable {
        return DrawableUtils.createCircleDrawableWithText(
            resources, ConvertUtils.dp2px(40f), bgColor, a, ConvertUtils.sp2px(18f) * 1f,
            fontColor
        )
    }
}