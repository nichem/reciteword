package com.example.recite.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.recite.base.App.Companion.wordManager
import com.example.recite.base.BaseActivity
import com.example.recite.databinding.ActivityQuestionBinding
import com.example.worddb.database.entity.Word
import com.xuexiang.xui.widget.actionbar.TitleBar
import kotlinx.coroutines.launch

class QuestionActivity : BaseActivity<ActivityQuestionBinding>() {
    private val viewModel by lazy {
        ViewModelProvider(this)[QuestionViewModel::class.java]
    }

    override fun createBinding(): ActivityQuestionBinding =
        ActivityQuestionBinding.inflate(layoutInflater)

    override fun initView() {
        viewModel.initQuestions()
        viewModel.question.observe(this) {
            setQuestion(it)
        }

        viewModel.questionState.observe(this) {
            if (it.isShowExplain) {
                binding.layoutExplain.expand()
                binding.btnLeft.text = "隐藏解析"
            } else {
                binding.layoutExplain.collapse()
                binding.btnLeft.text = "显示解析"
            }
            if (it.isAnswered) {
                binding.btnRight.text = "下一题"
                binding.questionSelector.setSelectEnable(false)
                binding.questionSelector.setSelectWithRightIndex(it.questionIndex, it.rightIndex)
            } else {
                binding.btnRight.text = "显示答案"
                binding.questionSelector.setSelectEnable(true)
                binding.questionSelector.setSelect(it.questionIndex)
            }
        }

        viewModel.subTitle.observe(this) {
            getTitleBar().setSubTitle(it)
        }

        binding.btnLeft.setOnClickListener {
            viewModel.leftClick()
        }
        binding.btnRight.setOnClickListener {
            viewModel.rightClick()
        }
        binding.questionSelector.callback = {
            viewModel.select(it)
        }
    }

    private fun setQuestion(question: Word?) {
        if (question == null) return
        binding.tvQuestion.text = question.question
        binding.tvExplain.text = question.explain
        binding.questionSelector.setAnswers(
            question.choiceIndexOne,
            question.choiceIndexTwo,
            question.choiceIndexThree,
            question.choiceIndexFour
        )
    }

    override fun initTitleBar(bar: TitleBar) {
        super.initTitleBar(bar)
        bar.setTitle("选择题")
            .setLeftClickListener { finish() }
    }
}

data class QuestionState(
    val isShowExplain: Boolean = false,
    val isAnswered: Boolean = false,
    val questionIndex: Int = -1 /*-1代表未选中*/,
    val rightIndex: Int = -1
)

class QuestionViewModel() : ViewModel() {
    private var questions: List<Word> = emptyList()

    private val _questionIndex = MutableLiveData<Int>()

    val question: LiveData<Word?> = _questionIndex.map {
        questions.getOrNull(it)
    }

    private val _questionState = MutableLiveData(QuestionState())
    val questionState: LiveData<QuestionState>
        get() = _questionState

    private val _subTitle = _questionIndex.map {
        "还剩${questions.size - it}道题"
    }

    val subTitle: LiveData<String>
        get() = _subTitle

    fun initQuestions() = viewModelScope.launch {
        questions = wordManager.getQuestions(false)
        _questionIndex.postValue(0)
    }

    fun rightClick() {
        if (questionState.value?.isAnswered == true) nextQuestion()
        else answerFinish()
    }

    fun leftClick() {
        val isShow = questionState.value?.isShowExplain ?: false
        showOrHideExplain(!isShow)
    }

    fun select(questionIndex: Int) {
        _questionState.postValue(_questionState.value?.copy(questionIndex = questionIndex))
    }

    private fun answerFinish() {
        val rightIndex = Math.abs(question.value?.rightIndex!!) - 1
        _questionState.postValue(
            _questionState.value?.copy(
                isAnswered = true,
                rightIndex = rightIndex
            )
        )
    }

    private fun nextQuestion() {
        val question = question.value
        viewModelScope.launch { wordManager.answerQuestion(question) }
        _questionIndex.postValue((_questionIndex.value ?: -1) + 1)
        _questionState.postValue(QuestionState())

    }

    private fun showOrHideExplain(isShow: Boolean) {
        _questionState.postValue(_questionState.value?.copy(isShowExplain = isShow))
    }
}