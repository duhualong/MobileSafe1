package com.mobilesafe;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by ` on 2016/1/11.
 */
public class Setup1Activity extends BaseupActivity {
    //定义一个手势识别器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);

    }
    /**
     * 下一步的点击事件
     */
    public void showNext() {
        Intent intent=new Intent(this,Setup2Activity.class);
        startActivity(intent);
        finish();
        //要求在finish()或者startActivity后面执行
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    @Override
    public void showPre() {

    }

}
