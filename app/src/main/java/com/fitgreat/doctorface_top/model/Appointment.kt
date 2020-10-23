package com.fitgreat.doctorface_top.model

/**
 * 病人预约信息
 */
class Appointment : ArrayList<Appointment.AppointmentItem>(){
    data class AppointmentItem(
        val age: String?,
        val booking_department: String?,
        val booking_doctor: String?,
        val booking_time: String?,
        val booking_type: String?,
        val disease: String?,
        val id: Int,
        val phone: String?,
        val return_visit: String?,
        val see: String?,
        val signin: String?,
        val user_name: String?
    )
}