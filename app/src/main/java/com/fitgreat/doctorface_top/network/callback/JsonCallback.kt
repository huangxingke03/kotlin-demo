package com.fitgreat.doctorface_top.network.callback

import android.os.Handler
import android.os.Looper
import com.fitgreat.doctorface_top.constants.ConstantsInt.REQUEST_SUCCEEDED_CODE
import com.fitgreat.doctorface_top.util.LogUtils
import com.fitgreat.doctorface_top.util.ToastUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * 网络请求返回字符串回调
 */
abstract class JsonCallback : Callback {
    companion object {
        private const val TAG = "ParsBeanCallback"
        private val handler = Handler(Looper.getMainLooper())
    }

    override fun onResponse(call: Call?, response: Response?) {
        if (response?.code()==REQUEST_SUCCEEDED_CODE){
            var responseString = response?.body()?.string()
            LogUtils.d(TAG, "获取数据成功得到json\t$responseString")
            handler.post {
                getJsonSuccess(responseString)
            }
        }else{
            handler.post {
               ToastUtils.showSmallToast("请求获取数据失败")
            }
        }
    }

    override fun onFailure(call: Call?, e: IOException?) {
        LogUtils.e(TAG, "获取数据失败\t${e?.message}")
        handler.post {
            connectFailure(e?.message)
        }
    }

    /**
     * 网络请求连接成功获取json
     */
    abstract fun getJsonSuccess(jsonData: String?)

    /**
     * 网络请求连接连接失败
     */
    abstract fun connectFailure(errorString: String?)


}