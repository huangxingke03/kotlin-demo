package com.fitgreat.doctorface_top.network.request

import com.alibaba.fastjson.JSON
import com.fitgreat.doctorface_top.network.METHOD
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.concurrent.ConcurrentHashMap

/**
 * json形式上传
 */
class PostJsonRequest : BaseRequest {
    constructor(
        url: String,
        concurrentHashMap: ConcurrentHashMap<String, String>,
        headers: Headers,
        method: METHOD
    ) : super(
        url,
        concurrentHashMap, headers,method
    )

    override fun createRequestBody(): RequestBody? {
        return RequestBody.create(
            MediaType.parse("application/json;charset=utf-8"),
            JSON.toJSONString(mConcurrentHashMap)
        )
    }
}