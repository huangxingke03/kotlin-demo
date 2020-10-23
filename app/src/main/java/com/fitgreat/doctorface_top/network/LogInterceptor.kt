package com.fitgreat.doctorface_top.network

import com.fitgreat.doctorface_top.BuildConfig
import com.fitgreat.doctorface_top.util.LogUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * 网络请求拦截器
 *
 *
 */
class LogInterceptor : Interceptor {
    private val TAG = LogInterceptor::class.java.simpleName

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        // 拦截请求，获取到该次请求的request
        val request = chain.request()
        LogUtils.d(TAG, "本次请求url==>" + request.url().toString())
        // 执行本次网络请求操作，返回response信息
        val response = chain.proceed(request)
        if (BuildConfig.DEBUG) {
            debug(request, response)
        }
        return response.newBuilder().build()
    }

    @Throws(IOException::class)
    private fun debug(request: Request, response: Response) {
        val buffer = StringBuffer()
        buffer.append("####################--打印请求体---#############")
        for (key in request.headers().toMultimap().keys) {
            buffer.append(
                """
header: {$key : ${request.headers().toMultimap()[key]}}"""
            )
        }
        LogUtils.d(TAG, buffer.toString())
        LogUtils.d(TAG, "url: " + request.url().uri().toString())
    }
}