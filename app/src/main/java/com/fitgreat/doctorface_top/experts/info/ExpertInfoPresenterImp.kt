package com.fitgreat.doctorface_top.experts.info

import com.alibaba.fastjson.JSON
import com.fitgreat.doctorface_top.base.BasePresenterImp
import com.fitgreat.doctorface_top.constants.ConstantsString.EXPERT_INTRODUCTION_FILE_PATH
import com.fitgreat.doctorface_top.model.BaseResponse
import com.fitgreat.doctorface_top.model.ExpertListData
import com.fitgreat.doctorface_top.network.AppUrlSum
import com.fitgreat.doctorface_top.network.HttpManager
import com.fitgreat.doctorface_top.network.callback.DownLoadCallback
import com.fitgreat.doctorface_top.network.callback.JsonCallback
import com.fitgreat.doctorface_top.network.callback.TypeMsgCallback
import com.fitgreat.doctorface_top.util.LogUtils
import com.fitgreat.doctorface_top.util.ToastUtils
import java.io.File
import java.util.concurrent.ConcurrentHashMap

class ExpertInfoPresenterImp : BasePresenterImp<ExpertInfoView>() {
    /**
     * 获取专家介绍文件
     */
    fun getExpertInfo(msg: ExpertListData.Msg) {
        val params = ConcurrentHashMap<String, String>()
        msg.container?.let {
            params["container"] = it
        }
        msg.synopsis?.let {
            if (!it.endsWith(".pdf")) {
                ToastUtils.showSmallToast("文件格式不对\t\t不是pdf格式文件")
                return
            }
            params["file_name"] = it
        }
        LogUtils.json(TAG, "获取专家介绍文件参数\t${JSON.toJSONString(params)}")
        val pdfFile =
            File("$EXPERT_INTRODUCTION_FILE_PATH${params["file_name"].toString()}")
        LogUtils.d(
            TAG,
            "专家介绍文件本地是否缓存\t${pdfFile.exists()}\t专家介绍pdf文件路径\t${pdfFile.absolutePath}"
        )
        if (pdfFile.exists()) { //专家介绍pdf文件已下载,加载展示
            mView?.showExpertsInfo(pdfFile)
        } else { //专家介绍pdf文件没下载,下载并加载展示
            if (params.size == 2) {
                //开始下载专家介绍pdf文件
                mView?.startDownloadExpertPdfFile()
                HttpManager.startPostJson(
                    AppUrlSum.EXPERT_INTRODUCTION_FILE,
                    params,
                    object : TypeMsgCallback() {
                        override fun parseBeanSuccess(baseResponse: BaseResponse) {
                            LogUtils.json(TAG, "获取下载专家介绍文件数据成功\t${JSON.toJSONString(baseResponse)}")
                            if (baseResponse.type == "success") {
                                HttpManager.startGetWithOutParam(
                                    baseResponse.msg!!,
                                    false,
                                    object : DownLoadCallback(
                                        params["file_name"].toString(),
                                        EXPERT_INTRODUCTION_FILE_PATH
                                    ) {
                                        override fun downLoadSuccess(file: File) {
                                            LogUtils.d(TAG, "下载专家介绍文件成功\t${file.absolutePath}")
                                            if (file.exists()) {
                                                mView?.showExpertsInfo(file)
                                            }
                                        }

                                        override fun downLoadFailure(errorString: String?) {
                                            LogUtils.e(TAG, "下载专家介绍文件失败\t$errorString")
                                            mView?.downloadExpertPdfFileFailure()
                                        }

                                        override fun downLoadProgress(progress: Int) {
                                            LogUtils.d(TAG, "当前下载专家介绍文件进度\t$progress")
                                            mView?.expertPdfFileDownloading(progress)
                                        }
                                    })
                            }
                        }

                        override fun connectFailure(errorString: String?) {
                            LogUtils.e(TAG, "获取下载专家介绍文件数据失败\t$errorString")
                        }
                    })
            } else { //专家介绍pdf文件下载失败
                mView?.downloadExpertPdfFileFailure()
            }
        }

    }

    companion object {
        private const val TAG = "ExpertInfoPresenterImp"
    }
}