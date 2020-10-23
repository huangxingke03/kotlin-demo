package com.fitgreat.doctorface_top.experts.info

import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.alibaba.fastjson.JSON
import com.fitgreat.doctorface_top.R
import com.fitgreat.doctorface_top.base.BaseMvpActivity
import com.fitgreat.doctorface_top.constants.ConstantsString.EXPERT_DATA
import com.fitgreat.doctorface_top.constants.ConstantsString.EXPERT_DATA_BUNDLE
import com.fitgreat.doctorface_top.model.ExpertListData
import com.fitgreat.doctorface_top.util.LogUtils
import com.fitgreat.doctorface_top.util.ToastUtils
import com.fitgreat.doctorface_top.view.BaseTitleView
import com.fitgreat.doctorface_top.view.MyDialogYesOrNo
import com.fitgreat.doctorface_top.view.MyDownloadDialog
import com.github.barteksc.pdfviewer.listener.*
import kotlinx.android.synthetic.main.activity_expert_info.*
import java.io.File
import java.util.*

/**
 * 专家详情页
 */
class ExpertInfoActivity : BaseMvpActivity<ExpertInfoPresenterImp, ExpertInfoView>(),
    BaseTitleView.BaseBackListener, ExpertInfoView {
    private val TAG = "ExpertInfoActivity"
    private var totalPage = 0
    private var myDialogYesOrNo: MyDialogYesOrNo? = null
    private var finishedOne: Boolean = false
    private var mPdfFile: File? = null
    var playPdfTimer: Timer? = null
    private var myDownloadDialog: MyDownloadDialog? = null

    //pdf播放
    private val pdfTurnTime: Long = 4000


    override fun getLayoutResource(): Int {
        return R.layout.activity_expert_info
    }

    override fun initData() {
        //返回键点击事件
        expert_info_title.setBackKListener(this)
        val bundleExtra = intent.getBundleExtra(EXPERT_DATA_BUNDLE)
        val msg = bundleExtra.getSerializable(EXPERT_DATA) as ExpertListData.Msg
        LogUtils.json(TAG, "专家详情页\t${JSON.toJSONString(msg)}")
        mPresenter?.getExpertInfo(msg)
        speechManager!!.textTtsPlay("播放结束,请点击屏幕上的确认按钮", "0")

    }

    override fun onPause() {
        super.onPause()
        playPdfTimer?.let {
            it.cancel()
        }
    }

    /**
     * 加载显示专家介绍pdf文件
     */
    override fun showExpertsInfo(pdfFile: File) {
        mPdfFile = pdfFile
        //循环播放专家介绍文件
        playPdfTimer = Timer()
        //加载pdf文件
        expert_introduction_pdf.fromFile(pdfFile)
            .enableSwipe(false)
            .swipeHorizontal(true)
            .enableDoubletap(false)
            .defaultPage(0)
            .onLoad { }
            .onPageChange { page: Int, pageCount: Int ->
                totalPage = pageCount
            }
            .onPageScroll { page: Int, _: Float ->
                LogUtils.d(
                    TAG,
                    "finishedOne=\t\t$finishedOne\t,page=\t$page\ttotalPage-1=\t${totalPage - 1}"
                )
                if (page == totalPage - 1 || page == totalPage - 2) {
                    //pdf介绍文件播放到最后一页时,取消循环播放任务
                    playPdfTimer?.cancel()
                    LogUtils.d(
                        TAG,
                        "专家介绍pdf文件播放结束,当前页面编码page=\t$page\tfinishedOne\t$finishedOne"
                    )
                    if (!finishedOne) {
                        speechManager!!.textTtsPlay("${pdfFile.name}播放结束,请点击屏幕上的确认按钮", "0")
                        finishedOne = true
                    }
                    myDialogYesOrNo = MyDialogYesOrNo.newInstance(
                        getString(R.string.play_end_tip_title),
                        "${pdfFile.name}播放结束!",
                        getString(R.string.dialog_chose_sure),
                        getString(R.string.dialog_chose_replay)
                    )
                    myDialogYesOrNo!!.setMyCheckChangeListener(object :
                        MyDialogYesOrNo.MyCheckChangeListener {
                        override fun checkYes() {  //播放结束确认
                            myDialogYesOrNo!!.dismiss()
                            speechManager!!.cancelTtsPlay()
                            finish()
                        }

                        override fun checkNo() {  //再放一遍
                            myDialogYesOrNo!!.dismiss()
                            expert_introduction_pdf.jumpTo(0)
                            playPdfTimer!!.schedule(object : TimerTask() {
                                override fun run() {
                                    runOnUiThread { playPdfNextPage() }
                                }
                            }, 0, pdfTurnTime)
                        }
                    })
                    myDialogYesOrNo?.show(supportFragmentManager, "playPdfEndDialog")
                }
            }
            .onError { t: Throwable ->
                //pdf宣教文件加载失败
                LogUtils.e(TAG, t.message)
            }
            .onRender { _: Int, _: Float, _: Float ->
                playPdfTimer!!.schedule(object : TimerTask() {
                    override fun run() {
                        runOnUiThread { playPdfNextPage() }
                    }
                }, 0, pdfTurnTime)
            }
            .onPageError { _: Int, _: Throwable? -> }
            .onTap(OnTapListener { e: MotionEvent? -> false })
            .enableAnnotationRendering(false)
            .enableAntialiasing(true)
            .load()
    }

    /**
     * 开始下载专家介绍pdf文件
     */
    override fun startDownloadExpertPdfFile() {
        myDownloadDialog = MyDownloadDialog.newInstance()
        myDownloadDialog!!.setContent(getString(R.string.file_start_download_title))
        myDownloadDialog?.show(supportFragmentManager, "downloadPdfEndDialog")
    }

    /**
     * 专家介绍文件下载中
     */
    override fun expertPdfFileDownloading(downloadProgress: Int) {
        myDownloadDialog!!.setContent(getString(R.string.file_downloading_title))
        myDownloadDialog!!.updateProgress(downloadProgress)
        if (downloadProgress == 100) {
            myDownloadDialog?.dismiss()
        }
    }

    /**
     * 下载专家介绍pdf文件失败
     */
    override fun downloadExpertPdfFileFailure() {
        ToastUtils.showSmallToast("专家介绍pdf文件下载失败")
        finish()
    }

    /**
     * 下一页
     */
    private fun playPdfNextPage() {
        val totalPage: Int = expert_introduction_pdf.pageCount
        val curPage: Int = expert_introduction_pdf.currentPage
        var nextPage = 0
        nextPage = if (curPage < totalPage - 1) {
            curPage + 1
        } else {
            0
        }
        expert_introduction_pdf.jumpTo(nextPage, true)
    }

    override fun getContext(): Context {
        return this
    }

    override fun back() {
        finish()
    }

    override fun initPresenter(): ExpertInfoPresenterImp {
        return ExpertInfoPresenterImp()
    }
}