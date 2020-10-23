package com.fitgreat.doctorface_top.model

/**
 * 获取token信息
 */
class AuthData(
    var access_token: String?,
    var expires_in: Int,
    var scope: String?,
    val token_type: String?
) {
}

