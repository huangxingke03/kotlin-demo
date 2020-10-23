package com.fitgreat.doctorface_top.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.fitgreat.doctorface_top.constants.ConstantsString.BASE_DIALOG_CONTENT
import com.fitgreat.doctorface_top.constants.ConstantsString.BASE_DIALOG_NO
import com.fitgreat.doctorface_top.constants.ConstantsString.BASE_DIALOG_TITLE
import com.fitgreat.doctorface_top.constants.ConstantsString.BASE_DIALOG_YES
import com.fitgreat.doctorface_top.MyApp
import com.fitgreat.doctorface_top.R
import com.fitgreat.doctorface_top.util.UIUtils

class MyDialogYesOrNo : DialogFragment(), OnCheckedChangeListener {
    /**
     * 选择按钮点击事件监听
     */
    interface MyCheckChangeListener {
        fun checkYes()
        fun checkNo()
    }

    private var mMyCheckChangeListener: MyCheckChangeListener? = null

    /**
     * 设置选择按钮事件监听
     */
    fun setMyCheckChangeListener(myCheckChangeListener: MyCheckChangeListener) {
        this.mMyCheckChangeListener = myCheckChangeListener
    }

    companion object {
        /**
         * 初始化获取 BaseDialogFragment 对象
         */
        fun newInstance(
            titleValue: String,
            contentValue: String,
            yesValue: String,
            noValue: String
        ): MyDialogYesOrNo {
            var baseDialogFragment = MyDialogYesOrNo()
            var bundle = Bundle()
            bundle.putString(BASE_DIALOG_TITLE, titleValue)
            bundle.putString(BASE_DIALOG_CONTENT, contentValue)
            bundle.putString(BASE_DIALOG_YES, yesValue)
            bundle.putString(BASE_DIALOG_NO, noValue)
            baseDialogFragment.arguments = bundle
            return baseDialogFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val titleValue = arguments?.getString(BASE_DIALOG_TITLE)
        val contentValue = arguments?.getString(BASE_DIALOG_CONTENT)
        val yesBtValue = arguments?.getString(BASE_DIALOG_YES)
        val noBtValue = arguments?.getString(BASE_DIALOG_NO)
        //初始化view
        val inflate = LayoutInflater.from(activity).inflate(R.layout.dialog_my_fragment, null)
        val baseTitleView = inflate.findViewById<TextView>(R.id.base_title)
        val baseContentView = inflate.findViewById<TextView>(R.id.base_content)
        val answerRadioGroupView = inflate.findViewById<RadioGroup>(R.id.answer_radioGroup)
        val answerNoBtView = inflate.findViewById<RadioButton>(R.id.answer_no_bt)
        val answerYesBtView = inflate.findViewById<RadioButton>(R.id.answer_yes_bt)
        //设置弹窗内容
        baseTitleView.text = titleValue
        baseContentView.text = contentValue
        answerNoBtView.text = noBtValue
        answerYesBtView.text = yesBtValue
        builder.setView(inflate)
        //设置按钮点击事件
        answerRadioGroupView.setOnCheckedChangeListener(this)
        return builder.create()
    }

    override fun onResume() {
        val attributes = dialog?.window?.attributes
        attributes?.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        attributes?.width = UIUtils.dp2px(MyApp.getContext(), 416)
        dialog?.window?.attributes = attributes
        super.onResume()
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        mMyCheckChangeListener?.let {
            if (checkedId == R.id.answer_yes_bt) {
                it.checkYes()
            } else if (checkedId == R.id.answer_no_bt) {
                it.checkNo()
            }
        }
    }
}