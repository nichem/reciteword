package com.example.recite.ui

import androidx.lifecycle.lifecycleScope
import com.example.recite.R
import com.example.recite.base.App.Companion.wordManager
import com.example.recite.base.BaseActivity
import com.example.recite.databinding.ActivitySettingBinding
import com.example.worddb.database.entity.BookID
import com.xuexiang.xui.widget.actionbar.TitleBar
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import com.xuexiang.xui.widget.grouplist.XUICommonListItemView
import com.xuexiang.xui.widget.grouplist.XUIGroupListView
import kotlinx.coroutines.launch

class SettingActivity : BaseActivity<ActivitySettingBinding>() {
    override fun createBinding(): ActivitySettingBinding =
        ActivitySettingBinding.inflate(layoutInflater)

    private lateinit var itemBook: XUICommonListItemView
    private lateinit var itemClear: XUICommonListItemView
    override fun initView() {
        itemBook = binding.groupListView.createItemView("词书").apply {
            detailText = wordManager.currentBookID.bookName
        }
        itemClear = binding.groupListView.createItemView("清除背诵记录")
        XUIGroupListView.newSection(this)
            .addItemView(itemBook) {
                setBook()
            }
            .addItemView(itemClear) {
                MaterialDialog.Builder(this)
                    .iconRes(R.drawable.baseline_warning_24)
                    .limitIconToDefaultSize()
                    .title("警告")
                    .content("此操作将清除你所有的背诵记录！")
                    .positiveText("确认清除")
                    .negativeText("点错了")
                    .onPositive { _, _ ->
                        resetDatabase()
                    }
                    .show()
            }
            .addTo(binding.groupListView)
    }


    override fun initTitleBar(bar: TitleBar) {
        super.initTitleBar(bar)
        bar.setTitle("设置")
            .setLeftClickListener {
                finish()
            }
    }

    private fun setBook() {
        val bookList = enumValues<BookID>().toList()
        val bookIndex = enumValues<BookID>().indexOf(wordManager.currentBookID)
        MaterialDialog.Builder(this)
            .items(bookList.map {
                it.bookName
            })
            .itemsCallbackSingleChoice(bookIndex) { _, _, which, _ ->
                wordManager.currentBookID = bookList[which]
                itemBook.detailText = wordManager.currentBookID.bookName
                true
            }
            .positiveText("确认")
            .show()
    }

    private fun resetDatabase() {
        val dialog = MaterialDialog.Builder(this)
            .progress(true, 0)
            .progressIndeterminateStyle(false)
            .content("清除中")
            .cancelable(false)
            .canceledOnTouchOutside(false)
            .show()
        lifecycleScope.launch {
            wordManager.resetDatabase()
            dialog.dismiss()
        }
    }
}