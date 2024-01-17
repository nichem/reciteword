package com.example.recite.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.recite.R
import com.example.recite.base.App
import com.example.recite.base.App.Companion.wordManager
import com.example.recite.base.BaseActivity
import com.example.recite.databinding.ActivityHistoryBinding
import com.example.worddb.database.entity.Word
import com.example.worddb.utils.Common
import com.xuexiang.xui.widget.actionbar.TitleBar
import com.xuexiang.xui.widget.actionbar.TitleBar.TextAction
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import com.xuexiang.xui.widget.popupwindow.popup.XUIListPopup
import com.xuexiang.xui.widget.popupwindow.popup.XUISimplePopup
import kotlinx.coroutines.launch

class HistoryActivity : BaseActivity<ActivityHistoryBinding>() {
    override fun createBinding(): ActivityHistoryBinding =
        ActivityHistoryBinding.inflate(layoutInflater)

    private val adapter = HistoryAdapter()

    override fun initView() {
        binding.rv.adapter = adapter
        lifecycleScope.launch {
            val words = wordManager.getAllRecitedWords(wordManager.currentBookID)
            adapter.setList(words)
        }
    }

    override fun initTitleBar(bar: TitleBar) {
        super.initTitleBar(bar)
        bar.setTitle("背诵历史")
            .setLeftClickListener { finish() }
            .addAction(object : TextAction("清除") {
                override fun performAction(view: View?) {
                    resetDatabase()
                }
            })

    }

    private fun resetDatabase() {
        MaterialDialog.Builder(this)
            .iconRes(R.drawable.baseline_warning_24)
            .limitIconToDefaultSize()
            .title("警告")
            .content("此操作将清除你所有的背诵记录！")
            .positiveText("确认清除")
            .negativeText("点错了")
            .onPositive { _, _ ->
                val dialog = MaterialDialog.Builder(this)
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .content("清除中")
                    .cancelable(false)
                    .canceledOnTouchOutside(false)
                    .show()
                lifecycleScope.launch {
                    wordManager.resetDatabase()
                    val words = wordManager.getAllRecitedWords(wordManager.currentBookID)
                    adapter.setList(words)
                    dialog.dismiss()
                }
            }
            .show()
    }

}

class HistoryAdapter : BaseQuickAdapter<Word, BaseViewHolder>(R.layout.item_history) {
    override fun convert(holder: BaseViewHolder, item: Word) {
        holder.setText(R.id.tv_text, item.text)
        holder.setText(R.id.tv_trans_cn, item.tranCN)
        val sub = item.nextTime - Common.getNowDay()
        val review = if (sub <= 0) "今日需要复习"
        else "${sub}天后复习"
        holder.setText(R.id.tv_review, review)
    }

}