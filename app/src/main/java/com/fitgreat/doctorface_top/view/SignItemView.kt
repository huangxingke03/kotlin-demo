package com.fitgreat.doctorface_top.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.fitgreat.doctorface_top.R

class SignItemView : ConstraintLayout {
    private var itemContentName: String? = null
    private var infoContent: TextView? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    private var mItemClickListener: ItemClickListener? = null

    interface ItemClickListener {
        fun itemClick()
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.mItemClickListener = itemClickListener
    }

    fun initView(context: Context, attrs: AttributeSet?) {
        var typedArray = context.obtainStyledAttributes(attrs, R.styleable.SignItemView)
        var itemTitleName = typedArray.getString(R.styleable.SignItemView_signItemTitle)
        itemContentName = typedArray.getString(R.styleable.SignItemView_signItemContent)

        val inflate = LayoutInflater.from(context).inflate(R.layout.view_sign_item, this, true)
        //添加条目点击事件
        inflate.setOnClickListener { mItemClickListener?.let { it.itemClick() } }
        //初始化view值
        val infoTitle = inflate.findViewById<TextView>(R.id.sign_info_title)
        infoContent = inflate.findViewById<TextView>(R.id.sign_info_content)
        infoTitle.text = itemTitleName
        infoContent?.text = itemContentName
        typedArray.recycle()
    }

    /**
     * 更新内容信息
     */
    fun setSignContent(content: String?) {
        infoContent?.text = content
    }
}
