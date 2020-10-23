package com.fitgreat.doctorface_top.network.request

import com.fitgreat.doctorface_top.network.METHOD
import com.fitgreat.doctorface_top.util.LogUtils
import okhttp3.Headers
import okhttp3.Request
import okhttp3.RequestBody
import java.net.URLEncoder
import java.util.*
import java.util.concurrent.ConcurrentHashMap

abstract class BaseRequest {

    var mHeaders: Headers? = null
    var mBody: RequestBody? = null
    var mUrl: String? = null
    var currentMethod: METHOD? = null
    var mHasHead: Boolean = false
    var builder: Request.Builder = Request.Builder()
    open var mConcurrentHashMap: ConcurrentHashMap<String, String>? = null

    constructor(
        url: String,
        concurrentHashMap: ConcurrentHashMap<String, String>,
        headers: Headers,
        method: METHOD
    ) {
        mUrl = url
        mConcurrentHashMap = concurrentHashMap
        mHeaders = headers
        currentMethod = method
        initBuilder()
    }

    constructor(
        url: String,
        headers: Headers,
        hasHead: Boolean,
        method: METHOD
    ) {
        mUrl = url
        mHeaders = headers
        currentMethod = method
        mHasHead = hasHead
        initBuilderGet()
    }

    private fun initBuilder() {
        if (currentMethod == METHOD.GET && mConcurrentHashMap?.size != 0) {
            builder.headers(mHeaders).url(attachHttpGetParams(mUrl, mConcurrentHashMap))
        } else {
            mBody = createRequestBody()
            builder.headers(mHeaders).url(mUrl).post(mBody)
        }
    }

    private fun initBuilderGet() {
        if (mHasHead) {
            builder.headers(mHeaders).url(mUrl)
        }
        builder.url(mUrl)
    }

    fun getRequest(): Request {
        return builder.build()
    }

    /**
     * 创建请求体
     */
    abstract fun createRequestBody(): RequestBody?

    /**
     * get请求拼接参数到url尾部
     *
     * @param url
     * @param params
     * @return
     */
    open fun attachHttpGetParams(url: String?, params: ConcurrentHashMap<String, String>?): String {
        val keys: Iterator<String> = params!!.keys.iterator()
        val values: Iterator<String?> = params!!.values.iterator()
        val stringBuffer = StringBuffer()
        stringBuffer.append("?")
        for (i in 0 until params.size) {
            var value: String? = null
            try {
                value = URLEncoder.encode(values.next(), "utf-8")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            stringBuffer.append(keys.next() + "=" + value)
            if (i != params.size - 1) {
                stringBuffer.append("&")
            }
            LogUtils.d("OperationUtils", "stringBuffer", stringBuffer.toString())
        }
        return url + stringBuffer.toString()
    }
}