package com.fitgreat.doctorface_top

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.AnimationDrawable
import android.view.View
import android.widget.ImageView
import com.fitgreat.doctorface_down.speech.SpeechManager
import com.fitgreat.doctorface_top.base.BaseMvpActivity
import com.fitgreat.doctorface_top.checkappointment.CheckAppointmentActivity
import com.fitgreat.doctorface_top.constants.ConstantsInt.ROBOT_CONTROL_MODE
import com.fitgreat.doctorface_top.constants.ConstantsInt.ROBOT_DRAG_MODE
import com.fitgreat.doctorface_top.constants.ConstantsString.INIT_DDS_SUCCESS
import com.fitgreat.doctorface_top.experts.ExpertListActivity
import com.fitgreat.doctorface_top.setting.SettingActivity
import com.fitgreat.doctorface_top.util.LogUtils
import com.fitgreat.doctorface_top.util.SpUtil
import com.fitgreat.ros.JRos
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.util.*

@RuntimePermissions
open class MainActivity : BaseMvpActivity<MainPresenterImp, MainView>(), View.OnClickListener,
    MainView {
    private val TAG = "MainActivity"
    private var jRos: JRos? = null
    private var timer: Timer? = null
    private var ddSInitBroadCastReceiver: DDSInitBroadCastReceiver? = null
    override fun getLayoutResource(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        //启动机器人说话动画
        wink_speak_animation.setBackgroundResource(R.drawable.wink_speak_animation)
        val animationDrawable = wink_speak_animation.drawable as AnimationDrawable
        animationDrawable.start()
        //启动波纹动画
        voice_animation.speechStarted()
        //添加点击事件
        btn_set_module.setOnClickListener(this)
        constraintLayout_sign_in.setOnClickListener(this)
        robot_control_mode_image.setOnClickListener(this)
        robot_drag_mode_image.setOnClickListener(this)
        constraintLayout_experts_glance.setOnClickListener(this)
        //机器人模式默认为控制模式
        setRobotModeUi(
            robot_control_mode_image,
            robot_drag_mode_image,
            getString(R.string.control_mode)
        )
        //获取机器人服务端token
        mPresenter?.getRobotToken(this)
        //初始化dds
        speechManager?.initialization()
        //JRos单例获取并初始化
        jRos = JRos.getInstance()
        mPresenter?.initJRos(jRos!!)
        val intentFilter = IntentFilter()
        intentFilter.addAction(INIT_DDS_SUCCESS)
        ddSInitBroadCastReceiver = DDSInitBroadCastReceiver()
        registerReceiver(ddSInitBroadCastReceiver, intentFilter)
    }

    inner class DDSInitBroadCastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == INIT_DDS_SUCCESS) {
                LogUtils.d(TAG, "dds初始化成功")
                //dds初始化成功后,基础参数设置
                //dds初始化成功后,基础参数设置
                SpeechManager.setParameter()
                speechManager!!.textTtsPlay("初始化成功", "0")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onResume() {
        super.onResume()
        this.initializationResumeWithPermissionCheck()
    }

    override fun onPause() {
        super.onPause()
        //取消timer定时任务
        timer?.let {
            it.cancel()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        jRos?.let {
            it.release()
        }
        //JRos初始化状态失败
        SpUtil.saveJRosInitializationTag(false)
        unregisterReceiver(ddSInitBroadCastReceiver)
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun initializationResume() {
        //获取手机卡信号强度
        mPresenter?.getSignalStrength(this)
        //根据机器人锁轴状态更新页面模式显示更新
        if (SpUtil.getJRosInitializationTag()) {

        }
        //启动定时任务间隔三秒更新页面显示电量
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                LogUtils.d(TAG, "timer")
                if (SpUtil.getJRosInitializationTag()) {  //JRos初始化成功,更新页面电量状态
                    runOnUiThread { robotInfoShow(jRos!!.robotState.percentage) }
                }
            }
        }, 0, 3 * 1000)
    }

    /**
     * 页面更新
     */
    override fun showPageData() {
        //显示机器人信息
        robot_name.text = SpUtil.getRobotInfo()?.f_Name
    }

    /**
     * 手机卡信号强度更新
     */
    override fun showLeaveSim(levelSim: Int) {
        signal_img.setImageLevel(levelSim)
    }

    /**
     * 电量状态显示
     */
    private fun robotInfoShow(power: Int) {
        if (jRos!!.robotState.isCharge) {  //机器人冲电时,电量状态显示冲电状态
            battery_img.setImageLevel(5)
        } else {   //机器人没冲电时,电量显示实际电量值
            when {
                power <= 20 -> {
                    battery_img.setImageLevel(0)
                }
                power <= 40 -> {
                    battery_img.setImageLevel(1)
                }
                power <= 60 -> {
                    battery_img.setImageLevel(2)
                }
                power <= 80 -> {
                    battery_img.setImageLevel(3)
                }
                power <= 100 -> {
                    battery_img.setImageLevel(4)
                }
            }
        }
        text_battery.text = "$power%"
        LogUtils.d(
            TAG,
            "机器人当前电量\t${jRos!!.robotState.percentage}\t\t当前锁轴状态\t\t${jRos!!.robotState.isLocked}\t\t当前冲电状态\t\t${jRos!!.robotState.isCharge}"
        )
        //根据机器人当前锁轴状态更新页面显示工作模式 控制模式  拖动模式
        if (jRos!!.robotState.isLocked) { //锁轴状态下为控制模式
            setRobotModeUi(
                robot_control_mode_image,
                robot_drag_mode_image,
                getString(R.string.control_mode)
            )
        } else { //非锁轴状态下为拖动模式
            setRobotModeUi(
                robot_drag_mode_image,
                robot_control_mode_image,
                getString(R.string.drag_mode)
            )
        }
    }

    override fun getContext(): Context {
        return this
    }

    override fun initPresenter(): MainPresenterImp {
        return MainPresenterImp()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_set_module -> {
                startActivity(Intent(this, SettingActivity::class.java))
            }
            R.id.constraintLayout_sign_in -> {
                startActivity(Intent(this, CheckAppointmentActivity::class.java))
            }
            R.id.constraintLayout_experts_glance -> {
                startActivity(Intent(this, ExpertListActivity::class.java))
            }
            R.id.robot_control_mode_image -> {
                setRobotModeUi(
                    robot_control_mode_image,
                    robot_drag_mode_image,
                    getString(R.string.control_mode)
                )
                setRobotModeJRos(ROBOT_CONTROL_MODE)
            }
            R.id.robot_drag_mode_image -> {
                setRobotModeUi(
                    robot_drag_mode_image,
                    robot_control_mode_image,
                    getString(R.string.drag_mode)
                )
                setRobotModeJRos(ROBOT_DRAG_MODE)
            }
        }
    }

    /**
     * 机器人模式变更页面ui更新显示
     */
    private fun setRobotModeUi(
        selectImageView: ImageView,
        unSelectImageView: ImageView,
        modeText: String
    ) {
        selectImageView.isSelected = true
        unSelectImageView.isSelected = false
        power_mode_txt.text = modeText
    }

    /**
     * 设置机器人工作模式通过JRos
     */
    private fun setRobotModeJRos(robotModeType: Int) {
        val jRosInitializationTag = SpUtil.getJRosInitializationTag()
        object : Thread() {
            override fun run() {
                super.run()
                if (jRosInitializationTag) {
                    when (robotModeType) {
                        ROBOT_CONTROL_MODE -> { //控制模式
                            jRos!!.op_setPowerlock(1.toByte())
                        }
                        ROBOT_DRAG_MODE -> {  //拖动模式
                            jRos!!.op_setPowerlock(2.toByte())
                        }
                    }
                }
            }
        }.start()
    }
}