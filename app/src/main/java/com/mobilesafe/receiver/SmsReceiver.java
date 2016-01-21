package com.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.mobilesafe.R;
import com.mobilesafe.service.GpsService;

/**
 * Created by ` on 2016/1/12.
 */
public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG ="SmsReceiver" ;
    private SharedPreferences sp;
    //public static final String SMS_RECEIVED_ACTION ="android.provider.Telephony.SMS_RECEIVED";
    @Override
    public void onReceive(Context context, Intent intent) {
        //写接收短信的代码
        Object[] objs= (Object[]) intent.getExtras().get("pdus");
        sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        for (Object b:objs){
            //具体的某一条短信
           SmsMessage sms= SmsMessage.createFromPdu((byte[]) b);
             String sender=sms.getOriginatingAddress();
            String safenumber=sp.getString("safenumber", "");

           // Toast.makeText(context,sender,Toast.LENGTH_LONG).show();
            Log.i(TAG, "!!!!!!sender==!!!!!" + sender);
            String body=sms.getMessageBody();
            System.out.println("！！！啦啦啦啦啦！！！！！！！"+body);

            String number="";
            for (int i=0;i<safenumber.length();i++){
                char ch=safenumber.charAt(i);
                if (Character.isDigit(ch)){
                    number += ch;
                }
            }
            System.out.println("jjjjjjj拉拉啊all"+number);
            if (sender.contains(number)){
                if ("#*location*#".equals(body)){
                    //得到手机的GPS
                    Log.i(TAG,"得到手机的GPS");
                    //启动服务
                    Intent i=new Intent(context, GpsService.class);
                    context.startService(i);
                    SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                    String lastlocation = sp.getString("lastlocation", null);
                    if(TextUtils.isEmpty(lastlocation)){
                        //位置没有得到
                        SmsManager.getDefault().sendTextMessage(sender, null, "geting loaction.....", null, null);
                    }else{
                        SmsManager.getDefault().sendTextMessage(sender, null, lastlocation, null, null);
                    }
                    //把这个广播终止掉
                    abortBroadcast();
                }else if("#*alarm*#".equals(body)){
                    //播放报警影音
                    Log.i(TAG, "播放报警影音");
                    MediaPlayer player = MediaPlayer.create(context,R.raw.ylzs);
                    player.setLooping(false);//
                    player.setVolume(1.0f, 1.0f);
                    player.start();

                    abortBroadcast();
                }else if ("#*wipedata*#".equals(body)){
                    //远程删除数据
                    Log.i(TAG, "远程删除数据");

                }else if ("#*lockscreen*#".equals(body)){
                    //远程锁屏
                    Log.i(TAG, "远程锁屏");

                }
            }

        }

    }
}
