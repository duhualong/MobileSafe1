package com.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * Created by ` on 2016/1/11.
 */
public class Setup4Activity extends BaseupActivity {
    private SharedPreferences sp;
    private CheckBox cb_proteting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        sp=getSharedPreferences("config",MODE_PRIVATE);
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
        cb_proteting= (CheckBox) findViewById(R.id.cb_proteting);
       boolean protecting= sp.getBoolean("protecting",false);
        if (protecting){
            //手机防盗已经开启
            cb_proteting.setText("手机防盗已经开启");
            cb_proteting.setChecked(true);
        }else {
            //手机防盗没有开启
            cb_proteting.setText("手机防盗没有开启");
            cb_proteting.setChecked(false);
        }
        cb_proteting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_proteting.setText("手机防盗已经开启");
                } else {
                    cb_proteting.setText("手机防盗没有开启");
                }
                //保存选择状态
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("protecting", isChecked);
                editor.apply();
            }
        });

    }

    @Override
    public void showNext() {
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean("configed", true);
        editor.apply();
        Intent intent=new Intent(this,LostFindActivity.class);
        startActivity(intent);
        finish();
//  overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
        
    }


    @Override
    public void showPre() {
        Intent intent=new Intent(this,Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
    }


}
