package com.fitgreat.doctorface_top.base

import android.content.Context

interface BaseView {
    /**
     * 获取当前页面上下文
     */
    fun getContext(): Context
}