package com.fitgreat.doctorface_top.signin.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fitgreat.doctorface_top.R
import com.fitgreat.doctorface_top.model.Appointment
import com.fitgreat.doctorface_top.view.SignItemView

class AppointmentAdapter : BaseQuickAdapter<Appointment.AppointmentItem, BaseViewHolder> {
    constructor(
        data: ArrayList<Appointment.AppointmentItem>,
        context: Context
    ) : super(R.layout.item_appointment, data)

    override fun convert(holder: BaseViewHolder, item: Appointment.AppointmentItem) {
        holder.getView<SignItemView>(R.id.item_patient_name).setSignContent(item.user_name)
        holder.getView<SignItemView>(R.id.item_patient_phone).setSignContent(item.phone)
        holder.getView<SignItemView>(R.id.item_reservation_type).setSignContent(item.booking_type)
        holder.getView<SignItemView>(R.id.item_patient_age).setSignContent(item.age)
        holder.getView<SignItemView>(R.id.item_reservation_department).setSignContent(item.booking_department)
        holder.getView<SignItemView>(R.id.item_reservation_department).setSignContent(item.booking_doctor)
        holder.getView<SignItemView>(R.id.item_reservation_time).setSignContent(item.booking_time)
        holder.getView<SignItemView>(R.id.item_patient_symptom).setSignContent(item.disease)

    }
}