package com.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.mobilesafe.ui.SettingItemView;

/**
 * Created by ` on 2016/1/11.
 */
public class Setup2Activity extends BaseupActivity {
    private SettingItemView siv_setup2_sim;
    private TelephonyManager tm;

    /**
     * 读取sim卡的信息
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        siv_setup2_sim= (SettingItemView) findViewById(R.id.siv_setup2_sim);
        tm= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String sim=sp.getString("sim", null);
        if (TextUtils.isEmpty(sim)){
            //没有绑定
            siv_setup2_sim.setChecked(false);
        }else {
            //已经绑定
            siv_setup2_sim.setChecked(true);
        }
        siv_setup2_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sp.edit();
                if (siv_setup2_sim.isChecked()){
                    siv_setup2_sim.setChecked(false);
                    editor.putString("sim", null);
                }else {
                    siv_setup2_sim.setChecked(true);
                    //保存sim卡的序列号
                    String sim=tm.getSimSerialNumber();
                    editor.putString("sim", sim);
                }
                editor.commit();



            }
        });
    }

    @Override
    public void showNext() {
        //取出是否绑定sim卡
       String sim=sp.getString("sim", null);
        if (TextUtils.isEmpty(sim)){
            //没有绑定
            Toast.makeText(this,"sim卡没有绑定",Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent=new Intent(this,Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    @Override
    public void showPre() {
        Intent intent=new Intent(this,Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
    }

}
