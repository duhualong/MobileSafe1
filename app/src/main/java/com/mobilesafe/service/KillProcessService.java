package com.mobilesafe.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import java.util.List;

public class KillProcessService extends Service {

	private LockScreenReceiver receiver;


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		receiver = new LockScreenReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(receiver, filter);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
		receiver = null;
	}
	
	private class LockScreenReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {

			ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);

			List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
			for (RunningAppProcessInfo runningAppProcessInfo : appProcesses) {
				activityManager.killBackgroundProcesses(runningAppProcessInfo.processName);
			}
		}
	}

}
