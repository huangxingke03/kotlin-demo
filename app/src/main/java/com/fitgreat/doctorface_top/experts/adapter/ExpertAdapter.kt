package com.fitgreat.doctorface_top.experts.adapter

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fitgreat.doctorface_top.R
import com.fitgreat.doctorface_top.model.ExpertListData

class ExpertAdapter : BaseQuickAdapter<ExpertListData.Msg, BaseViewHolder> {
    constructor(data: MutableList<ExpertListData.Msg>, context: Context) : super(R.layout.item_expert, data)

    override fun convert(holder: BaseViewHolder, item: ExpertListData.Msg) {
        Glide.with(context).load(item.image_url).into(holder.getView(R.id.expert_image) as ImageView)
        holder.setText(R.id.expert_name,item.name)
        holder.setText(R.id.expert_job_title,item.academic_title)
        holder.setText(R.id.expert_department,item.department)
        holder.setText(R.id.features_content,item.specialty)
    }
}