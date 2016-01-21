package com.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * 服务状态工具类
 * Created by ` on 2016/1/15.
 */
public class ServiceStatusUtils {
    /**
     * 检查服务是否在运行
     * @return
     */
    public  static  boolean isServiceRunning(Context ctx,String serviceName){

        ActivityManager am= (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
       List<ActivityManager.RunningServiceInfo> runningServices=am.getRunningServices(100);
       for (ActivityManager.RunningServiceInfo runningServiceInfo:runningServices){
           String className=runningServiceInfo.service.getClassName();
           System.out.println("正在运行的服务："+className);
           if (className.equals(serviceName)){
               return  true;
           }
       }
        return false;
    }
}
