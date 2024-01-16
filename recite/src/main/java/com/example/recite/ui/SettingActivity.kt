package com.example.recite.ui

import com.example.recite.base.App.Companion.wordManager
import com.example.recite.base.BaseActivity
import com.example.recite.databinding.ActivitySettingBinding
import com.example.worddb.database.entity.BookID
import com.xuexiang.xui.widget.actionbar.TitleBar
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import com.xuexiang.xui.widget.grouplist.XUIGroupListView

class SettingActivity : BaseActivity<ActivitySettingBinding>() {
    override fun createBinding(): ActivitySettingBinding =
        ActivitySettingBinding.inflate(layoutInflater)

    override fun initView() {
        val itemBook = binding.groupListView.createItemView("词书").apply {
            detailText = wordManager.currentBookID.bookName
        }
        XUIGroupListView.newSection(this)
            .addItemView(itemBook) {
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
            .addTo(binding.groupListView)
    }


    override fun initTitleBar(bar: TitleBar) {
        super.initTitleBar(bar)
        bar.setTitle("设置")
            .setLeftClickListener {
                finish()
            }
    }
}