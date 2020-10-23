package com.fitgreat.doctorface_top.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils

/**
 * activity跳转工具
 *
 *
 *
 * @author zixuefei
 * @since 2019/7/18 21:29
 */
object RouteUtils {
    fun goHome(context: Context) {
        val home = Intent(Intent.ACTION_MAIN)
        home.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        home.addCategory(Intent.CATEGORY_HOME)
        context.startActivity(home)
    }

    fun <T : Activity?> goToActivity(context: Context?, action: String?, category: String?) {
        try {
            if (context == null || TextUtils.isEmpty(action)) {
                return
            }
            val intent = Intent(action)
            intent.addCategory(category)
            context.startActivity(intent)
        } catch (e: Exception) {
            LogUtils.e("RouteUtils", "EXCEPTION:" + e.message)
        }
    }

    fun goToActivity(context: Context?, action: String?) {
        try {
            if (context == null || TextUtils.isEmpty(action)) {
                return
            }
            val intent = Intent(action)
            context.startActivity(intent)
        } catch (e: Exception) {
            LogUtils.e("RouteUtils", "EXCEPTION:" + e.message)
        }
    }

    fun <T : Activity?> goToActivity(context: Context?, action: String?, data: Bundle?) {
        try {
            if (context == null || TextUtils.isEmpty(action)) {
                return
            }
            val intent = Intent(action)
            if (data != null) {
                intent.putExtras(data)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            LogUtils.e("RouteUtils", "EXCEPTION:" + e.message)
        }
    }

    fun <T : Activity?> goToActivity(context: Context?, activity: Class<T>?) {
        try {
            if (context == null || activity == null) {
                return
            }
            val intent = Intent(context, activity)
            context.startActivity(intent)
        } catch (e: Exception) {
            LogUtils.e("RouteUtils", "EXCEPTION:" + e.message)
        }
    }

    fun <T : Activity?> goToActivity(context: Context?, activity: Class<T>?, data: Bundle?) {
        try {
            if (context == null || activity == null) {
                return
            }
            val intent = Intent(context, activity)
            if (data != null) {
                intent.putExtras(data)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            LogUtils.e("RouteUtils", "EXCEPTION:" + e.message)
        }
    }

    fun <T : Activity?> goToActivity(
        context: Context?,
        activity: Class<T>?,
        data: Bundle?,
        flag: Int
    ) {
        try {
            if (context == null || activity == null) {
                return
            }
            val intent = Intent(context, activity)
            if (data != null) {
                intent.putExtras(data)
            }
            intent.addFlags(flag)
            context.startActivity(intent)
        } catch (e: Exception) {
            LogUtils.e("RouteUtils", "EXCEPTION:" + e.message)
        }
    }

    //    public static void goToWebActivity(Context context, String title, String url, boolean isShowTitleBar, boolean isDarkFontStatus) {
    //        WebPageEntity webPageEntity = new WebPageEntity();
    //        webPageEntity.title = title;
    //        webPageEntity.url = url;
    //        webPageEntity.isShowTitleBar = isShowTitleBar;
    //        webPageEntity.isDarkFont = isDarkFontStatus;
    //        WebpageActivity.startWebpageActivity(context, webPageEntity);
    //    }
    //
    //    public static void goToWebActivity(Context context, String title, String url, boolean isShowTitleBar, boolean isDarkFontStatus, int flag) {
    //        WebPageEntity webPageEntity = new WebPageEntity();
    //        webPageEntity.title = title;
    //        webPageEntity.url = url;
    //        webPageEntity.isShowTitleBar = isShowTitleBar;
    //        webPageEntity.isDarkFont = isDarkFontStatus;
    //        WebpageActivity.startWebpageActivity(context, webPageEntity, flag);
    //    }
    fun openBrowser(context: Context, url: String?) {
        if (TextUtils.isEmpty(url)) {
            LogUtils.e("zxf", "------url is empty------------")
            return
        }
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(url)
        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
        // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(context.packageManager) != null) {
            val componentName = intent.resolveActivity(context.packageManager)
            LogUtils.d("zxf", "componentName = " + componentName.className)
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"))
        } else {
            LogUtils.e("zxf", "------沒有发现可打开程序------------")
        }
    }

    fun sendDaemonBroadcast(context: Context, action: String?, data: Bundle?) {
        val intent = Intent(action)
        if (data != null) {
            intent.putExtra("robot", data)
        }
        intent.setPackage("com.fitgreat.airfacedaemon")
        context.sendBroadcast(intent)
    }
}