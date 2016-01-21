package com.mobilesafe.bean;

/**
 * Created by ` on 2016/1/18.
 */
public class BlackNumberInfo {
    /**
     * 黑名单电话号码
     */

    private  String number;
    /**
     * 黑名单拦截模式
     * 1电话和短信拦截
     * 2电话拦截
     * 3短信拦截
     */
    private  String mode;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

}
