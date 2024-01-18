package com.example.recite.ui

import com.blankj.utilcode.util.ActivityUtils
import com.example.recite.base.App.Companion.wordManager
import com.example.recite.base.BaseActivity
import com.example.recite.databinding.ActivitySettingBinding
import com.example.worddb.database.entity.BookID
import com.example.worddb.utils.Common
import com.xuexiang.xui.widget.actionbar.TitleBar
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import com.xuexiang.xui.widget.grouplist.XUICommonListItemView
import com.xuexiang.xui.widget.grouplist.XUIGroupListView


class SettingActivity : BaseActivity<ActivitySettingBinding>() {
    override fun createBinding(): ActivitySettingBinding =
        ActivitySettingBinding.inflate(layoutInflater)

    private lateinit var itemBook: XUICommonListItemView
    private lateinit var itemReciteHistory: XUICommonListItemView
    private lateinit var itemSkipToday: XUICommonListItemView
    private lateinit var itemQuestion: XUICommonListItemView
    override fun initView() {
        itemBook = binding.groupListView.createItemView("词书").apply {
            detailText = wordManager.currentBookID.bookName
        }
        itemReciteHistory = binding.groupListView.createItemView("背诵历史").apply {
            accessoryType = XUICommonListItemView.ACCESSORY_TYPE_CHEVRON
        }
        itemSkipToday = binding.groupListView.createItemView("今日跳过复习").apply {
            accessoryType = XUICommonListItemView.ACCESSORY_TYPE_SWITCH
            switch.isChecked = wordManager.isSkipTodayReview()
            switch.setOnCheckedChangeListener { _, isChecked ->
                wordManager.skipToday = if (isChecked) Common.getNowDay() else 0
            }
        }
        itemQuestion = binding.groupListView.createItemView("做下选择题").apply {
            accessoryType = XUICommonListItemView.ACCESSORY_TYPE_CHEVRON
        }
        val itemAbout = binding.groupListView.createItemView("关于")
        XUIGroupListView.newSection(this)
            .addItemView(itemBook) {
                setBook()
            }
            .addItemView(itemQuestion) {
                ActivityUtils.startActivity(QuestionActivity::class.java)
            }
            .addItemView(itemReciteHistory) {
                ActivityUtils.startActivity(HistoryActivity::class.java)
            }
            .addItemView(itemSkipToday) {
                itemSkipToday.switch.isChecked = !itemSkipToday.switch.isChecked
            }
            .addItemView(itemAbout) {
                about()
            }
            .addTo(binding.groupListView)
    }


    override fun initTitleBar(bar: TitleBar) {
        super.initTitleBar(bar)
        bar.setTitle("更多")
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


    private fun about() {
        val packageInfo = packageManager.getPackageInfo(
            packageName, 0
        )
        val versionName: String = packageInfo.versionName

        MaterialDialog.Builder(this)
            .content("作者：dlearn\n版本：${versionName}")
            .positiveText("确认")
            .show()
    }
}