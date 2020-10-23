package com.fitgreat.doctorface_top.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import java.util.List;

/**
 * 工具类<p>
 *
 * @author zixuefei
 * @since 2020/3/17 0017 14:03
 */
public class DaemonUtils {

    /**
     * 获取当前ip地址
     *
     * @param context
     * @return
     */
    public static String getLocalIpAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            return int2ip(i);
        } catch (Exception ex) {
            return " 获取IP出错鸟!!!!请保证是WIFI,或者请重新打开网络!\n" + ex.getMessage();
        }
    }

    /**
     * 将ip的整数形式转换成ip形式
     *
     * @param ipInt
     * @return
     */
    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    public static boolean isAppRunning(Context context, String packageName) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = mActivityManager.getRunningTasks(100);
        if (tasks != null && !tasks.isEmpty()) {
            for (ActivityManager.RunningTaskInfo topTask : tasks) {
                if (topTask != null) {
                    ComponentName topActivity = topTask.topActivity;
                    LogUtils.d("AirFaceDaemon", "total size:" + tasks.size() + " processName:" + topActivity.getClassName());
                    if (packageName.equals(topActivity.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

//    public static boolean isAirFaceRobotInstalled() {
//        return checkAppInstalled(AirFaceDaemon.getContext(), "com.fitgreat.airfacerobot");
//    }

//    public static boolean checkAppInstalled(Context context, String pkgName) {
//        if (pkgName == null || pkgName.isEmpty()) {
//            return false;
//        }
//        final PackageManager packageManager = context.getPackageManager();
//        List<PackageInfo> info = packageManager.getInstalledPackages(0);
//        if (info == null || info.isEmpty()) {
//            return false;
//        }
//        LogUtils.d("AirFaceDaemon", "install size:" + info.size());
//        for (PackageInfo packageInfo : info) {
//            if (packageInfo != null) {
//                if (pkgName.equals(packageInfo.packageName)) {
//                    LogUtils.d("AirFaceDaemon", "versionCode:" + packageInfo.versionCode);
//                    RobotInfoUtils.setVersionCode(packageInfo.versionCode);
//                    return true;
//                }
//            }
//        }
//        return false;
//    }


    public static void sendBroadcast(Context context, String action, Bundle data) {
        Intent intent = new Intent(action);
        if (data != null) {
            intent.putExtra("daemon", data);
        }
        intent.setPackage("com.fitgreat.airfacerobot");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.sendBroadcast(intent);
    }


    public static void goHome(Context context) {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(home);
    }

    /**
     * 执行重启,需要系统权限
     */
    public static void reboot(Context context) {
//        Intent i = new Intent(Intent.ACTION_REBOOT);
//        i.putExtra("nowait", 1);
//        i.putExtra("interval", 1);
//        i.putExtra("window", 0);
//        context.sendBroadcast(i);
        //method one
        String cmd="su-c reboot";
        try{
            //获取系统权限
            Runtime.getRuntime().exec(cmd);
        }catch(Exception ex){
        }
    }

    /**
     * 执行关机，需要系统权限 sharedUID
     */
    public static void shutDown(Context context) {
        Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
