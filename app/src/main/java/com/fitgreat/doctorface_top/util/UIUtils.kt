package com.fitgreat.doctorface_top.util

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration
import android.view.WindowManager

object UIUtils {
    /**
     * 屏幕宽度和指定比率计算位置
     *
     * @param ratio 比率
     */
    fun getPointFromScreenWidthRatio(context: Context, ratio: Float): Int {
        return (getScreenWidth(context) * ratio).toInt()
    }

    /**
     * 判断坐标点是否在屏幕左边
     *
     * @param x 点的x坐标
     */
    fun isScreenLeft(context: Context, x: Int): Boolean {
        val halfScreenWidth = getScreenWidth(context) / 2
        return x <= halfScreenWidth
    }

    fun getPointFromScreenHeightRatio(context: Context, ratio: Float): Int {
        return (getScreenHeight(context) * ratio).toInt()
    }

    fun dp2px(ctx: Context?, dp: Int): Int {
        ctx?.let {
            val displayMetrics = getDisplayMetrics(it)
            return (displayMetrics.density * dp + 0.5f).toInt()
        }
        return 0
    }

    fun px2dp(ctx: Context, px: Int): Int {
        val displayMetrics = getDisplayMetrics(ctx)
        return (px / displayMetrics.density).toInt()
    }

    fun getScreenWidth(context: Context): Int {
        val dm = context.resources.displayMetrics
        return dm.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        val dm = context.resources.displayMetrics
        return dm.heightPixels
    }

    fun getDisplayMetrics(context: Context): DisplayMetrics {
        return context.resources.displayMetrics
    }

    /**
     * 在Application onCreate() onConfigurationChanged() 或Activity 的onCreate()中配置
     * xml中长度单位用pt
     *
     * @param ctx
     * @param designWidth 设计图上的宽度
     */
    fun configPT(ctx: Context, designWidth: Float) {
        val displayMetrics = getDisplayMetrics(ctx)
        displayMetrics.xdpi = displayMetrics.widthPixels / designWidth * 72f
    }

    fun pt2px(ctx: Context, pt: Float): Int {
        return (getDisplayMetrics(ctx).xdpi * pt / 72f + 0.5f).toInt()
    }

    fun px2pt(ctx: Context, px: Int): Float {
        return px * 72f / getDisplayMetrics(ctx).xdpi
    }

    fun hasNavigationBar(ctx: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val wm =
                ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            val realSize = Point()
            display.getSize(size)
            display.getRealSize(realSize)
            realSize.y != size.y
        } else {
            val menu = ViewConfiguration.get(ctx).hasPermanentMenuKey()
            val back =
                KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
            !menu && !back
        }
    }
}