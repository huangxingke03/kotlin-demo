package com.fitgreat.doctorface_top.experts.info

import com.fitgreat.doctorface_top.base.BaseView
import com.fitgreat.doctorface_top.model.ExpertListData
import java.io.File


interface ExpertInfoView : BaseView {
    /**
     * 专家介绍pdf展示
     */
    fun showExpertsInfo(pdfFile: File)

    /**
     * 开始下载专家介绍pdf文件
     */
    fun startDownloadExpertPdfFile()

    /**
     * 专家介绍pdf文件下载中
     */
    fun expertPdfFileDownloading(downloadProgress: Int)
    /**
     * 下载专家介绍pdf文件失败
     */
    fun downloadExpertPdfFileFailure()
}