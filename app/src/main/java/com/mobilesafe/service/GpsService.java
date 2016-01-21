package com.mobilesafe.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ` on 2016/1/13.
 */
public class GpsService extends Service {
    private LocationManager lm;
    private MyLocationListener listener;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
//      List<String> provider=lm.getAllProviders();
//        for (String l:provider){
//            System.out.println("位置提供者："+l);
//        }
        listener = new MyLocationListener();
        //注册监听位置服务
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //注册监听服务
        //给位置提供者设置条件
        Criteria criteria=new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        criteria.setAltitudeRequired(false);//不要求海拔信息
//        criteria.setBearingRequired(false);//不要求方位信息
//        criteria.setCostAllowed(true);//是否允许付费
        //       criteria.setPowerRequirement(Criteria.POWER_LOW);//对电量的要求
        String proveder=lm.getBestProvider(criteria, true);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 50, listener);
    }
    class MyLocationListener implements LocationListener {

        /**
         * 当位置改变的时候回调
         * @param location
         */


        @Override
        public void onLocationChanged(Location location) {
            String longitude="经度："+ location.getLongitude()+"\n";
            String latitude= "维度："+location.getLatitude()+"\n";
            String accuracy="精确度："+location.getAccuracy()+"\n";
//            TextView textView=new TextView(GpsService.this);
//            textView.setText(longitude+"\n"+latitude+"\n"+accuracy);
            // 发短信给安全号码

            // 把标准的GPS坐标转换成火星坐标
			InputStream is;
			try {
				is = getAssets().open("axisoffset.dat");
				ModifyOffset offset = ModifyOffset.getInstance(is);
				PointDouble double1 = offset.s2c(new PointDouble(location
						.getLongitude(), location.getLatitude()));
				longitude ="经度：" + offset.X+ "\n";
				latitude =  "维度：" +offset.Y+ "\n";

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("lastlocation", longitude + latitude + accuracy);
            editor.commit();

        }

        /**
         * 当状态发生改变的时候回调，从开启到关闭，关闭到开启
         * @param provider
         * @param status
         * @param extras
         */

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        /**
         * 某一个位置提供者可使用了
         * @param provider
         */

        @Override
        public void onProviderEnabled(String provider) {

        }
        /**
         * 某一个位置提供者不可使用了
         * @param provider
         */

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消监听位置服务
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.removeUpdates(listener);
        listener=null;
    }
}
