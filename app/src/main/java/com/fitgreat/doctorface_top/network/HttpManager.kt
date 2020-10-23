package com.fitgreat.doctorface_top.network

import android.os.Build
import android.util.Base64
import com.fitgreat.doctorface_top.network.request.NoParametersGetRequest
import com.fitgreat.doctorface_top.network.request.PostJsonRequest
import com.fitgreat.doctorface_top.network.request.PostMultipartRequest
import com.fitgreat.doctorface_top.network.request.WithParametersGetRequest
import com.fitgreat.doctorface_top.util.SpUtil
import okhttp3.Callback
import okhttp3.Headers
import okhttp3.OkHttpClient
import java.nio.charset.StandardCharsets
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

object HttpManager {
    enum class MyEnum(var num: Int) {
        CONNECT_TIMEOUT(10),
        READ_TIMEOUT(30),
        WRITE_TIMEOUT(30);

        /**
         * 初始化okhttp
         */
        companion object {
            fun initClient(): OkHttpClient {
                val builder = OkHttpClient.Builder()
                builder.connectTimeout(CONNECT_TIMEOUT.num.toLong(), TimeUnit.SECONDS)
                builder.writeTimeout(WRITE_TIMEOUT.num.toLong(), TimeUnit.SECONDS)
                builder.readTimeout(READ_TIMEOUT.num.toLong(), TimeUnit.SECONDS)
                builder.addNetworkInterceptor(LogInterceptor())
                return builder.build()
            }

            /**
             * 获取机器人认证token请求Headers
             */
            fun getRobotTokenHeads(): Headers {
                val builder = Headers.Builder()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    builder.add(
                        "Authorization", "Basic " + Base64.encodeToString(
                            AppUrlSum.AUTH_STR.toByteArray(StandardCharsets.UTF_8), Base64.NO_WRAP
                        )
                    )
                }
                builder.add("Content-Type", "application/x-www-form-urlencoded")
                return builder.build()
            }

            /**
             * 公共网络请求Headers
             */
            fun getCommonHeads(): Headers {
                val builder = Headers.Builder()
                val token: String = SpUtil.getToken()
                builder.add("Authorization", "Bearer $token")
                builder.add("Content-Type", "application/x-www-form-urlencoded")
                return builder.build()
            }
        }
    }

    /**
     * post请求参数以form表单格式
     */
    fun <T : Callback> startPostFormData(
        currentUrl: String,
        mConcurrentHashMap: ConcurrentHashMap<String, String>,
        callBack: T
    ) {
        val initClient = MyEnum.initClient()
        val postMultipartRequest = PostMultipartRequest(
            currentUrl,
            mConcurrentHashMap,
            MyEnum.getCommonHeads(), METHOD.POST
        ).getRequest()
        val newCall = initClient.newCall(postMultipartRequest)
        if (!newCall.isExecuted) {
            newCall.enqueue(callBack)
        }
    }

    /**
     * get请求没有参数
     */
    fun <T : Callback> startGetWithOutParam(
        currentUrl: String,
        hasHead: Boolean,
        callBack: T
    ) {
        val initClient = MyEnum.initClient()
        val noParametersGetRequest = NoParametersGetRequest(
            currentUrl,
            MyEnum.getCommonHeads(), hasHead, METHOD.GET
        ).getRequest()
        val newCall = initClient.newCall(noParametersGetRequest)
        if (!newCall.isExecuted) {
            newCall.enqueue(callBack)
        }
    }

    /**
     * get请求有参数
     */
    fun <T : Callback> startGetWithParam(
        currentUrl: String,
        mConcurrentHashMap: ConcurrentHashMap<String, String>,
        callBack: T
    ) {
        val initClient = MyEnum.initClient()
        val withParametersGetRequest = WithParametersGetRequest(
            currentUrl,
            MyEnum.getCommonHeads(), mConcurrentHashMap, METHOD.GET
        ).getRequest()
        val newCall = initClient.newCall(withParametersGetRequest)
        if (!newCall.isExecuted) {
            newCall.enqueue(callBack)
        }
    }

    /**
     * 获取服务器端机器人token
     */
    fun <T : Callback> getRobotToken(
        currentUrl: String,
        mConcurrentHashMap: ConcurrentHashMap<String, String>,
        callBack: T
    ) {
        val initClient = MyEnum.initClient()
        val postMultipartRequest = PostMultipartRequest(
            currentUrl,
            mConcurrentHashMap,
            MyEnum.getRobotTokenHeads(), METHOD.POST
        ).getRequest()
        val newCall = initClient.newCall(postMultipartRequest)
        if (!newCall.isExecuted) {
            newCall.enqueue(callBack)
        }
    }

    /**
     * post请求json格式
     */
    fun <T : Callback> startPostJson(
        currentUrl: String,
        mConcurrentHashMap: ConcurrentHashMap<String, String>,
        callBack: T
    ) {
        val initClient = MyEnum.initClient()
        val postJsonRequest = PostJsonRequest(
            currentUrl,
            mConcurrentHashMap,
            MyEnum.getCommonHeads(), METHOD.POST
        ).getRequest()
        val newCall = initClient.newCall(postJsonRequest)
        if (!newCall.isExecuted) {
            newCall.enqueue(callBack)
        }
    }
}
