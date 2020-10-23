package com.fitgreat.doctorface_top.network.request

import com.fitgreat.doctorface_top.network.METHOD
import okhttp3.Headers
import okhttp3.RequestBody

/**
 * get请求没有参数
 */
class NoParametersGetRequest : BaseRequest {
    constructor(
        url: String,
        headers: Headers,
        hasHead:Boolean,
        method: METHOD
    ) : super(
        url,
        headers,
        hasHead,
        method
    )

    override fun createRequestBody(): RequestBody? {
        TODO("Not yet implemented")
    }
}