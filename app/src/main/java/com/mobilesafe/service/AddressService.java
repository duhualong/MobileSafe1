package com.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.mobilesafe.R;
import com.mobilesafe.dao.NumberAddressQueryUtils;

/**
 * 来电提醒服务
 * Created by ` on 2016/1/14.
 */
public class AddressService extends Service {
    private TelephonyManager tm;
    private  MyListener listener;
    private OutCallReceiver receiver;
    private WindowManager mWM;
    private View view;
    private SharedPreferences sp;
    private int[] bgs = new int[] { R.drawable.white,
            R.drawable.orange, R.drawable.blue,
            R.drawable.gray, R.drawable.green };
    private int startX;
    private int startY;
    private int winWidth;
    private int winHeight;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sp=getSharedPreferences("config",MODE_PRIVATE);
        tm= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener=new MyListener();
        tm.listen(listener,PhoneStateListener.LISTEN_CALL_STATE);//监听打电话的状态

        receiver=new OutCallReceiver();
        IntentFilter filter=new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(receiver, filter);//动态注册广播
    }
    class  MyListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state){
                case  TelephonyManager.CALL_STATE_RINGING:
                    System.out.println("电话铃响。。。。");
                    //根据来电号码查询归属地
                   String address= NumberAddressQueryUtils.queryNumber(incomingNumber);
                    //Toast.makeText(AddressService.this,address,Toast.LENGTH_LONG).show();
                    showToast(address);
                    break;
                case TelephonyManager.CALL_STATE_IDLE://电话闲置状态
                    if (mWM!=null&&view!=null){
                        mWM.removeView(view);//从Windows移除view
                        view=null;
                    }
                    break;

                default:
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }
    /**
     * 监听去电的广播接收者
     * 需要权限：<uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS"/>
     * Created by ` on 2016/1/15.
     */
    class  OutCallReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String number= getResultData();//获取去电电话号码
            String address=NumberAddressQueryUtils.queryNumber(number);
           // Toast.makeText(context,address,Toast.LENGTH_LONG).show();
            showToast(address);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tm.listen(listener,PhoneStateListener.LISTEN_NONE);
        unregisterReceiver(receiver);//注销广播
    }
    /**
     * 自定义归属地浮窗 需要权限 android.permission.SYSTEM_ALERT_WINDOW
     */
    private  void showToast(String text){
         mWM=(WindowManager)this.getSystemService(Context.WINDOW_SERVICE);//可以在第三方的app显示自己的浮窗
         winWidth=mWM.getDefaultDisplay().getWidth();
         winHeight=mWM.getDefaultDisplay().getHeight();
        final WindowManager.LayoutParams params=new WindowManager.LayoutParams();
        params.height=WindowManager.LayoutParams.WRAP_CONTENT;
        params.width=WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
              //  |WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE 不能触摸
                |WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format= PixelFormat.TRANSLUCENT;
        params.type=WindowManager.LayoutParams.TYPE_PHONE;//电话窗口触摸相应//短暂通知的提示 TYPE_TOAST
        params.gravity= Gravity.START+Gravity.TOP;//将重心位置设置为左上方，也就是（0，0）从左上方，而不是默认的重心位置
        params.setTitle("Toast");

        int lastX=sp.getInt("lastX",0);
        int lastY=sp.getInt("lastY", 0);
        //设置浮窗的位置，基于左上方的偏移量
        params.x=lastX;
        params.y=lastY;
       // view=new TextView(this);
        view= View.inflate(this, R.layout.toast_address,null);
        int style=sp.getInt("address_style",0);
        view.setBackgroundResource(bgs[style]);//根据存储样式更新背景
        TextView tvText= (TextView) view.findViewById(R.id.tv_number);
        tvText.setText(text);
        mWM.addView(view, params);//将view添加到屏幕上
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();

                        // 计算移动偏移量
                        int dx = endX - startX;
                        int dy = endY - startY;
                        //更新浮窗位置
                        params.x+=dx;
                        params.y+=dy;
                        //防止坐标偏移屏幕
                        if (params.x<0){
                            params.x=0;
                        }
                        if (params.y<0){
                            params.y=0;
                        }
                        if (params.x>winWidth-view.getWidth()){
                            params.x=winWidth-view.getWidth();
                        }
                        if (params.y>winHeight-view.getHeight()){
                            params.y=winHeight-view.getHeight();
                        }

                      //  System.out.println("x:"+params.x+"\n"+"y:"+params.y);
                        mWM.updateViewLayout(view,params);
                        //重初始化起点坐标
                        startX= (int) event.getRawX();
                        startY= (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //记录坐标点
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putInt("lastX",params.x);
                        editor.putInt("lastY",params.y);
                        editor.apply();
                        break;
                }
              //  System.out.println("别摸我了，sb");

                return true;
            }
        });



    }
}
