package com.fitgreat.doctorface_top.constants

import android.os.Environment

object ConstantsString {
    /**
     * 公用弹窗标题
     */
    val BASE_DIALOG_TITLE: String = "base_dialog_title"

    /**
     * 公用弹窗提示内容
     */
    val BASE_DIALOG_CONTENT: String = "base_dialog_content"

    /**
     * 公用弹窗选择按钮是
     */
    val BASE_DIALOG_YES: String = "base_dialog_yes"

    /**
     * 公用弹窗选择按钮否
     */
    val BASE_DIALOG_NO: String = "base_dialog_no"

    /**
     * sp文件名字
     */
    val ROBOT_TOKEN_KEY: String = "robot_token_key"

    /**
     * dds初始化成功
     */
    var INIT_DDS_SUCCESS = "init_dds_success"

    /**
     * dds初始化失败
     */
    var INIT_DDS_FAILURE = "init_dds_failure"

    /**
     * 机器人注册信息
     */
    var REGIST_ROBOT_INFO = "regist_robot_info"

    /**
     * 查询预约条件
     */
    var CHECK_APPOINTMENT_CONDITION = "check_appointment_condition"

    /**
     * 设置语音提示语
     */
    var VOICE_HELLO_TIP = "voice_hello_tip"

    /**
     * 播放间隔时间
     */
    var PLAY_DELAY_TIME = "play_delay_time"

    /**
     * 工程开始模式
     */
    var DEVELOPMENT_MODEL = "development_model"

    /**
     * 专家信息
     */
    var EXPERT_DATA = "expert_data"

    /**
     * 专家信息
     */
    var EXPERT_DATA_BUNDLE = "expert_data_bundle"

    /**
     * 机器人返回原点,并冲电工作流
     */
    var AUTOMATIC_WORK_FLOW = "automatic_work_flow"

    /**
     * 导航引导病人挂号工作流
     */
    var REGISTER_WORK_FLOW = "registered_work_flow"

    /**
     * 外宾预约引导工作流
     */
    var FOREIGN_RESERVATION_WORK_FLOW = "foreign_reservation_work_flow"

    /**
     * 非外宾预约引导工作流
     */
    var NORMAL_RESERVATION_WORK_FLOW = "normal_reservation_work_flow"

    /**
     * 机器人返回原点,并冲电工作流
     */
    var WORK_FLOW_TYPE1 = "charge"

    /**
     * 导航引导病人挂号工作流
     */
    var WORK_FLOW_TYPE2 = "doctorface-register1"

    /**
     * 外宾预约就诊引导工作流
     */
    var WORK_FLOW_TYPE3 = "foreign-guest"

    /**
     * 非外宾预约就诊引导工作流
     */
    var WORK_FLOW_TYPE4 = "un-foreign-guest"

    /**
     * JORS初始化成功标记
     */
    var JROS_INITIALIZATION_SUCCESS = "jros-initialization-success"

    /**
     * 专家介绍文件下载路径
     */
    val EXPERT_INTRODUCTION_FILE_PATH =
        "${Environment.getExternalStorageDirectory().absolutePath}/Download/"

    /**
     * 机器人硬件版本
     */
    val ROBOT_HARDWARE_VERSION = "robot_hardware_version"
    /**
     * 计时器间隔3秒实现
     */
    val TIME_TASK_INTERVAL_THREE = "time_task_interval_three"
}