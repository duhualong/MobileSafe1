package com.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ` on 2016/1/11.
 */
public class LostFindActivity extends Activity {
    private SharedPreferences sp;
    private TextView tv_safenumber;
    private ImageView iv_protecting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp=getSharedPreferences("config",MODE_PRIVATE);
        setContentView(R.layout.activity_lost_find);
        //判断一下是否做过设置向导，如果没有做过，就跳转到设置向导页面去设置，否则就留着当前的页面
       boolean configed= sp.getBoolean("configed",false);
        if (configed){
            //就在手机防盗页面
            tv_safenumber= (TextView) findViewById(R.id.tv_safenumber);
            iv_protecting= (ImageView) findViewById(R.id.iv_protecting);
            //得到我们设置的安全号码
            String safenumber=sp.getString("safenumber", "");
            tv_safenumber.setText(safenumber);
            //设置防盗保护的状态
            boolean protecting=sp.getBoolean("protecting",false);
            if (protecting){
                //已经开启防盗保护
                iv_protecting.setImageResource(R.drawable.lock);
            }else {
                //没有开启防盗保护
                iv_protecting.setImageResource(R.drawable.unlock);
            }

        }else {
            //还没有做过设置向导
            Intent intent=new Intent(this,Setup1Activity.class);
            startActivity(intent);
            finish();
        }

    }

    /**
     * 重新进入手机防盗设置向导页面
     * @param view
     */
    public void reEntrySetup(View view){
        Intent intent=new Intent(this,Setup1Activity.class);
        startActivity(intent);
        finish();
    }

}
