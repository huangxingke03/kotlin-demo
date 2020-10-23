package com.fitgreat.doctorface_top

import android.app.Application
import android.content.Context
import com.fitgreat.doctorface_top.util.LogUtils

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        LogUtils.init(true)
        mContext = this
    }

    companion object {
        private var mContext: Context? = null

        fun getContext(): Context? {
            return mContext
        }
    }
}