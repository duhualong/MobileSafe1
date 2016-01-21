package com.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.mobilesafe.service.AddressService;
import com.mobilesafe.service.CallSafeService;
import com.mobilesafe.ui.SettingClickView;
import com.mobilesafe.ui.SettingItemView;
import com.mobilesafe.utils.ServiceStatusUtils;

/**
 * Created by ` on 2016/1/9.
 */
public class SettingActivity extends Activity {
    private SettingItemView siv_update;
    private SettingItemView siv_Address;
    private SettingClickView scvAddressStyle;
    private SettingClickView scvAddressLocation;
    private SharedPreferences sp;
    private SettingItemView siv_callsafe;//黑名单
   private final String[] items=new String[]{"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sp = getSharedPreferences("config", MODE_PRIVATE);

        initUpdateView();
        initAddressView();
        initAddressStyle();
        initAdressLocation();
        initBlackView();
    }
//初始化黑名单
    private void initBlackView() {
        siv_callsafe= (SettingItemView) findViewById(R.id.siv_callsafe);
        //根据归属地服务是否运行勾选checkbox
        boolean serviceRunning=ServiceStatusUtils.isServiceRunning(this, "com.mobilesafe.service.CallSafeService");
        if (serviceRunning){
            siv_callsafe.setChecked(true);
        }else {
            siv_callsafe.setChecked(false);
        }
        siv_callsafe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (siv_callsafe.isChecked()){
                    siv_callsafe.setChecked(false);
                    stopService(new Intent(SettingActivity.this, CallSafeService.class));
                }else {
                    siv_callsafe.setChecked(true);
                    startService(new Intent(SettingActivity.this, CallSafeService.class));//开启归属地服务
                }
            }
        });
    }

    private void initUpdateView(){
        siv_update = (SettingItemView) findViewById(R.id.siv_update);

        boolean update = sp.getBoolean("update", false);
        if(update){
            //自动升级已经开启
            siv_update.setChecked(true);
            //   siv_update.setDesc("自动升级已经开启");
        }else{
            //自动升级已经关闭
            siv_update.setChecked(false);
            //    siv_update.setDesc("自动升级已经关闭");
        }
        siv_update.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                //判断是否有选中
                //已经打开自动升级了
                if (siv_update.isChecked()) {
                    siv_update.setChecked(false);
                    //         siv_update.setDesc("自动升级已经关闭");
                    editor.putBoolean("update", false);

                } else {
                    //没有打开自动升级
                    siv_update.setChecked(true);
                    //     siv_update.setDesc("自动升级已经开启");
                    editor.putBoolean("update", true);
                }
                editor.apply();
            }
        });
    }

    /**
     * 初始化归属地开关
     */
    private void initAddressView(){
        siv_Address= (SettingItemView) findViewById(R.id.siv_address);
        //根据归属地服务是否运行勾选checkbox
        boolean serviceRunning=ServiceStatusUtils.isServiceRunning(this, "com.mobilesafe.service.AddressService");
       if (serviceRunning){
           siv_Address.setChecked(true);
       }else {
           siv_Address.setChecked(false);
       }
        siv_Address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (siv_Address.isChecked()){
                    siv_Address.setChecked(false);
                    stopService(new Intent(SettingActivity.this, AddressService.class));
                }else {
                    siv_Address.setChecked(true);
                    startService(new Intent(SettingActivity.this, AddressService.class));//开启归属地服务
                }
            }
        });

    }
    private  void initAddressStyle(){
        scvAddressStyle= (SettingClickView) findViewById(R.id.scv_address_style);
        scvAddressStyle.setTitle("归属地提示框风格");
        int style=sp.getInt("address_style", 0);//读取保存的style
        scvAddressStyle.setDesc(items[style]);
        scvAddressStyle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleChooseDailog();
            }
        });
        
    }


    /**
     * 弹出选择风格的单选框
     */

    private void showSingleChooseDailog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.dialog_logo);
        builder.setTitle("归属地提示框风格");
        int style=sp.getInt("address_style",0);//读取保存的style
        builder.setSingleChoiceItems(items, style, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sp.edit().putInt("address_style",which).apply();//保存选择的风格
                dialog.dismiss();//让dialog消失
                scvAddressStyle.setDesc(items[which]);//更新组合控件描述的信息

            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();

    }

    /**
     * 修改归属地的位置
     */
    private  void initAdressLocation(){
        scvAddressLocation= (SettingClickView) findViewById(R.id.scv_address_location);
        scvAddressLocation.setTitle("归属地提示框显示位置");
        scvAddressLocation.setDesc("设置归属地提示框的显示位置");
        scvAddressLocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this,DragViewActivity.class));

            }
        });
    }
}

