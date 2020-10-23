package com.fitgreat.doctorface_top.network.request

import com.fitgreat.doctorface_top.network.METHOD
import okhttp3.Headers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.concurrent.ConcurrentHashMap

/**
 * form表单格式上传
 */
class PostMultipartRequest : BaseRequest {
    constructor(
        url: String,
        concurrentHashMap: ConcurrentHashMap<String, String>,
        headers: Headers,
        method: METHOD
    ) : super(
        url,
        concurrentHashMap, headers, method
    )

    override fun createRequestBody(): RequestBody? {
        var builder = MultipartBody.Builder()
        mConcurrentHashMap?.let {
            for (mutableEntry in it) {
                builder.addFormDataPart(mutableEntry.key, mutableEntry.value)
            }
        }
        builder.setType(MultipartBody.FORM)
        return builder.build()
    }
}