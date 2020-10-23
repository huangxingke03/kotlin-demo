package com.fitgreat.doctorface_top.util

import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.fitgreat.doctorface_top.MyApp
import com.fitgreat.doctorface_top.R
import java.security.AccessController.getContext

object ToastUtils {
    fun showSmallToast(tvStr: String?) {
        if (TextUtils.isEmpty(tvStr)) {
            return
        }
        try {
            val toast2 = Toast(MyApp.getContext())
            val view: View = LayoutInflater.from(MyApp.getContext())
                .inflate(R.layout.publish_success_tip, null)
            val tv = view.findViewById<TextView>(R.id.toast_text)
            tv.text = tvStr
            toast2.view = view
            toast2.setGravity(Gravity.CENTER, 0, 0)
            toast2.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}