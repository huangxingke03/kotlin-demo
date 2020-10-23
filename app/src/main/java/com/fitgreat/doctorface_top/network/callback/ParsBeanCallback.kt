package com.fitgreat.doctorface_top.network.callback

import android.os.Handler
import android.os.Looper
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.fitgreat.doctorface_top.constants.ConstantsInt.REQUEST_SUCCEEDED_CODE
import com.fitgreat.doctorface_top.model.BaseResponse
import com.fitgreat.doctorface_top.util.LogUtils
import com.fitgreat.doctorface_top.util.ToastUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

/**
 * 网络请求返回对象回调 返回数据其他格式
 */
abstract class ParsBeanCallback<T>(private var myClass: Class<T>) : Callback {
    companion object {
        private const val TAG = "ParsBeanCallback"
        private val handler = Handler(Looper.getMainLooper())
    }

    override fun onResponse(call: Call?, response: Response?) {
        if (response?.code() == REQUEST_SUCCEEDED_CODE) {
            var responseString = response?.body()?.string()
            LogUtils.d(TAG, "responseString\t\t$responseString")
            val parseObject = JSON.parseObject(responseString, myClass)
            handler.post {
                parseBeanSuccess(parseObject)
            }
        } else {
            handler.post {
                ToastUtils.showSmallToast("请求获取数据失败")
            }
        }
    }

    override fun onFailure(call: Call?, e: IOException?) {
        handler.post {
            connectFailure(e?.message)
        }
    }

    /**
     * 网络请求连接成功获取java对象
     */
    abstract fun parseBeanSuccess(javaBean: T)

    /**
     * 网络请求连接连接失败
     */
    abstract fun connectFailure(errorString: String?)


}