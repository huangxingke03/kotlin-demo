package com.fitgreat.doctorface_top.network.request

import com.fitgreat.doctorface_top.network.METHOD
import okhttp3.Headers
import okhttp3.RequestBody
import java.util.concurrent.ConcurrentHashMap

/**
 * get请求有参数
 */
class WithParametersGetRequest : BaseRequest {
    constructor(
        url: String,
        headers: Headers,
        concurrentHashMap: ConcurrentHashMap<String, String>,
        method: METHOD
    ) : super(
        url,
        concurrentHashMap,
        headers,
        method
    )

    override fun createRequestBody(): RequestBody? {
        TODO("Not yet implemented")
    }


}