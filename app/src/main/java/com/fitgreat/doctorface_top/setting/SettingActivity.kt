package com.fitgreat.doctorface_top.setting

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.fitgreat.doctorface_top.R
import com.fitgreat.doctorface_top.base.BaseActivity
import com.fitgreat.doctorface_top.model.RobotInfoData
import com.fitgreat.doctorface_top.util.*
import com.fitgreat.doctorface_top.util.RouteUtils.goToActivity
import com.fitgreat.doctorface_top.view.BaseTitleView
import com.fitgreat.doctorface_top.view.MyDialog
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.include_settings_about_us.*
import kotlinx.android.synthetic.main.settings_hello_string.*

/**
 * 设置activity
 */
class SettingActivity : BaseActivity(), View.OnClickListener, BaseTitleView.BaseBackListener {
    private val TAG = "SettingActivity"
    var inputMethodManager: InputMethodManager? = null
    var myDialog: MyDialog? = null
    private var STRING_HELLO: String? = null

    override fun getLayoutResource(): Int {
        return R.layout.activity_settings
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initData() {
        val robotInfoData: RobotInfoData? = SpUtil.getRobotInfo()
        if (robotInfoData != null) {
            settings_robot_name.text = robotInfoData.f_Name
            settings_robot_department.text = robotInfoData.f_Account
        }
        settings_robot_serial_number.text = PhoneInfoUtils.getSn(this)
        settings_robot_app_version.text = "V " + VersionUtils.getVersionName(this)
        settings_robot_os_version.text = "Android " + PhoneInfoUtils.getSystemVersion()
//        settings_robot_hardware_version.text =
//            "V " + RobotInfoUtils.getHardwareVersion().replace("v", "").replace("V", "")
        if (TextUtils.equals(SpUtil.getDevelopmentModel(), "debug")) {
            val selected = getDrawable(R.mipmap.btn_selected)
            selected.setBounds(0, 0, selected.minimumWidth, selected.minimumHeight)
            val select = getDrawable(R.mipmap.btn_select)
            select.setBounds(0, 0, selected.minimumWidth, selected.minimumHeight)
            test_domains_radio.setCompoundDrawables(selected, null, null, null)
            product_domains_radio.setCompoundDrawables(select, null, null, null)
            settings_develop_layout.visibility = View.VISIBLE
        } else {
            val selected = getDrawable(R.mipmap.btn_selected)
            selected.setBounds(0, 0, selected.minimumWidth, selected.minimumHeight)
            val select = getDrawable(R.mipmap.btn_select)
            select.setBounds(0, 0, selected.minimumWidth, selected.minimumHeight)
            test_domains_radio.setCompoundDrawables(select, null, null, null)
            product_domains_radio.setCompoundDrawables(selected, null, null, null)
        }
        STRING_HELLO = SpUtil.getVoiceTip()
        et_hello.setText(STRING_HELLO)
        if (SpUtil.getDelayTime().toInt() !== 10) {
            de_time.setText(SpUtil.getDelayTime())
        } else {
            de_time.setText("10")
        }
        settings_reboot_layout.visibility = VISIBLE
        //添加页面点击事件
        ll_info.setOnClickListener(this)
        ll_voice.setOnClickListener(this)
        settings_wifi_layout.setOnClickListener(this)
        settings_wireless_layout.setOnClickListener(this)
        settings_robot_official_website_layout.setOnClickListener(this)
        settings_bluetooth_layout.setOnClickListener(this)
        settings_display_layout.setOnClickListener(this)
        settings_sound_layout.setOnClickListener(this)
        settings_apps_layout.setOnClickListener(this)
        settings_date_time_layout.setOnClickListener(this)
        settings_language_layout.setOnClickListener(this)
        settings_inputmethod_layout.setOnClickListener(this)
        settings_device_id_layout.setOnClickListener(this)
        settings_develop_layout.setOnClickListener(this)
        settings_reboot_layout.setOnClickListener(this)
        settings_shutdown_layout.setOnClickListener(this)
        btn_save.setOnClickListener(this)
        test_domains_radio.setOnClickListener(this)
        product_domains_radio.setOnClickListener(this)
        setting_title.setBackKListener(this)
    }

    override fun getContext(): Context {
        return this
    }

    /**
     * Settings.ACTION_WIFI_SETTINGS  打开WiFi设置
     * Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS 打开开发者选项
     * Settings.ACTION_BLUETOOTH_SETTINGS 打开蓝牙设置
     * Settings.ACTION_DATE_SETTINGS   打开时间日期设置
     * Settings.ACTION_INPUT_METHOD_SETTINGS 打开输入法设置
     * Settings.ACTION_SOUND_SETTINGS  打开声音设置
     * Settings.ACTION_APPLICATION_SETTINGS  打开应用信息
     * Settings.ACTION_CAST_SETTINGS  打开投屏设置
     * Settings.ACTION_LOCALE_SETTINGS  打开语言设置
     * Settings.ACTION_DISPLAY_SETTINGS  打开显示设置
     * Settings.ACTION_WIRELESS_SETTINGS 打开热点，移动 Ethernet网络设置
     * 电池不支持打开二级界面
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ll_info -> {
                inc_info.visibility = View.VISIBLE
                inc_hello.visibility = View.GONE
            }
            R.id.ll_voice -> {
                inc_info.visibility = View.GONE
                inc_hello.visibility = View.VISIBLE
            }
            R.id.settings_wifi_layout -> goToActivity(
                this,
                Settings.ACTION_WIFI_SETTINGS
            )
            R.id.settings_wireless_layout -> {
                goToActivity(
                    this,
                    Settings.ACTION_WIRELESS_SETTINGS
                )
            }
            R.id.settings_robot_official_website_layout -> WebPageUtils.goToWebActivity(
                this,
                "",
                "http://www.fitgreat.cn/Home/Index",
                false,
                true
            )
            R.id.settings_bluetooth_layout -> {
                goToActivity(this, Settings.ACTION_BLUETOOTH_SETTINGS)
            }
            R.id.settings_display_layout -> goToActivity(
                this,
                Settings.ACTION_DISPLAY_SETTINGS
            )
            R.id.settings_sound_layout -> goToActivity(
                this,
                Settings.ACTION_SOUND_SETTINGS
            )
            R.id.settings_apps_layout -> goToActivity(
                this,
                Settings.ACTION_APPLICATION_SETTINGS
            )
            R.id.settings_date_time_layout -> goToActivity(
                this,
                Settings.ACTION_DATE_SETTINGS
            )
            R.id.settings_language_layout -> goToActivity(
                this,
                Settings.ACTION_LOCALE_SETTINGS
            )
            R.id.settings_inputmethod_layout -> goToActivity(
                this,
                Settings.ACTION_INPUT_METHOD_SETTINGS
            )
            R.id.settings_device_id_layout -> {
//                startCheckSelf()
            }
            R.id.settings_develop_layout -> goToActivity(
                this,
                Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS
            )
            R.id.settings_reboot_layout -> {
                if (myDialog != null) {
                    if (myDialog!!.isShowing) {
                        myDialog!!.dismiss()
                    }
                    myDialog = null
                }
                myDialog = MyDialog(this)
                myDialog!!.setTitle("重新启动")
                myDialog!!.setMessage("请确认是否要进行系统重启")
                myDialog!!.setCanceledOnTouchOutside(false)
                myDialog!!.setPositiveOnclicListener(
                    "确定"
                ) {
                    myDialog!!.dismiss()
                    ToastUtils.showSmallToast("重启Android系统")
                    DaemonUtils.reboot(getContext())
                }
                myDialog!!.setNegativeOnclicListener(
                    "取消"
                ) { myDialog!!.dismiss() }
                myDialog!!.show()
            }
            R.id.settings_shutdown_layout -> {
                ToastUtils.showSmallToast("关闭Android系统")
                DaemonUtils.reboot(getContext())
            }
            R.id.btn_save -> {
                SpUtil.saveVoiceTip(et_hello.text.toString())
                inputMethodManager!!.hideSoftInputFromWindow(view.windowToken, 0)
                if (de_time.text.toString() == "") {
                    SpUtil.saveDelayTime(10.toString())
                } else {
                    if (Integer.valueOf(de_time.text.toString().trim()) < 10) {
                        SpUtil.saveDelayTime(10.toString())
                        de_time.setText("10")
                        Toast.makeText(getContext(), "间隔时间不能小于10秒!", Toast.LENGTH_SHORT).show()
                    } else {
                        SpUtil.saveDelayTime(de_time.text.trim().toString())
                        Toast.makeText(getContext(), "设置成功！", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.test_domains_radio -> if (!TextUtils.equals(
                    SpUtil.getDevelopmentModel(), "debug"
                )
            ) {
                if (myDialog != null) {
                    if (myDialog!!.isShowing()) {
                        myDialog!!.dismiss()
                    }
                    myDialog = null
                }
                myDialog = MyDialog(this)
                myDialog!!.setTitle("切换环境")
                myDialog!!.setMessage("请确认是否要切换到测试环境")
                myDialog!!.setCanceledOnTouchOutside(false)
                myDialog!!.setPositiveOnclicListener("确定") {
                    myDialog!!.dismiss()
                    SpUtil.saveDevelopmentModel("debug")
                    val selected = getDrawable(R.mipmap.btn_selected)
                    selected.setBounds(0, 0, selected.minimumWidth, selected.minimumHeight)
                    val select = getDrawable(R.mipmap.btn_select)
                    select.setBounds(0, 0, selected.minimumWidth, selected.minimumHeight)
                    test_domains_radio.setCompoundDrawables(selected, null, null, null)
                    product_domains_radio.setCompoundDrawables(select, null, null, null)
                    ToastUtils.showSmallToast("服务器环境已切换，即将重启生效")
                    DaemonUtils.reboot(getContext())
                }
                myDialog!!.setNegativeOnclicListener(
                    "取消"
                ) { myDialog!!.dismiss() }
                myDialog!!.show()
            }
            R.id.product_domains_radio -> if (TextUtils.equals(
                    SpUtil.getDevelopmentModel(), "debug"
                )
            ) {
                if (myDialog != null) {
                    if (myDialog!!.isShowing()) {
                        myDialog!!.dismiss()
                    }
                    myDialog = null
                }
                myDialog = MyDialog(this)
                myDialog!!.setTitle("切换环境")
                myDialog!!.setMessage("请确认是否要切换到正式环境")
                myDialog!!.setCanceledOnTouchOutside(false)
                myDialog!!.setPositiveOnclicListener("确定") {
                    myDialog!!.dismiss()
                    SpUtil.saveDevelopmentModel("product")
                    val selected = getDrawable(R.mipmap.btn_selected)
                    selected.setBounds(0, 0, selected.minimumWidth, selected.minimumHeight)
                    val select = getDrawable(R.mipmap.btn_select)
                    select.setBounds(0, 0, selected.minimumWidth, selected.minimumHeight)
                    test_domains_radio.setCompoundDrawables(select, null, null, null)
                    product_domains_radio.setCompoundDrawables(selected, null, null, null)
                    ToastUtils.showSmallToast("服务器环境已切换，即将重启生效")
                    DaemonUtils.reboot(getContext())
                }
                myDialog!!.setNegativeOnclicListener(
                    "取消"
                ) { myDialog!!.dismiss() }
                myDialog!!.show()
            }
            else -> {
            }
        }
    }

    /**
     * 跳转自检初始化界面
     */
    private fun startCheckSelf() {
//        if (TextUtils.isEmpty(RobotInfoUtils.getAirFaceDeviceId())) {
//            stopService(Intent(this, RobotBrainService::class.java))
//            if (inputDialog != null && !inputDialog.isShowing()) {
//                inputDialog.show()
//                inputDialog.setOnInputDoneListener({
//                    goToActivity(
//                        this@SettingActivity,
//                        RobotInitActivity::class.java
//                    )
//                })
//            }
//        } else {
//            ToastUtils.showSmallToast("当前设备ID:" + RobotInfoUtils.getAirFaceDeviceId())
//        }
    }

    /**
     * 左上角返回按钮
     */
    override fun back() {
        finish()
    }
}