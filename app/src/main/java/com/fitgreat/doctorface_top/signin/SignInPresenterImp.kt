package com.fitgreat.doctorface_top.signin

import com.alibaba.fastjson.JSON
import com.fitgreat.doctorface_top.base.BasePresenterImp
import com.fitgreat.doctorface_top.model.Appointment
import com.fitgreat.doctorface_top.model.BaseResponse
import com.fitgreat.doctorface_top.model.CheckResult
import com.fitgreat.doctorface_top.network.AppUrlSum.PATIENT_SIGN_IN
import com.fitgreat.doctorface_top.network.AppUrlSum.SEARCH_PATIENT_BY_PHONE
import com.fitgreat.doctorface_top.network.HttpManager
import com.fitgreat.doctorface_top.network.callback.JsonCallback
import com.fitgreat.doctorface_top.network.callback.TypeMsgCallback
import com.fitgreat.doctorface_top.util.LogUtils
import com.fitgreat.doctorface_top.util.ToastUtils
import java.util.concurrent.ConcurrentHashMap

/**
 * 预约类型   全科检查/外宾专家  一个挂号点      外宾预约 一个挂号点
 */
class SignInPresenterImp : BasePresenterImp<SignInView>() {
    private val TAG = "SignInPresenterImp"

    /**
     * 查询病人预约信息
     */
    fun checkAppointment(checkCondition: String) {
        val params = ConcurrentHashMap<String, String>()
//        params["phone"] = checkCondition
        params["phone"] = "13526749710"
        HttpManager.startGetWithParam(
            SEARCH_PATIENT_BY_PHONE,
            params,
            object : TypeMsgCallback() {
                override fun parseBeanSuccess(baseResponse: BaseResponse) {
                    LogUtils.json(TAG, "查询病人预约信息成功\t\t${JSON.toJSONString(baseResponse)}")
                    if (baseResponse.type == "success") {
                        val appointment =
                            JSON.parseObject(baseResponse.msg, Appointment::class.java)
                        LogUtils.json(TAG, "查询获取病人预约信息\t\t${JSON.toJSONString(appointment)}")
                        mView?.showAppointment(appointment)
                    } else {
                        mView?.showAppointment(ArrayList<Appointment.AppointmentItem>())
                    }
                }

                override fun connectFailure(errorString: String?) {
                    LogUtils.e(TAG, "查询病人预约信息失败\t\t$errorString")
                }
            })
    }

    /**
     * 病人签到
     */
    fun signIn(id: String) {
        val params = ConcurrentHashMap<String, String>()
        params["id"] = id
        params["check_source"] = 2.toString()
        LogUtils.json(TAG, "病人开始签到\t\t${JSON.toJSONString(params)}")
        HttpManager.startPostJson(PATIENT_SIGN_IN, params, object : JsonCallback() {
            override fun getJsonSuccess(jsonData: String?) {
                LogUtils.d(TAG, "病人签到结果\t\t$jsonData")
                val checkResult = JSON.parseObject(jsonData, CheckResult::class.java)
                if (checkResult.type == "success") { //签到成功
                    mView?.signInSuccess()
                } else {
                    ToastUtils.showSmallToast("签到失败\t\t${checkResult.msg}")
                }
            }

            override fun connectFailure(errorString: String?) {
                LogUtils.e(TAG, "病人签到失败\t\t$errorString")
                ToastUtils.showSmallToast("签到失败\t\t$errorString")
            }
        })
    }
}