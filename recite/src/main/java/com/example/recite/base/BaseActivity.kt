package com.example.recite.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.recite.databinding.ActivityBaseBinding
import com.xuexiang.xui.widget.actionbar.TitleBar

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    val binding: T by lazy {
        createBinding()
    }

    private val baseBinding: ActivityBaseBinding by lazy {
        ActivityBaseBinding.inflate(layoutInflater)
    }

    abstract fun createBinding(): T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseBinding.fl.addView(binding.root)
        if (hideTitleBar()) baseBinding.titleBar.visibility = View.GONE
        else initTitleBar(baseBinding.titleBar)
        setContentView(baseBinding.root)
        initView()
    }

    abstract fun initView()

    open fun initTitleBar(bar: TitleBar) {}

    open fun hideTitleBar(): Boolean = false

    fun getTitleBar() = baseBinding.titleBar

    fun addOver(view: View) {
        baseBinding.flOver.addView(view)
    }
}