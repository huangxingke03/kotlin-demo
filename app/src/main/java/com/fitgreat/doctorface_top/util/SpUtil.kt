package com.fitgreat.doctorface_top.util

import android.content.Context
import android.content.SharedPreferences
import com.alibaba.fastjson.JSON
import com.fitgreat.doctorface_top.MyApp
import com.fitgreat.doctorface_top.constants.ConstantsString.AUTOMATIC_WORK_FLOW
import com.fitgreat.doctorface_top.constants.ConstantsString.DEVELOPMENT_MODEL
import com.fitgreat.doctorface_top.constants.ConstantsString.FOREIGN_RESERVATION_WORK_FLOW
import com.fitgreat.doctorface_top.constants.ConstantsString.JROS_INITIALIZATION_SUCCESS
import com.fitgreat.doctorface_top.constants.ConstantsString.NORMAL_RESERVATION_WORK_FLOW
import com.fitgreat.doctorface_top.constants.ConstantsString.PLAY_DELAY_TIME
import com.fitgreat.doctorface_top.constants.ConstantsString.REGISTER_WORK_FLOW
import com.fitgreat.doctorface_top.constants.ConstantsString.REGIST_ROBOT_INFO
import com.fitgreat.doctorface_top.constants.ConstantsString.ROBOT_HARDWARE_VERSION
import com.fitgreat.doctorface_top.constants.ConstantsString.ROBOT_TOKEN_KEY
import com.fitgreat.doctorface_top.constants.ConstantsString.VOICE_HELLO_TIP
import com.fitgreat.doctorface_top.model.RobotInfoData
import com.fitgreat.doctorface_top.model.WorkFlow

object SpUtil {
    enum class SPENUM(var value: String) {
        SP_FILE_NAME("doctor_face_sp");

        companion object {
            /**
             * 获取SharedPreferences
             */
            fun getSpFile(context: Context?): SharedPreferences {
                return context?.getSharedPreferences(SP_FILE_NAME.value, Context.MODE_PRIVATE)!!
            }

            /**
             * 获取Editor
             */
            fun getEditor(context: Context?): SharedPreferences.Editor {
                return getSpFile(context).edit()
            }
        }
    }

    /**
     * 保存机器人token到本地
     */
    fun saveToken(tokenValue: String?) {
        val editor = SPENUM.getEditor(MyApp.getContext())
        editor.putString(ROBOT_TOKEN_KEY, tokenValue)
        editor.apply()
    }

    /**
     * 本地获取机器人token
     */
    fun getToken(): String {
        val initSpFile = SPENUM.getSpFile(MyApp.getContext())
        return initSpFile.getString(ROBOT_TOKEN_KEY, null)
    }

    /**
     * 保存机器人注册信息到本地
     */
    fun saveRobotInfo(robotInfoData: RobotInfoData?) {
        val editor = SPENUM.getEditor(MyApp.getContext())
        editor.putString(REGIST_ROBOT_INFO, JSON.toJSONString(robotInfoData))
        editor.apply()
    }

    /**
     * 本地获取机器人注册信息
     */
    fun getRobotInfo(): RobotInfoData? {
        val initSpFile = SPENUM.getSpFile(MyApp.getContext())
        val stringValue = initSpFile.getString(REGIST_ROBOT_INFO, null)
        return JSON.parseObject(stringValue, RobotInfoData::class.java)
    }

    /**
     * 保存机器人提示语到本地
     */
    fun saveVoiceTip(voiceTip: String?) {
        val editor = SPENUM.getEditor(MyApp.getContext())
        editor.putString(VOICE_HELLO_TIP, voiceTip)
        editor.apply()
    }

    /**
     * 本地获取机器人提示语
     */
    fun getVoiceTip(): String {
        val initSpFile = SPENUM.getSpFile(MyApp.getContext())
        return initSpFile.getString(ROBOT_TOKEN_KEY, "你好")
    }


    /**
     * 保存机器人提示语播放间隔时间到本地
     */
    fun saveDelayTime(delayTimeValue: String?) {
        val editor = SPENUM.getEditor(MyApp.getContext())
        editor.putString(PLAY_DELAY_TIME, delayTimeValue)
        editor.apply()
    }

    /**
     * 本地获取机器人提示语播放间隔时间
     */
    fun getDelayTime(): String {
        val initSpFile = SPENUM.getSpFile(MyApp.getContext())
        return initSpFile.getString(PLAY_DELAY_TIME, "10")
    }

    /**
     * 保存当前开发模式到本地
     */
    fun saveDevelopmentModel(developmentModel: String?) {
        val editor = SPENUM.getEditor(MyApp.getContext())
        editor.putString(DEVELOPMENT_MODEL, developmentModel)
        editor.apply()
    }

    /**
     * 本地获取机器人提示语播放间隔时间
     */
    fun getDevelopmentModel(): String {
        val initSpFile = SPENUM.getSpFile(MyApp.getContext())
        return initSpFile.getString(DEVELOPMENT_MODEL, "debug")
    }

    /**
     * 保存原点回充工作流到本地
     */
    fun saveAutomaticWorkFlow(automaticWorkFlow: String?) {
        val editor = SPENUM.getEditor(MyApp.getContext())
        editor.putString(AUTOMATIC_WORK_FLOW, automaticWorkFlow)
        editor.apply()
    }

    /**
     * 本地获取原点回充工作流
     */
    fun getAutomaticWorkFlow(): WorkFlow {
        val initSpFile = SPENUM.getSpFile(MyApp.getContext())
        return JSON.parseObject(initSpFile.getString(AUTOMATIC_WORK_FLOW, ""), WorkFlow::class.java)
    }

    /**
     * 保存导航引导病人挂号工作流到本地
     */
    fun saveRegisteredWorkFlow(registeredWorkFlow: String?) {
        val editor = SPENUM.getEditor(MyApp.getContext())
        editor.putString(REGISTER_WORK_FLOW, registeredWorkFlow)
        editor.apply()
    }

    /**
     * 本地获取导航引导病人挂号工作流
     */
    fun getRegisteredWorkFlow(): WorkFlow {
        val initSpFile = SPENUM.getSpFile(MyApp.getContext())
        return JSON.parseObject(initSpFile.getString(REGISTER_WORK_FLOW, ""), WorkFlow::class.java)
    }

    /**
     * 保存外宾预约引导工作流到本地
     */
    fun saveForeignReservationWorkFlow(foreignReservationWorkFlow: String?) {
        val editor = SPENUM.getEditor(MyApp.getContext())
        editor.putString(FOREIGN_RESERVATION_WORK_FLOW, foreignReservationWorkFlow)
        editor.apply()
    }

    /**
     * 本地获取外宾预约引导工作流
     */
    fun getForeignReservationWorkFlow(): WorkFlow {
        val initSpFile = SPENUM.getSpFile(MyApp.getContext())
        return JSON.parseObject(
            initSpFile.getString(FOREIGN_RESERVATION_WORK_FLOW, ""),
            WorkFlow::class.java
        )
    }


    /**
     * 保存非外宾预约引导工作流到本地
     */
    fun saveNormalReservationWorkFlow(normalReservationWorkFlow: String?) {
        val editor = SPENUM.getEditor(MyApp.getContext())
        editor.putString(NORMAL_RESERVATION_WORK_FLOW, normalReservationWorkFlow)
        editor.apply()
    }

    /**
     * 本地获取非外宾预约引导工作流
     */
    fun getNormalReservationWorkFlow(): WorkFlow {
        val initSpFile = SPENUM.getSpFile(MyApp.getContext())
        return JSON.parseObject(
            initSpFile.getString(NORMAL_RESERVATION_WORK_FLOW, ""),
            WorkFlow::class.java
        )
    }

    /**
     * 保存JRos初始化状态到本地
     */
    fun saveJRosInitializationTag(initializationTag: Boolean) {
        val editor = SPENUM.getEditor(MyApp.getContext())
        editor.putBoolean(JROS_INITIALIZATION_SUCCESS, initializationTag)
        editor.apply()
    }

    /**
     * 本地获取JRos初始化状态
     */
    fun getJRosInitializationTag(): Boolean {
        val initSpFile = SPENUM.getSpFile(MyApp.getContext())
        return initSpFile.getBoolean(JROS_INITIALIZATION_SUCCESS, false)
    }

    /**
     * 获取硬件版本号
     */
    fun getHardwareVersion(): String? {
        val initSpFile = SPENUM.getSpFile(MyApp.getContext())
        return initSpFile.getString(ROBOT_HARDWARE_VERSION, "1.0.0")
    }

    /**
     * 保存硬件版本号
     */
    fun saveHardwareVersion(version: String) {
        val editor = SPENUM.getEditor(MyApp.getContext())
        editor.putString(ROBOT_HARDWARE_VERSION, version)
        editor.apply()
    }
}