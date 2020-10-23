package com.fitgreat.doctorface_top

import com.fitgreat.doctorface_top.base.BaseView

interface MainView : BaseView {
    /**
     * 页面信息更新
     */
    fun showPageData()
    /**
     * 页面4g信号强度更新
     */
    fun showLeaveSim(levelSim:Int)
}