package com.fitgreat.doctorface_top.experts

import com.alibaba.fastjson.JSON
import com.fitgreat.doctorface_top.base.BasePresenterImp
import com.fitgreat.doctorface_top.model.ExpertListData
import com.fitgreat.doctorface_top.network.AppUrlSum
import com.fitgreat.doctorface_top.network.HttpManager
import com.fitgreat.doctorface_top.network.callback.JsonCallback
import com.fitgreat.doctorface_top.util.LogUtils

class ExpertListPresenterImp : BasePresenterImp<ExpertListView>() {
    /**
     * 获取专家列表数据
     */
    fun getExpertList() {
        HttpManager.startGetWithOutParam(AppUrlSum.GET_EXPERT_LIST, true,object : JsonCallback() {
            override fun getJsonSuccess(jsonData: String?) {
                LogUtils.json(TAG, "获取${jsonData}")
                var expertListData = JSON.parseObject(jsonData, ExpertListData::class.java)
                //页面数据
                mView?.showExpertsSuccess(expertListData.msg)
                LogUtils.json(TAG, "专家一览列表数据\tmsg")
            }

            override fun connectFailure(errorString: String?) {
                LogUtils.e(TAG, "获取专家列表数据失败\t$errorString")
            }
        })
    }

    companion object {
        private const val TAG = "ExpertListPresenterImp"
    }
}