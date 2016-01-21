package com.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mobilesafe.dao.NumberAddressQueryUtils;

/**
 * 监听去电的广播接收者
 * 需要权限：<uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS"/>
 * Created by ` on 2016/1/15.
 */
public class OutCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       String number= getResultData();//获取去电电话号码
       String address=NumberAddressQueryUtils.queryNumber(number);
        Toast.makeText(context,address,Toast.LENGTH_LONG).show();

    }
}
