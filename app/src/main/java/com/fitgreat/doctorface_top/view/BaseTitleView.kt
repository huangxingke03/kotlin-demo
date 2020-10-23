package com.fitgreat.doctorface_top.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.fitgreat.doctorface_top.R

class BaseTitleView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initializeView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initializeView(context, attrs)
    }

    private var mBaseBackListener: BaseBackListener? = null

    interface BaseBackListener {
        fun back()
    }

    fun setBackKListener(baseBackListener: BaseBackListener) {
        this.mBaseBackListener = baseBackListener
    }

    private fun initializeView(context: Context, attrs: AttributeSet?) {
        var typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseTitleView)
        var titleName = typedArray.getString(R.styleable.BaseTitleView_titleName)
        val inflate = LayoutInflater.from(context).inflate(R.layout.view_base_title, this, true)
        val baseBack = inflate.findViewById<ImageView>(R.id.base_back)
        val baseTitle = inflate.findViewById<TextView>(R.id.base_title)
        baseBack.setOnClickListener {
            mBaseBackListener?.let {
                it.back()
            }
        }
        baseTitle.text = titleName
        typedArray.recycle()
    }
}
