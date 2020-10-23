package com.fitgreat.doctorface_top

import android.content.Context
import android.telephony.PhoneStateListener
import android.telephony.SignalStrength
import android.telephony.TelephonyManager
import com.alibaba.fastjson.JSON
import com.fitgreat.doctorface_top.base.BasePresenterImp
import com.fitgreat.doctorface_top.constants.ConstantsString.WORK_FLOW_TYPE1
import com.fitgreat.doctorface_top.constants.ConstantsString.WORK_FLOW_TYPE2
import com.fitgreat.doctorface_top.constants.ConstantsString.WORK_FLOW_TYPE3
import com.fitgreat.doctorface_top.constants.ConstantsString.WORK_FLOW_TYPE4
import com.fitgreat.doctorface_top.model.AuthData
import com.fitgreat.doctorface_top.model.BaseResponse
import com.fitgreat.doctorface_top.model.RobotInfoData
import com.fitgreat.doctorface_top.network.AppUrlSum
import com.fitgreat.doctorface_top.network.AppUrlSum.SPECIFIC_WORK_FLOW
import com.fitgreat.doctorface_top.network.HttpManager
import com.fitgreat.doctorface_top.network.callback.ParsBeanCallback
import com.fitgreat.doctorface_top.network.callback.TypeMsgCallback
import com.fitgreat.doctorface_top.util.LogUtils
import com.fitgreat.doctorface_top.util.PhoneInfoUtils
import com.fitgreat.doctorface_top.util.SpUtil
import com.fitgreat.ros.JRos
import com.fitgreat.ros.JRosConfig
import com.fitgreat.ros.JRosInitListener
import com.fitgreat.ros.MoveStateObserver
import java.util.concurrent.ConcurrentHashMap

class MainPresenterImp : BasePresenterImp<MainView>() {
    private val TAG = "MainPresenterImp"

    //机器人信息更新标签
    private var robotInfoChangeTag: Boolean = false

    /**
     * 获取机器人token信息
     */
    fun getRobotToken(context: Context) {
        val params = ConcurrentHashMap<String, String>()
        params["grant_type"] = "client_credentials"
        params["scope"] = "basic"
        HttpManager.getRobotToken(
            AppUrlSum.GET_ROBOT_TOKEN,
            params,
            object : ParsBeanCallback<AuthData>(AuthData::class.java) {
                override fun parseBeanSuccess(javaBean: AuthData) {
                    LogUtils.json(TAG, "获取机器人token成功,\t\t${JSON.toJSONString(javaBean)}")
                    //保存机器人token到本地
                    SpUtil.saveToken(javaBean.access_token)
                    //获取机器人注册信息
                    getRobotInfo(context)
                    //自动回充工作流
                    getWorkFlow(WORK_FLOW_TYPE1)
                    //导航引导病人挂号工作流
                    getWorkFlow(WORK_FLOW_TYPE2)
                    //外宾预约就诊引导工作流
                    getWorkFlow(WORK_FLOW_TYPE3)
                    //非外宾预约就诊引导工作流
                    getWorkFlow(WORK_FLOW_TYPE4)
                }

                override fun connectFailure(errorString: String?) {
                    LogUtils.d(TAG, "获取token失败,\t\t$errorString")
                }
            })
    }

    /**
     * 获取特定工作流
     */
    private fun getWorkFlow(workFlowType: String) {
        val robotInfo = SpUtil.getRobotInfo()
        val params = ConcurrentHashMap<String, String>()
        params["departmentId"] = "${robotInfo?.f_DepartmentId}"
        params["mapId"] = "${robotInfo?.F_MapId}"
        params["type"] = "$workFlowType"
        HttpManager.startGetWithParam(SPECIFIC_WORK_FLOW, params, object : TypeMsgCallback() {
            override fun parseBeanSuccess(baseResponse: BaseResponse) {
                LogUtils.d(
                    TAG,
                    "工作流种类\t$workFlowType\t\t工作流数据信息获取成功\t\t${baseResponse.msg}"
                )
                if (baseResponse.type == "success") {
                    //原点回充,引导挂号导航,外国人就诊导航,非外国人就诊导航 所有工作流信息保存
                    when (workFlowType) {
                        WORK_FLOW_TYPE1 -> SpUtil.saveAutomaticWorkFlow(baseResponse.msg)
                        WORK_FLOW_TYPE2 -> SpUtil.saveRegisteredWorkFlow(baseResponse.msg)
                        WORK_FLOW_TYPE3 -> SpUtil.saveForeignReservationWorkFlow(baseResponse.msg)
                        WORK_FLOW_TYPE4 -> SpUtil.saveNormalReservationWorkFlow(baseResponse.msg)
                    }
                }
            }

            override fun connectFailure(errorString: String?) {
                LogUtils.e(
                    TAG,
                    "工工作流数据信息获取失败$errorString"
                )
            }
        })
    }

    /**
     *获取手机卡信号强度
     */
    fun getSignalStrength(context: Context) {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        tm.listen(object : PhoneStateListener() {
            override fun onSignalStrengthsChanged(signalStrength: SignalStrength?) {
                super.onSignalStrengthsChanged(signalStrength)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    val level = signalStrength!!.level
                    LogUtils.json(TAG, "手机卡信号强度,\t\t$level")
                    //页面更新显示4g卡信号强度
                    mView?.showLeaveSim(level)
                }
            }
        }, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)
    }

    /**
     * 获取机器人注册信息
     */
    private fun getRobotInfo(context: Context) {
        val params = ConcurrentHashMap<String, String>()
        params["robotId"] = PhoneInfoUtils.getSn(context)
        params["hardwareId"] = PhoneInfoUtils.getSn(context)
        HttpManager.startPostJson(
            AppUrlSum.ROBOT_INFO,
            params,
            object : TypeMsgCallback() {
                override fun parseBeanSuccess(baseResponse: BaseResponse) {
                    LogUtils.json(TAG, "获取机器人注册信息成功\t${JSON.toJSONString(baseResponse)}")
                    if (baseResponse.type == "success") {
                        val robotInfoData =
                            JSON.parseObject(baseResponse.msg, RobotInfoData::class.java)
                        LogUtils.json(TAG, "机器人注册信息\t${JSON.toJSONString(robotInfoData)}")
                        //保存机器人注册信息到本地
                        SpUtil.saveRobotInfo(robotInfoData)
                        mView?.showPageData()
                    }
                }

                override fun connectFailure(errorString: String?) {
                    LogUtils.e(TAG, "获取机器人注册信息失败\t$errorString")
                }
            })
    }

    /**
     * 初始化JRos
     * batteryEvent.setBattery(jRos.robotState.percentage)
     * batteryEvent.setPowerTechnology(jRos.robotState.isUrgent)
     * batteryEvent.setPowerHealth(jRos.robotState.isLocked)
     * batteryEvent.setConnect(jRos.IsConnected())
     * batteryEvent.setPowerStatus(jRos.robotState.isCharge)
     * positionEvent.setPosition_X(jRos.op_GetPose().x)
     * positionEvent.setPosition_Y(jRos.op_GetPose().y)
     * positionEvent.setPosition_Z(jRos.op_GetPose().yaw)
     * jRos.op_GetHardwareVersion()
     */
    fun initJRos(jRos: JRos) {
        //重新初始化JRos
        SpUtil.saveJRosInitializationTag(false)
        object : Thread() {
            override fun run() {
                super.run()
                jRos.init(
                    JRosConfig().addConfig("SOCKET_URI", "ws://192.168.88.8:9090"),
                    object : JRosInitListener {
                        override fun onInitComplete() {
                            LogUtils.d(TAG, "jRos onInitComplete! ")
                            //更新页面机器人信息
                            robotInfoChangeTag = true
                            jRos.subscribe(
                                arrayOf(
                                    "navigation_started",
                                    "navigation_stopped",
                                    "navigation_arrived",
                                    "parking_success",
                                    "parking_timeout"
                                ), moveStateObserver
                            )
                            //保存机器人硬件编号
                            SpUtil.saveHardwareVersion(jRos.op_GetHardwareVersion())
                            LogUtils.d(TAG, "机器人硬件编号\t${jRos.op_GetHardwareVersion()}")
                            //JRos初始化成功
                            SpUtil.saveJRosInitializationTag(true)
                            //机器人工作模式默认为控制模式
                            jRos.op_setPowerlock(1.toByte())
                        }

                        override fun onError(msg: String) {
                            LogUtils.d(TAG, "main onError msg:$msg")
                            //暂停更新页面机器人信息
                            robotInfoChangeTag = false
                            //JRos初始化状态失败
                            SpUtil.saveJRosInitializationTag(false)
                        }

                        override fun onDisconnect(s: String) {
                            LogUtils.d(TAG, "main onDisconnect s:$s")
                            //暂停更新页面机器人信息
                            robotInfoChangeTag = false
                            //JRos初始化状态失败
                            SpUtil.saveJRosInitializationTag(false)
                        }
                    })
            }
        }.start()
    }

    var moveStateObserver = MoveStateObserver { s ->
        LogUtils.d(TAG, "s ==== $s")
        when (s) {
            "navigation_started" -> {
            }
            "navigation_cancelled" -> LogUtils.d(TAG, "任务被终止，导航停止！")
            "navigation_stopped" -> {
//                LogUtils.d("task_robot_status", "isTerminal = $isTerminal 导航失败到不了 ")
//                if (!isTerminal) {
//                    //                        aiuiManager.onPlayLineTTS("抱歉，我无法到达" + instructionName + "！");
//                    speechManager.textTtsPlay("抱歉，我无法到达$instructionName！", "0")
//                    //更新提示信息到首页对话记录
//                    EventBus.getDefault().post(NavigationTip("抱歉，我无法到达$instructionName！"))
//                }
//                saveRobotstatus(1)
//                instruction_status = "-1"
//                BusinessRequest.UpdateInstructionStatue(
//                    instructionId,
//                    instruction_status,
//                    updateInstructionCallback
//                )
            }
            "navigation_arrived" -> {
//                //                    aiuiManager.onPlayLineTTS("我已到达" + instructionName);
//                speechManager.textTtsPlay("我已到达$instructionName", "0")
//                //更新提示信息到首页对话记录
//                EventBus.getDefault().post(NavigationTip("我已到达$instructionName"))
//                instruction_status = "2"
//                LogUtils.d(
//                    "navigation_arrived",
//                    "instructionId = $instructionId 导航成功到达  $instructionName"
//                )
//                BusinessRequest.UpdateInstructionStatue(
//                    instructionId,
//                    instruction_status,
//                    updateInstructionCallback
//                )
            }
            "parking_success" -> {
//                //                    aiuiManager.onPlayLineTTS("充电成功!");
//                speechManager.textTtsPlay("充电成功!", "0")
//                //更新提示信息到首页对话记录
//                EventBus.getDefault().post(NavigationTip("充电成功!"))
//                saveRobotstatus(5)
//                LogUtils.d(
//                    "task_robot_status",
//                    "冲电成功:为冲电状态" + "robot status = " + RobotInfoUtils.getRobotRunningStatus()
//                )
//                BusinessRequest.updateRobotState(
//                    getBattery(),
//                    RobotInfoUtils.getRobotRunningStatus(),
//                    object : HttpCallback() {
//                        fun onResult(baseResponse: BaseResponse?) {
//                            LogUtils.json(
//                                "parking_success",
//                                "更新机器人信息成功: " + JSON.toJSONString(baseResponse)
//                            )
//                        }
//                    })
//                instruction_status = "2"
//                BusinessRequest.UpdateInstructionStatue(
//                    instructionId,
//                    instruction_status,
//                    updateInstructionCallback
//                )
            }
            "parking_timeout" -> {
//                LogUtils.d(TAG, "parking_success !!!!!!!!!!!!!!!!!!")
//                //                    aiuiManager.onPlayLineTTS("充电失败，未扫描到充电桩!");
//                speechManager.textTtsPlay("充电失败，未扫描到充电桩!", "0")
//                //更新提示信息到首页对话记录
//                EventBus.getDefault().post(NavigationTip("充电失败，未扫描到充电桩!"))
//                instruction_status = "-1"
//                BusinessRequest.UpdateInstructionStatue(
//                    instructionId,
//                    instruction_status,
//                    updateInstructionCallback
//                )
            }
        }
    }
}