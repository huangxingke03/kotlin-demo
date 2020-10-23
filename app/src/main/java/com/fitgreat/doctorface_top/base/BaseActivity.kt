package com.fitgreat.doctorface_top.base

import android.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity(), BaseView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResource())
        initData()
    }

    /**
     * 获取页面布局xml文件id
     */
    abstract fun getLayoutResource(): Int

    /**
     * 页面创建初始化
     */
    abstract fun initData()
}