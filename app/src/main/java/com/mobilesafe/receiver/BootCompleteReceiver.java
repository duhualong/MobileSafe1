package com.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Created by ` on 2016/1/12.
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    private SharedPreferences sp;
    private TelephonyManager tm;

    @Override
    public void onReceive(Context context, Intent intent) {

        sp = context.getSharedPreferences("config", Context.MODE_APPEND);
        boolean protecting=sp.getBoolean("protecting", false);
        if (protecting){
            //开启防盗保护才执行
            tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            //读取之前保存的sim卡信息
            String saveSim = sp.getString("sim", "");
            //读取当前sim卡信息
            String realSim = tm.getSimSerialNumber();
            //比较是否一样
            if (saveSim.equals(realSim)) {
                //sim卡没有变化

            }else {
                //sim卡已经变更，发一个短信给安全号码
                System.out.println("sim卡已经变更" + saveSim);
                Toast.makeText(context, "sim卡已经变更", Toast.LENGTH_LONG).show();
                SmsManager.getDefault().sendTextMessage(sp.getString("safenumber",""),null,"sim卡挂了，啦啦啦",null,null);
            }
        }


    }
}