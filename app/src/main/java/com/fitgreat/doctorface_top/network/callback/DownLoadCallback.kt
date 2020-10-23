package com.fitgreat.doctorface_top.network.callback

import android.os.Handler
import android.os.Looper
import com.fitgreat.doctorface_top.constants.ConstantsInt.REQUEST_SUCCEEDED_CODE
import com.fitgreat.doctorface_top.util.LogUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * 文件下载返回callback
 */
abstract class DownLoadCallback(fileName: String, filePath: String) : Callback {
    companion object {
        private const val TAG = "DownLoadCallback"
        private val handler = Handler(Looper.getMainLooper())
    }

    private var mFileName: String? = null
    private var mFilePath: String? = null

    init {
        mFileName = fileName
        mFilePath = filePath
    }

    override fun onResponse(call: Call?, response: Response?) {
        if (response?.code() == REQUEST_SUCCEEDED_CODE) {
            var fileInputStream: InputStream? = null
            var fileOutputStream: FileOutputStream? = null
            try {//下载文件数据总长度
                val contentLength = response.body().contentLength()
                LogUtils.d(TAG, "文件总长度\t$contentLength")
                fileInputStream = response.body().byteStream()
                val parentFile = File("$mFilePath")
                if (!parentFile.exists()) { //专家介绍文件父目录不存在创建父目录
                    parentFile.mkdirs()
                }
                val childFile = File(parentFile, mFileName)
                if (!childFile.exists()) {
                    childFile.createNewFile()
                }
                fileOutputStream = FileOutputStream(childFile)
                LogUtils.d(TAG, "下载文件保存路径\t${childFile.absolutePath}")
                //页面更新下载进度显示
                var showLength: Long = 0
                //每次读取下载文件量
                var eachDownloadLength: Int = 0
                val buf = ByteArray(2048)
                //下载到本地,开始写数据标记  (fileInputStream.read(buf)) != -1
                var writeFileTag: Boolean = true
                while (writeFileTag) {
                    eachDownloadLength = fileInputStream.read(buf)
                    if (eachDownloadLength != -1) {
                        fileOutputStream.write(buf, 0, eachDownloadLength)
                        showLength += eachDownloadLength
                        val showProgress = (showLength * 1.0f / contentLength * 100).toInt()
                        LogUtils.d(
                            TAG,
                            "showProgress\t$showProgress\t\teachDownloadLength\t$eachDownloadLength\t\tshowLength\t$showLength"
                        )
                        //下载进度更新
                        handler.postDelayed({downLoadProgress(showProgress)},3*1000)
                    } else {
                        writeFileTag = false
                    }
                }
                fileOutputStream.flush()
                //下载成功
                handler.post { downLoadSuccess(childFile) }
            } catch (e: Exception) {
                //下载失败
                handler.post { downLoadFailure(e?.message) }
            } finally {
                fileInputStream?.let {
                    it.close()
                }
                fileOutputStream?.let {
                    it.close()
                }
            }
        }
    }

    override fun onFailure(call: Call?, e: IOException?) {
        LogUtils.e(TAG, "文件下载失败\t${e?.message}")
        //下载失败
        handler.post { downLoadFailure(e?.message) }
    }

    /**
     * 文件下载成功
     */
    abstract fun downLoadSuccess(file: File)

    /**
     * 文件下载进度失败
     */
    abstract fun downLoadFailure(errorString: String?)

    /**
     * 文件下载进度
     */
    abstract fun downLoadProgress(progress: Int)

}