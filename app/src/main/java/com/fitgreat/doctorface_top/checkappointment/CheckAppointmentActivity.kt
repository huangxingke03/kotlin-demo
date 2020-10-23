package com.fitgreat.doctorface_top.checkappointment

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.fitgreat.doctorface_top.R
import com.fitgreat.doctorface_top.base.BaseActivity
import com.fitgreat.doctorface_top.constants.ConstantsString.CHECK_APPOINTMENT_CONDITION
import com.fitgreat.doctorface_top.signin.SignInActivity
import com.fitgreat.doctorface_top.util.ToastUtils
import com.fitgreat.doctorface_top.view.BaseTitleView
import kotlinx.android.synthetic.main.activity_check_appointment.*

/**
 * 预约输入查询页面
 */
class CheckAppointmentActivity : BaseActivity(), BaseTitleView.BaseBackListener,
    View.OnClickListener {

    override fun getLayoutResource(): Int {
        return R.layout.activity_check_appointment
    }

    override fun initData() {
        //页面添加点击事件
        check_appointment_title.setBackKListener(this)
        bt_check_appointment.setOnClickListener(this)
    }

    override fun getContext(): Context {
        return this
    }

    override fun back() {
        finish()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_check_appointment -> {
                checkAppointmentTodo()
            }
        }
    }

    private fun checkAppointmentTodo() {
//        if (check_appointment_condition.text.isNullOrBlank()) {  //手机号为空提示
//            ToastUtils.showSmallToast(getString(R.string.check_appointment_tip))
//            return
//        }
        //跳转签到页面
        val intent = Intent(this, SignInActivity::class.java)
//        intent.putExtra(CHECK_APPOINTMENT_CONDITION, check_appointment_condition.text)
        intent.putExtra(CHECK_APPOINTMENT_CONDITION, "13526749710")
        startActivity(intent)
        finish()
    }
}