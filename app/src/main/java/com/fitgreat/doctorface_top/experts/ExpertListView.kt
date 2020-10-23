package com.fitgreat.doctorface_top.experts

import com.fitgreat.doctorface_top.base.BaseView
import com.fitgreat.doctorface_top.model.ExpertListData


interface ExpertListView:BaseView {
    /**
     * 展示专家列表数据
     */
    fun showExpertsSuccess(msg: MutableList<ExpertListData.Msg>)
}