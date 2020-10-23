package com.fitgreat.doctorface_top.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.fitgreat.doctorface_top.MyApp
import com.fitgreat.doctorface_top.R
import com.fitgreat.doctorface_top.util.UIUtils

/**
 * 下载文件进度提示弹窗
 */
class MyDownloadDialog : DialogFragment() {
    private var messageView: TextView? = null
    private var updateProgressView: ProgressBar? = null
    private var percentView: TextView? = null

    companion object {
        /**
         * 初始化获取 BaseDialogFragment 对象
         */
        fun newInstance(): MyDownloadDialog {
            //            var bundle = Bundle()
//            baseDialogFragment.arguments = bundle
            return MyDownloadDialog()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        //初始化view
        val inflate = LayoutInflater.from(activity).inflate(R.layout.dialog_my_download, null)
        messageView = inflate.findViewById<TextView>(R.id.message)
        updateProgressView = inflate.findViewById<ProgressBar>(R.id.update_progress)
        percentView = inflate.findViewById<TextView>(R.id.percent)
        builder.setView(inflate)
        return builder.create()
    }

    override fun onResume() {
        val attributes = dialog?.window?.attributes
        attributes?.height =ConstraintLayout.LayoutParams.WRAP_CONTENT
        attributes?.width = UIUtils.dp2px(MyApp.getContext(), 416)
        attributes?.gravity=Gravity.CENTER
        dialog?.window?.attributes = attributes
        super.onResume()
    }

    /**
     * 更新下载进度显示
     */
    fun updateProgress(progress: Int) {
        updateProgressView?.let {
            it.progress = progress
        }
        percentView?.let {
            it.text = "$progress%"
        }
    }

    /**
     * 更新弹窗显示内容
     */
    fun setContent(msg: String?) {
        messageView?.let {
            it.text = msg
        }
    }
}