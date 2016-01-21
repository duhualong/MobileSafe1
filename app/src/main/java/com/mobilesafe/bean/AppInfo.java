package com.mobilesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by ` on 2016/1/20.
 */
public class AppInfo {
    //Drawable可表示所有的资源
    private Drawable icon;
    private String apkName;
    private long apkSize;
    //用户app还是系统app true表示用户app，false表示系统app
    private boolean userApp;
    //放置app的位置
    private  boolean isRoom;
    private  String apkPackageName;

    public String getApkPackageName() {
        return apkPackageName;
    }

    public void setApkPackageName(String apkPackageName) {
        this.apkPackageName = apkPackageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public long getApkSize() {
        return apkSize;
    }

    public void setApkSize(long apkSize) {
        this.apkSize = apkSize;
    }

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }

    public boolean isRoom() {
        return isRoom;
    }

    public void setIsRoom(boolean isRoom) {
        this.isRoom = isRoom;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "apkPackageName='" + apkPackageName + '\'' +
                ", apkName='" + apkName + '\'' +
                ", apkSize=" + apkSize +
                ", userApp=" + userApp +
                ", isRoom=" + isRoom +
                '}';
    }
}
