package com.fitgreat.doctorface_top.network

object AppUrlSum {
    /**
     * 生产环境
     */
    val RELASE_URL: String = ""

    /**
     * 测试环境
     */
    val DEBUG_URL: String = "http://iot-test.fitgreat.cn:8100/"

    /**
     * 服务器认证获取token接口 测试环境
     */
    var GET_TOKEN_TEST = "https://login-test.fitgreat.cn/"

    /**
     * 服务器认证获取token接口 生产环境
     */
    var GET_TOKEN_PRODUCT = "https://login.fitgreat.cn/"

    /**
     * 获取医生信息列表
     */
    var GET_EXPERT_LIST: String = "$DEBUG_URL/api/doctor/all"

    /**
     * 获取医生介绍文件
     */
    var EXPERT_INTRODUCTION_FILE: String = "$DEBUG_URL/api/doctor/synopsis"

    /**
     *病人签到
     */
    var PATIENT_SIGN_IN: String = "$DEBUG_URL/api/booking/sign"

    /**
     *根据手机号查询预约病人信息
     */
    var SEARCH_PATIENT_BY_PHONE: String = "$DEBUG_URL/api/booking/info/phone"

    /**
     * HEADER 认证参数
     */
    var AUTH_STR = "fU459arccaBJLfygiCGXBHrb:T5GOi1CRVi8WFsjCzxDzNQBmksEBB7uml47Q7ojtP4kCJW5Z"

    /**
     * 服务器认证获取token接口
     */
    var GET_ROBOT_TOKEN: String = "$GET_TOKEN_TEST/oauth/v2/token"

    /**
     * 获取机器人注册信息接口 测试环境
     */
    var GET_ROBOT_INFO_TEST = "https://signal-test.fitgreat.cn"

    /**
     * 获取机器人信息接口
     */
    var ROBOT_INFO: String = "$GET_ROBOT_INFO_TEST/api/robot/info"

    /**
     * 获取下一步操作
     */
    var GET_NEXT_STEP: String = "$GET_ROBOT_INFO_TEST/api/robot/instruction/nextstep"

    /**
     * 获取特定工作流
     */
    var SPECIFIC_WORK_FLOW: String = "$GET_ROBOT_INFO_TEST/api/workflow/info"
}