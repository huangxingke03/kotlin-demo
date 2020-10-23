package com.fitgreat.doctorface_top.signin

import com.fitgreat.doctorface_top.base.BaseView
import com.fitgreat.doctorface_top.model.Appointment


interface SignInView:BaseView {
    fun signInSuccess()

    /**
     * 预约信息展示
     */
    fun showAppointment(data: ArrayList<Appointment.AppointmentItem>)
}